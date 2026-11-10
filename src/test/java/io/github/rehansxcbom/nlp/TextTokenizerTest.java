package io.github.rehansxcbom.nlp;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TextTokenizerTest {

  @Test
  void testBasicTokenization() {
    String[] result = TextTokenizer.tokenize("Hello World");
    assertArrayEquals(new String[] { "hello", "world" }, result);
  }

  @Test
  void testPunctuationRemoval() {
    String[] result = TextTokenizer.tokenize("HELLO!!! HOW ARE YOU???");
    assertArrayEquals(new String[] { "hello", "how", "are", "you" }, result);
  }

  @Test
  void testUnicodeCharacters() {
    // Ensures Spanish/French accents are kept, not deleted by the regex
    String[] result = TextTokenizer.tokenize("¿Cómo estás, mon ami?");
    assertArrayEquals(new String[] { "cómo", "estás", "mon", "ami" }, result);
  }

  @Test
  void testEmptyOrNullStrings() {
    assertArrayEquals(new String[] {}, TextTokenizer.tokenize(null));
    assertArrayEquals(new String[] { "" }, TextTokenizer.tokenize(""));
    assertArrayEquals(new String[] { "" }, TextTokenizer.tokenize("   "));
    assertArrayEquals(new String[] { "" }, TextTokenizer.tokenize("!@#$"));
  }
}
