# Contributing

Thank you for considering a contribution to `lib-cnpj`.

## How to contribute

1. Fork the repository.
2. Create a feature branch (`git checkout -b feature/my-change`).
3. Make your changes.
4. Run the checks for the languages you changed (see below).
5. Open a pull request with a clear description.

## Development setup

### PHP

```bash
cd php
composer install
composer check
```

The `composer check` command runs lint, static analysis and tests.

### JavaScript

```bash
cd js
npm install
npm run check
```

The `npm run check` command runs lint, tests and build.

### Java

```bash
cd java
mvn test
```

Checkstyle runs automatically during the `validate` phase.

## Coding standards

- PHP: PSR-12 via PHP-CS-Fixer, PHPStan level 8.
- JavaScript: ESLint with the recommended rules.
- Java: Checkstyle with the project config in `java/config/checkstyle.xml`.
- No ternary operators (`?:`, `??`, `?->`). Use explicit `if/else`.
- Keep runtime dependencies at zero.
- Add tests for new behavior and update shared fixtures when applicable.

## Shared fixtures

When adding a new validation case, consider adding it to
`fixtures/test-vectors.json` so all three languages run the same test.

## Commit messages

Use [Conventional Commits](https://www.conventionalcommits.org/):

```text
feat: add something new
fix: correct a bug
docs: update README
test: add missing tests
refactor: improve code structure without changing behavior
```

## License

By contributing, you agree that your contributions will be licensed under the
MIT License.
