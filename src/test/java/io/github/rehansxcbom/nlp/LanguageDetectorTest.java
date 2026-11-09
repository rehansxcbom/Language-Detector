package io.github.rehansxcbom.nlp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LanguageDetectorTest {

  private LanguageDetector detector;

  @BeforeEach
  void setUp() {
    // This runs before every single test to give us a fresh, blank brain
    detector = new LanguageDetector();
  }

  @Test
  void testPredictionBeforeTraining() {
    // If we haven't trained it, it should return our default fallback
    String result = detector.predict("Hello world");
    assertEquals("Unknown", result);
  }

  @Test
  void testBasicLanguageDetection() {
    detector.train("The quick brown fox jumps over the lazy dog.", "English");
    detector.train("I love programming in Java.", "English");

    detector.train("El zorro marrón rápido salta sobre el perro perezoso.", "Spanish");
    detector.train("Me encanta programar en Java.", "Spanish");

    // Exact vocabulary matches
    assertEquals("English", detector.predict("quick brown fox"));
    assertEquals("Spanish", detector.predict("zorro marrón"));
  }

  @Test
  void testCaseAndPunctuationInsensitivity() {
    // Train with messy punctuation and uppercase
    detector.train("HELLO!!! HOW ARE YOU???", "English");

    // Predict with lowercase and no punctuation
    String result = detector.predict("hello how are you");
    assertEquals("English", result, "Tokenizer should ignore case and punctuation.");
  }

  @Test
  void testLaplaceSmoothingOnUnknownWords() {
    detector.train("Hello friend", "English");
    detector.train("Hola amigo", "Spanish");

    // "computer" is a word the model has never seen.
    // Without Laplace smoothing, this would cause a probability of 0 and crash the
    // math.
    // Because it knows "Hello", it should still lean English.
    String result = detector.predict("Hello computer");
    assertEquals("English", result, "Laplace smoothing failed to handle an unknown word.");
  }

  @Test
  void testEmptyOrSymbolOnlyStrings() {
    detector.train("Hello world", "English");
    detector.train("Hola mundo", "Spanish");

    // These strings contain no valid Unicode letters, so the tokenizer returns
    // empty arrays.
    // The engine should not throw exceptions (like divide-by-zero).
    assertDoesNotThrow(() -> {
      detector.predict("");
      detector.predict("   ");
      detector.predict("!?@#");
    });
  }

  @Test
  void testPriorProbabilityTieBreaker() {
    // Train English 3 times, Spanish 1 time
    detector.train("One", "English");
    detector.train("Two", "English");
    detector.train("Three", "English");
    detector.train("Uno", "Spanish");

    // Predict a word it has NEVER seen ("Apple").
    // Because of Laplace smoothing, both languages have the same Likelihood for
    // this word.
    // But English has a higher Prior probability (75% vs 25%), so it should win the
    // tie-breaker.
    assertEquals("English", detector.predict("Apple"));
  }
}
