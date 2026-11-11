package io.github.rehansxcbom.nlp;

import java.util.Scanner;

public class Main {

  private static final String RESET = "\u001B[0m";
  private static final String GREEN = "\u001B[32m";
  private static final String YELLOW = "\u001B[33m";
  private static final String RED = "\u001B[31m";
  private static final String CYAN = "\u001B[36m";

  public static void main(String[] args) {
    if (args.length < 1) {
      System.err.println(RED + "Usage: java -jar detector.jar <dataset.csv | model.ser>" + RESET);
      System.exit(1);
    }

    String inputPath = args[0];
    LanguageDetector detector;

    System.out.println(CYAN + "=== Naive Bayes Language Engine ===" + RESET);

    if (inputPath.endsWith(".ser")) {
      System.out.print("Loading pre-trained model... ");
      long startTime = System.currentTimeMillis();

      detector = ModelSerializer.loadModel(inputPath);
      if (detector == null)
        System.exit(1);

      System.out.println(GREEN + "Done (" + (System.currentTimeMillis() - startTime) + "ms)" + RESET);
    } else {
      System.out.print("Training new model from dataset... ");
      long startTime = System.currentTimeMillis();

      detector = new LanguageDetector();
      DataLoader.loadCsvAndTrain(detector, inputPath);

      System.out.println(GREEN + "Done (" + (System.currentTimeMillis() - startTime) + "ms)" + RESET);

      String savePath = "language_model.ser";
      ModelSerializer.saveModel(detector, savePath);
    }

    System.out.println(CYAN + "Engine ready. Type a sentence (or 'exit' to quit):" + RESET);

    try (Scanner scanner = new Scanner(System.in)) {
      while (true) {
        System.out.print("\n> ");
        String input = scanner.nextLine();

        if (input.equalsIgnoreCase("exit")) {
          System.out.println(YELLOW + "Shutting down engine." + RESET);
          break;
        }

        if (input.trim().isEmpty())
          continue;

        LanguageDetector.Prediction prediction = detector.predict(input);
        String formattedConfidence = String.format("%.2f%%", prediction.confidence * 100);

        // The Gibberish Threshold
        if (prediction.confidence < 0.25) {
          System.out.println("  Result: " + RED + "Unknown / Gibberish" + RESET + " (Top match was "
              + prediction.language + " at only " + formattedConfidence + ")");
        } else if (prediction.confidence < 0.60) {
          System.out.println(
              "  Result: " + YELLOW + prediction.language + " (" + formattedConfidence + " confidence)" + RESET);
        } else {
          System.out.println(
              "  Result: " + GREEN + prediction.language + " (" + formattedConfidence + " confidence)" + RESET);
        }
      }
    }
  }
}
