package io.github.rehansxcbom.nlp;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;

public class ModelSerializer {

  public static void saveModel(LanguageDetector detector, String filePath) {
    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
      oos.writeObject(detector);
      System.out.println("Model successfully saved to: " + filePath);
    } catch (IOException e) {
      System.err.println("Failed to save the model: " + e.getMessage());
    }
  }

  public static LanguageDetector loadModel(String filePath) {
    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
      return (LanguageDetector) ois.readObject();
    } catch (IOException | ClassNotFoundException e) {
      System.err.println("Failed to load the model: " + e.getMessage());
      return null;
    }
  }
}
