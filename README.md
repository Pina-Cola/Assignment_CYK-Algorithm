# CYK Algorithm – Java Implementation

This repository contains a Java implementation of the **Cocke–Younger–Kasami (CYK)** algorithm for parsing strings using context-free grammars in **Chomsky Normal Form (CNF)**.

It was developed as part of an **Efficient Algorithms** course at Umeå University.

## Supports three parsing approaches
  - Naive recursive parser
  - Top-down parser with memoization
  - Bottom-up parser using dynamic programming


## Repository Contents

| File          | Description                                     |
|---------------|-------------------------------------------------|
| `Main.java`   | Entry point; parses input arguments             |
| `Grammar.java`| Loads grammar rules into internal structures    |
| `Parser.java` | Contains all parser implementations             |

## How to Run

Compile the code, then run from command line with grammar rules and input string:

```
java Main "SSS" "SLA" "SLR" "ASR" "L(" "R)" "(())"
```

This example checks if the input string `(())` is accepted by the specified grammar.


## Associated Report

The full report regarding this implementation is available here:

[Efficient Algorithms Report – Runtime Analysis of the Java Implementation of the CYK Algorithm](https://github.com/Pina-Cola/EfficientAlgorithms_Report)

---

👩‍💻 *Developed as part of academic coursework.* 
📘 Course: Efficient Algorithms – Umeå University
📅 March 2023
