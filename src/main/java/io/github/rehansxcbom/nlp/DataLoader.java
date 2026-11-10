package io.github.rehansxcbom.nlp;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DataLoader {

  public static void loadCsvAndTrain(LanguageDetector detector, String filePath) {
    Path path = Paths.get(filePath);

    if (!Files.exists(path)) {
      throw new IllegalArgumentException("Could not find file at: " + path.toAbsolutePath());
    }

    try (BufferedReader br = Files.newBufferedReader(path)) {
      String line;
      boolean isHeader = true;

      while ((line = br.readLine()) != null) {
        if (line.trim().isEmpty())
          continue;

        if (isHeader) {
          isHeader = false;
          continue;
        }

        String[] parts = line.split(",", 2);
        if (parts.length == 2) {
          String language = parts[0].trim();
          String text = parts[1].trim();

          if (text.startsWith("\"") && text.endsWith("\"")) {
            text = text.substring(1, text.length() - 1);
          }

          detector.train(text, language);
        }
      }
    } catch (IOException e) {
      System.err.println("Failed to read the dataset: " + e.getMessage());
    }
  }
}
