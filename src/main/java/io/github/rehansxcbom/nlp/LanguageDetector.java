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

  private String[] tokenize(String text) {
    return text.toLowerCase()
        .replaceAll("[^\\{L}\\s]", "")
        .split("\\s+");
  }

  public void train(String text, String language) {
    totalDocuments = totalDocuments + 1;

    documentCountPerLanguage.put(language, documentCountPerLanguage.getOrDefault(language, 0) + 1);

    wordCountsPerLanguage.putIfAbsent(language, new HashMap<>());
    Map<String, Integer> currentLangWordCounts = wordCountsPerLanguage.get(language);

    String[] words = tokenize(text);

    for (String word : words) {
      // Skip empty strings that might sneak through multiple spaces
      if (word.isEmpty())
        continue;

      vocabulary.add(word);

      currentLangWordCounts.put(word, currentLangWordCounts.getOrDefault(word, 0) + 1);

      totalWordsPerLanguage.put(language, totalWordsPerLanguage.getOrDefault(language, 0) + 1);
    }
  }

  public String predict(String text) {
    String bestLanguage = "Unknown";
    double highestLogProb = Double.NEGATIVE_INFINITY; // Start at the lowest possible number

    String[] words = tokenize(text);
    int vocabSize = vocabulary.size();

    // Loop through every language we learned during training
    for (String language : documentCountPerLanguage.keySet()) {

      // 1. The Prior: Probability of this language occurring overall
      double priorProb = (double) documentCountPerLanguage.get(language) / totalDocuments;
      double logPrior = Math.log(priorProb);

      double logLikelihood = 0.0;
      Map<String, Integer> currentLangWordCounts = wordCountsPerLanguage.get(language);
      int totalWordsInThisLang = totalWordsPerLanguage.getOrDefault(language, 0);

      // 2. The Likelihood: Probability of these specific words in this language
      for (String word : words) {
        if (word.isEmpty())
          continue;

        // Get how many times we saw this word in this language (default to 0 if never
        // seen)
        int wordCount = currentLangWordCounts.getOrDefault(word, 0);

        // Laplace Smoothing: (count + 1) / (total words in lang + total unique words
        // ever seen)
        double wordProb = (double) (wordCount + 1) / (totalWordsInThisLang + vocabSize);

        // Add the log probability
        logLikelihood += Math.log(wordProb);
      }

      // 3. Combine Prior and Likelihood
      double totalScore = logPrior + logLikelihood;

      // Check if this language has the highest score so far
      if (totalScore > highestLogProb) {
        highestLogProb = totalScore;
        bestLanguage = language;
      }
    }

    return bestLanguage;
  }
}
