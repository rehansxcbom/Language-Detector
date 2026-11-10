package io.github.rehansxcbom.nlp;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class LanguageDetector {

  private Map<String, Integer> documentCountPerLanguage = new HashMap<>();
  private Map<String, Map<String, Integer>> wordCountsPerLanguage = new HashMap<>();
  private Map<String, Integer> totalWordsPerLanguage = new HashMap<>();
  private Set<String> vocabulary = new HashSet<>();
  private int totalDocuments = 0;

  public void train(String text, String language) {
    totalDocuments++;
    documentCountPerLanguage.put(language, documentCountPerLanguage.getOrDefault(language, 0) + 1);

    wordCountsPerLanguage.putIfAbsent(language, new HashMap<>());
    Map<String, Integer> currentLangWordCounts = wordCountsPerLanguage.get(language);

    // Uses our new static utility class
    String[] words = TextTokenizer.tokenize(text);

    for (String word : words) {
      if (word.isEmpty())
        continue;

      vocabulary.add(word);
      currentLangWordCounts.put(word, currentLangWordCounts.getOrDefault(word, 0) + 1);
      totalWordsPerLanguage.put(language, totalWordsPerLanguage.getOrDefault(language, 0) + 1);
    }
  }

  public String predict(String text) {
    String bestLanguage = "Unknown";
    double highestLogProb = Double.NEGATIVE_INFINITY;

    String[] words = TextTokenizer.tokenize(text);
    int vocabSize = vocabulary.size();

    for (String language : documentCountPerLanguage.keySet()) {
      double priorProb = (double) documentCountPerLanguage.get(language) / totalDocuments;
      double logPrior = Math.log(priorProb);

      double logLikelihood = 0.0;
      Map<String, Integer> currentLangWordCounts = wordCountsPerLanguage.get(language);
      int totalWordsInThisLang = totalWordsPerLanguage.getOrDefault(language, 0);

      for (String word : words) {
        if (word.isEmpty())
          continue;

        int wordCount = currentLangWordCounts.getOrDefault(word, 0);
        double wordProb = (double) (wordCount + 1) / (totalWordsInThisLang + vocabSize);
        logLikelihood += Math.log(wordProb);
      }

      double totalScore = logPrior + logLikelihood;

      if (totalScore > highestLogProb) {
        highestLogProb = totalScore;
        bestLanguage = language;
      }
    }

    return bestLanguage;
  }
}
