import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class LanguageDetector {

  // 1. How many sentences of each language we've seen
  private Map<String, Integer> documentCountPerLanguage = new HashMap<>();

  // 2. Frequency of each word per language: Map<Language, Map<Word, Count>>
  private Map<String, Map<String, Integer>> wordCountsPerLanguage = new HashMap<>();

  // 3. Total number of words in each language
  private Map<String, Integer> totalWordsPerLanguage = new HashMap<>();

  // 4. Every unique word we've ever seen
  private Set<String> vocabulary = new HashSet<>();

  // Total sentences processed overall
  private int totalDocuments = 0;

  private String[] tokenize(String text) {
    return text.toLowerCase()
        .replaceAll("[^\\{L}\\s]", "")
        .split("\\s+");
  }
}
