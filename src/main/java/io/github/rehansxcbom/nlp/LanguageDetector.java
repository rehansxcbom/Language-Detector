package io.github.rehansxcbom.nlp;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class LanguageDetector implements Serializable {

  private static final long serialVersionUID = 3L;

  private final double alpha;

  private Map<String, Integer> documentCountPerLanguage = new HashMap<>();
  private Map<String, Integer> totalNGramsPerLanguage = new HashMap<>();
  private int totalDocuments = 0;

  private int nextGramId = 0;
  private Map<String, Integer> dictionary = new HashMap<>();

  private Map<String, Map<Integer, Integer>> nGramCountsPerLanguage = new HashMap<>();

  public LanguageDetector() {
    this(1.0);
  }

  public LanguageDetector(double alpha) {
    this.alpha = alpha;
  }

  public static class Prediction {
    public final String language;
    public final double confidence;

    public Prediction(String language, double confidence) {
      this.language = language;
      this.confidence = confidence;
    }
  }

  public void train(String text, String language) {
    totalDocuments++;
    documentCountPerLanguage.put(language, documentCountPerLanguage.getOrDefault(language, 0) + 1);
    nGramCountsPerLanguage.putIfAbsent(language, new HashMap<>());

    Map<Integer, Integer> currentLangCounts = nGramCountsPerLanguage.get(language);
    String[] nGrams = TextTokenizer.tokenize(text);

    for (String gram : nGrams) {
      Integer gramId = dictionary.get(gram);
      if (gramId == null) {
        gramId = nextGramId++;
        dictionary.put(gram, gramId);
      }

      currentLangCounts.put(gramId, currentLangCounts.getOrDefault(gramId, 0) + 1);
      totalNGramsPerLanguage.put(language, totalNGramsPerLanguage.getOrDefault(language, 0) + 1);
    }
  }

  public Prediction predict(String text) {
    if (documentCountPerLanguage.isEmpty()) {
      return new Prediction("Unknown", 0.0);
    }

    String[] nGrams = TextTokenizer.tokenize(text);
    int vocabSize = dictionary.size();

    Map<String, Double> languageScores = new HashMap<>();
    double maxLogScore = Double.NEGATIVE_INFINITY;

    for (String language : documentCountPerLanguage.keySet()) {
      double logPrior = Math.log((double) documentCountPerLanguage.get(language) / totalDocuments);
      double logLikelihood = 0.0;

      Map<Integer, Integer> counts = nGramCountsPerLanguage.get(language);
      int totalGramCount = totalNGramsPerLanguage.getOrDefault(language, 0);

      for (String gram : nGrams) {
        Integer gramId = dictionary.get(gram);
        int count = 0;
        if (gramId != null) {
          count = counts.getOrDefault(gramId, 0);
        }

        double prob = (count + alpha) / (totalGramCount + (alpha * vocabSize));
        logLikelihood += Math.log(prob);
      }

      double totalScore = logPrior + logLikelihood;
      languageScores.put(language, totalScore);

      if (totalScore > maxLogScore) {
        maxLogScore = totalScore;
      }
    }

    double sumExp = 0.0;
    for (Map.Entry<String, Double> entry : languageScores.entrySet()) {
      sumExp += Math.exp(entry.getValue() - maxLogScore);
    }

    String bestLanguage = "Unknown";
    double highestConfidence = 0.0;

    for (Map.Entry<String, Double> entry : languageScores.entrySet()) {
      double confidence = Math.exp(entry.getValue() - maxLogScore) / sumExp;
      if (confidence > highestConfidence) {
        highestConfidence = confidence;
        bestLanguage = entry.getKey();
      }
    }

    return new Prediction(bestLanguage, highestConfidence);
  }
}
