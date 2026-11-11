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
    LanguageDetector.Prediction result = detector.predict("Hello world");
    assertEquals("Unknown", result.language, "Untrained engine should return 'Unknown'");
    assertEquals(0.0, result.confidence, "Untrained engine should have 0.0 confidence");
  }

  @Test
  void testBasicLanguageDetection() {
    detector.train("The quick brown fox", "English");
    detector.train("El zorro marrón rápido", "Spanish");

    LanguageDetector.Prediction engPrediction = detector.predict("quick brown");
    assertEquals("English", engPrediction.language);
    assertTrue(engPrediction.confidence > 0.5, "Confidence should be strong for exact matches");

    LanguageDetector.Prediction spanPrediction = detector.predict("zorro marrón");
    assertEquals("Spanish", spanPrediction.language);
  }

  @Test
  void testLaplaceSmoothingOnUnknownWords() {
    detector.train("Hello friend", "English");
    detector.train("Hola amigo", "Spanish");

    LanguageDetector.Prediction result = detector.predict("Hello computer");
    assertEquals("English", result.language);
  }

  @Test
  void testPriorProbabilityTieBreaker() {
    detector.train("a", "English");
    detector.train("b", "English");
    detector.train("c", "English");

    detector.train("x y z", "Spanish");

    LanguageDetector.Prediction result = detector.predict("q");
    assertEquals("English", result.language);
  }
}
