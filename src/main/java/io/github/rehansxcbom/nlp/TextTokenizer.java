package io.github.rehansxcbom.nlp;

import java.util.ArrayList;
import java.util.List;

public class TextTokenizer {

  public static String[] tokenize(String text) {
    if (text == null)
      return new String[0];

    String cleaned = text.toLowerCase().replaceAll("[^\\p{L}\\s]", "");

    cleaned = " " + cleaned.trim().replaceAll("\\s+", " ") + " ";

    List<String> trigrams = new ArrayList<>();

    for (int i = 0; i < cleaned.length() - 2; i++) {
      trigrams.add(cleaned.substring(i, i + 3));
    }

    return trigrams.toArray(new String[0]);
  }
}
