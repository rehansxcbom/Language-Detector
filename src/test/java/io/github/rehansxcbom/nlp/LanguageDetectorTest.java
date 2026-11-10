package io.github.rehansxcbom.nlp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LanguageDetectorTest {

  private LanguageDetector detector;

  @BeforeEach
  void setUp() {
    detector = new LanguageDetector();
  }

  @Test
  void testPredictionBeforeTraining() {
    String result = detector.predict("Hello world");
    assertEquals("Unknown", result, "Untrained engine should return 'Unknown'");
  }

  @Test
  void testBasicLanguageDetection() {
    detector.train("The quick brown fox", "English");
    detector.train("El zorro marrón rápido", "Spanish");

    assertEquals("English", detector.predict("quick brown fox"));
    assertEquals("Spanish", detector.predict("zorro marrón"));
  }

  @Test
  void testLaplaceSmoothingOnUnknownWords() {
    detector.train("Hello friend", "English");
    detector.train("Hola amigo", "Spanish");

    // "computer" is unknown. Laplace smoothing prevents a crash/zero-probability.
    String result = detector.predict("Hello computer");
    assertEquals("English", result);
  }

  @Test
  void testPriorProbabilityTieBreaker() {
    // English has a 75% prior probability, Spanish has 25%
    detector.train("One", "English");
    detector.train("Two", "English");
    detector.train("Three", "English");
    detector.train("Uno", "Spanish");

    // "Apple" is unknown to both. Both likelihoods are equal.
    // English should win purely because it appears more frequently in the training
    // data.
    assertEquals("English", detector.predict("Apple"));
  }
}
