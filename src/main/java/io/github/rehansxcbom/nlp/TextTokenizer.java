package io.github.rehansxcbom.nlp;

public class TextTokenizer {

  public static String[] tokenize(String text) {
    if (text == null)
      return new String[0];

    return text.toLowerCase()
        .replaceAll("[^\\p{L}\\s]", "")
        .split("\\s+");
  }
}
