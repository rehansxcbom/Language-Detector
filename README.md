# Naive Bayes Language Engine 🌍

A custom-built, highly optimized Natural Language Processing (NLP) engine built in Java from scratch. It uses a custom implementation of the Naive Bayes classification algorithm to detect the language of a given text with high accuracy and confidence scoring.

## ✨ Key Features

- **Character Trigram Tokenization**: Splits text into overlapping 3-letter chunks (N-Grams) rather than whole words, making the engine highly resilient to typos and capable of detecting short, ambiguous words.
- **Softmax Confidence Scoring**: Converts raw logarithmic probabilities into human-readable confidence percentages (0% to 100%).
- **Memory Optimization (Dictionary Encoding)**: Maps multi-byte string tokens to primitive integer IDs, reducing RAM usage by up to 80% when training on massive datasets.
- **Laplace Smoothing & Prior Probabilities**: Prevents mathematical crashes on unseen words and uses historical distribution data to break ties.
- **Model Serialization**: Persists trained models to binary `.ser` files, allowing for instant loading without retraining.
- **Interactive CLI**: Features ANSI color-coded terminal output and a strict 25% "Gibberish Threshold" to filter out unsupported inputs.

## 📂 Project Architecture

Adheres to the Single Responsibility Principle (SRP) and standard Maven directory structure.

```text
src/main/java/io/github/rehansxcbom/nlp/
 ├── Main.java               # CLI entry point and UX routing
 ├── LanguageDetector.java   # Core Naive Bayes mathematical engine
 ├── TextTokenizer.java      # Regex, cleaning, and Trigram extraction
 ├── DataLoader.java         # External CSV I/O handling
 └── ModelSerializer.java    # Binary state persistence (.ser)
```

## 🚀 Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6+

### 1. Build the Application

This project uses the Maven Shade Plugin to compile into a standalone executable Fat JAR.

```bash
mvn clean package
```

### 2. Train the Model

You can train the engine using any CSV file formatted with a `Language,Text` schema.

```bash
java -jar target/language-detector-1.0-SNAPSHOT.jar /path/to/dataset.csv
```

_Note: Upon successful training, the engine will automatically save its state to `language_model.ser` in your root directory._

### 3. Load a Pre-Trained Model

Bypass the training phase and instantly launch the interactive CLI using a saved `.ser` file.

```bash
java -jar target/language-detector-1.0-SNAPSHOT.jar language_model.ser
```

## 📊 Dataset Format

The engine expects a standard CSV file with headers. The first column must be the label (Language) and the second column must be the raw text.

```csv
Language,Text
English,The quick brown fox jumps over the lazy dog.
Spanish,El zorro marrón rápido salta sobre el perro perezoso.
French,Le renard brun rapide saute par-dessus le chien.
```

## 🧪 Testing

The project includes a comprehensive JUnit 5 test suite verifying tokenization boundaries, Laplace smoothing, Softmax calculations, and edge cases.

```bash
mvn clean test
```
