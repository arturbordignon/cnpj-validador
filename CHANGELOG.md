# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.0.0] - 2026-08-01

### Added

- PHP implementation with validation, formatting and check-digit calculation.
- JavaScript implementation with ESM, CJS and UMD builds.
- Java implementation with Maven and JUnit 5.
- Support for legacy numeric CNPJ and new alphanumeric CNPJ
  (Instrução Normativa RFB nº 2.229/2024 / Nota Técnica Conjunta 2025.001).
- Shared test fixtures in `fixtures/test-vectors.json` for cross-language
  consistency.
- GitHub Actions CI matrix for PHP 7.4–8.3, Node 18/20/22 and Java 8/11/17/21.
- Static analysis and linting: PHPStan, PHP-CS-Fixer, ESLint and Checkstyle.
- Bilingual README in Portuguese and English.
- MIT license.
