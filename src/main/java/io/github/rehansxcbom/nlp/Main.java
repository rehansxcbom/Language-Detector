package io.github.rehansxcbom.nlp;

import java.util.Scanner;

public class Main {

  public static void main(String[] args) {
    if (args.length < 1) {
      System.err.println("Usage: java -cp target/classes io.github.rehansxcbom.nlp.Main <path-to-dataset.csv>");
      System.exit(1);
    }

    String datasetPath = args[0];
    LanguageDetector detector = new LanguageDetector();

    System.out.println("Loading dataset from: " + datasetPath + "...");
    long startTime = System.currentTimeMillis();

    DataLoader.loadCsvAndTrain(detector, datasetPath);

    long endTime = System.currentTimeMillis();

    System.out.println("Training complete in " + (endTime - startTime) + "ms!");
    System.out.println("Type a sentence to detect its language (or 'exit' to quit):");

    try (Scanner scanner = new Scanner(System.in)) {
      while (true) {
        System.out.print("> ");
        String input = scanner.nextLine();

        if (input.equalsIgnoreCase("exit")) {
          System.out.println("Shutting down engine.");
          break;
        }

        if (input.trim().isEmpty())
          continue;

        String prediction = detector.predict(input);
        System.out.println("  Predicted: " + prediction);
      }
    }
  }
}
