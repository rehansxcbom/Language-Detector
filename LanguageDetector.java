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
}
