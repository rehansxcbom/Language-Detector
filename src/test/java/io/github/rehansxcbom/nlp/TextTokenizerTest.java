package io.github.rehansxcbom.nlp;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TextTokenizerTest {

  @Test
  void testBasicTrigramTokenization() {
    String[] result = TextTokenizer.tokenize("Hello");
    assertArrayEquals(new String[] { " he", "hel", "ell", "llo", "lo " }, result);
  }

  @Test
  void testShortWordsTokenization() {
    String[] result = TextTokenizer.tokenize("Hi");
    assertArrayEquals(new String[] { " hi", "hi " }, result);
  }

  @Test
  void testPunctuationRemoval() {
    String[] result = TextTokenizer.tokenize("Hi!!!");
    assertArrayEquals(new String[] { " hi", "hi " }, result);
  }

  @Test
  void testUnicodeCharacters() {
    // Ensures foreign characters are kept in the trigrams
    String[] result = TextTokenizer.tokenize("año");
    assertArrayEquals(new String[] { " añ", "año", "ño " }, result);
  }

  @Test
  void testEmptyOrNullStrings() {
    assertArrayEquals(new String[] {}, TextTokenizer.tokenize(null));
    assertArrayEquals(new String[] {}, TextTokenizer.tokenize(""));
    assertArrayEquals(new String[] {}, TextTokenizer.tokenize("   "));
    assertArrayEquals(new String[] {}, TextTokenizer.tokenize("!@#$"));
  }
}
