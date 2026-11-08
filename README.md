# Naive Bayes Language Detector

A raw Java implementation of a Naive Bayes classifier used for language detection.

This repository contains a custom, from-scratch implementation of a Naive Bayes text classifier in Java, configured as a Language Detector. Built entirely without external machine learning libraries, this project demonstrates core natural language processing (NLP) and probability concepts under the hood.

Naive Bayes outperforms heavier, more complex neural networks for short texts, mostly because languages give themselves away so easily through high-frequency "stop words" (like the, el, le, der).

To build this, we first set up our classifier's "memory." The engine needs to remember four specific things during the training phase to be able to do the math later:

1. documentCountPerLanguage: To calculate our baseline probability (e.g., if 60% of our training data is English, we want to know that).

2. wordCountsPerLanguage: The core of the engine. If we see the word "bonjour", we need to look up how many times it appeared in French vs. Spanish.

3. totalWordsPerLanguage: The denominator for our math. We need the total count of all words processed for a specific language.

4. vocabulary: A master list of every unique word we've ever seen across all languages. We need the total size of this for Laplace smoothing later.

We are going to need a train(String text, String language) method that takes a sentence, chops it up into words, and updates all these maps.

To handle punctuation and lowercasing in a language detector, we have to be careful with our regular expressions.

We do not just strip out everything that isn't standard a-z, we will accidentally delete characters like é, ñ, or ü — which are massive, dead-giveaway clues for languages like French, Spanish, or German.

Instead, we use the Unicode regex [^\p{L}\s]. '\p{L}' matches a letter from any language, and '\s' matches whitespace. We will tell Java to replace anything that isn't those two things with an empty string.

A raw Java implementation of a Naive Bayes classifier used for language detection.

Key Features:
Zero Dependencies: Built using only standard Java libraries.
Mathematical Stability: Uses logarithmic probabilities to prevent floating-point underflow.
Laplace Smoothing: Gracefully handles unknown words encountered during testing.
Unicode Tokenization: Accurately processes language-specific characters (like é, ñ, ü) using Regex \p{L}.
