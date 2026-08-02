# lib-cnpj

Fast, zero-dependency CNPJ validation and formatting for PHP, JavaScript and Java.

Supports both the legacy **numeric** CNPJ and the new **alphanumeric** CNPJ
introduced by Instrução Normativa RFB nº 2.229/2024 and detailed in
**Nota Técnica Conjunta 2025.001**. The alphanumeric format coexists with the
numeric one and does **not** replace existing CNPJs.

> Versão em português: [README.md](README.md)

![CI](https://github.com/lib-cnpj/cnpj-validator/actions/workflows/ci.yml/badge.svg)
[![License: MIT](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
[![Packagist](https://img.shields.io/packagist/v/lib-cnpj/php)](https://packagist.org/packages/lib-cnpj/php)
[![npm](https://img.shields.io/npm/v/@lib-cnpj/js)](https://www.npmjs.com/package/@lib-cnpj/js)
[![Maven Central](https://img.shields.io/maven-central/v/com.libcnpj/lib-cnpj)](https://search.maven.org/artifact/com.libcnpj/lib-cnpj)

## Using only one language

This repository is a monorepo, but you can use only the folder for your
language. For PHP, copy or publish only the `php/` folder; the `js/` and
`java/` folders can be ignored.

In a real publishing flow, each language becomes a separate package:

- PHP: `composer require lib-cnpj/php`
- JavaScript: `npm install @lib-cnpj/js`
- Java: `com.libcnpj:lib-cnpj:1.0.0`

Each package has **zero runtime dependencies** and occupies only a few
kilobytes.

## Features

- Validates legacy numeric and new alphanumeric CNPJs.
- Formats and strips CNPJ masks (`XX.XXX.XXX/XXXX-XX`).
- Calculates check digits from the 12-character base.
- Pure validation: no network calls, no external APIs.
- Low-latency, allocation-light implementation.
- Clean code without ternary operators (`?:`, `??`, `?->`).

## Format

The CNPJ has 14 positions:

| Positions | Content | Example |
|-----------|---------|---------|
| 1–8       | Root (alphanumeric in the new format) | `12ABC345` |
| 9–12      | Establishment order | `01DE` |
| 13–14     | Numeric check digits | `35` |

Display mask: `XX.XXX.XXX/XXXX-XX`

## Algorithm

The official SERPRO algorithm uses modulo 11 with weights cycling from 2 to 9
from right to left:

- First check digit weights: `5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2`
- Second check digit weights: `6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2`

Each character is converted to a numeric value by subtracting 48 from its ASCII
code (`'0'..'9'` → 0..9, `'A'..'Z'` → 17..42).

If the remainder of the weighted sum divided by 11 is less than 2, the digit is
0; otherwise it is `11 - remainder`.

## PHP

### Installation

```bash
composer require lib-cnpj/php
```

### Usage

```php
<?php
require 'vendor/autoload.php';

use LibCnpj\Cnpj;

var_dump(Cnpj::isValid('11.222.333/0001-81')); // bool(true)
var_dump(Cnpj::isValid('12.ABC.345/01DE-35')); // bool(true)
var_dump(Cnpj::isValid('00000000000000'));     // bool(false)

echo Cnpj::format('11222333000181');     // 11.222.333/0001-81
echo Cnpj::strip('11.222.333/0001-81');  // 11222333000181
echo Cnpj::calculateCheckDigits('12ABC34501DE'); // 35
```

## JavaScript

### Installation

```bash
npm install @lib-cnpj/js
```

### Usage

```js
import { isValid, format, strip, calculateCheckDigits } from '@lib-cnpj/js';

console.log(isValid('11.222.333/0001-81')); // true
console.log(isValid('12.ABC.345/01DE-35')); // true
console.log(isValid('00000000000000'));     // false

console.log(format('11222333000181'));        // 11.222.333/0001-81
console.log(strip('11.222.333/0001-81'));     // 11222333000181
console.log(calculateCheckDigits('12ABC34501DE')); // '35'
```

### Usage with jQuery / legacy browsers

Build the UMD file locally:

```bash
cd js
npm install
npm run build
```

Then include the generated file with a `<script>` tag:

```html
<script src="js/dist/cnpj.umd.js"></script>
<script>
  if (LibCnpj.isValid('12.ABC.345/01DE-35')) {
    // valid
  }

  document.getElementById('cnpj').value = LibCnpj.format('11222333000181');
</script>
```

Or load directly from a CDN after publishing to npm:

```html
<script src="https://cdn.jsdelivr.net/npm/@lib-cnpj/js@latest/dist/cnpj.umd.js"></script>
```

With jQuery:

```js
$('#cnpj').on('blur', function () {
  var value = $(this).val();

  if (LibCnpj.isValid(value)) {
    $(this).val(LibCnpj.format(value));
  } else {
    $(this).addClass('is-invalid');
  }
});
```

## Java

### Installation

With Maven:

```xml
<dependency>
    <groupId>com.libcnpj</groupId>
    <artifactId>lib-cnpj</artifactId>
    <version>1.0.0</version>
</dependency>
```

With Gradle:

```groovy
implementation 'com.libcnpj:lib-cnpj:1.0.0'
```

### Usage

```java
import com.libcnpj.Cnpj;

public class Example {
    public static void main(String[] args) {
        System.out.println(Cnpj.isValid("11.222.333/0001-81")); // true
        System.out.println(Cnpj.isValid("12.ABC.345/01DE-35")); // true
        System.out.println(Cnpj.isValid("00000000000000"));     // false

        System.out.println(Cnpj.format("11222333000181"));        // 11.222.333/0001-81
        System.out.println(Cnpj.strip("11.222.333/0001-81"));     // 11222333000181
        System.out.println(Cnpj.calculateCheckDigits("12ABC34501DE")); // 35
    }
}
```

## Testing

```bash
# PHP
cd php
composer install
composer check    # lint + static analysis + tests

# JavaScript
cd js
npm install
npm run check     # lint + tests + build

# Java
cd java
mvn test          # checkstyle runs automatically during validate
```

## Benchmark

```bash
# PHP
cd php
composer benchmark

# JavaScript
cd js
npm run benchmark

# Java
cd java
mvn compile exec:java
```

All three test suites load the same cases from `fixtures/test-vectors.json`,
ensuring cross-language consistency.

## Compatibility

### PHP

- Requires PHP **7.4 or later**.
- Zero runtime dependencies.
- Works in legacy projects as long as they run PHP 7.4+.
- Projects on PHP 5.6 must be upgraded to use this library.

### JavaScript

- Source uses ES modules and is tested on Node **18+**.
- The UMD build (`dist/cnpj.umd.js`) works in old browsers and with jQuery
  without any bundler.
- The CommonJS build (`dist/cnpj.cjs`) works with `require()` in older Node
  projects.

### Java

- Requires Java **8 or later**.
- Zero runtime dependencies.

## Integration guide for large projects

### 1. Store CNPJ as stripped text

Use `VARCHAR(14)` (or equivalent) and always store the stripped value. Numeric
columns do not support letters, so migrate them before the alphanumeric format
enters production.

### 2. Validate at every boundary

- **Frontend (JS):** validate on blur and before submit; format for display.
- **Backend (PHP/Java):** validate in controllers, use-case layers and before
  persistence.
- **Reports / exports:** format only at presentation time.

### 3. Normalize before comparing

Always compare stripped values. Two CNPJs are equal when their 14 stripped
characters match:

```
"11.222.333/0001-81" == "11222333000181"
```

### 4. Keep the three implementations in sync

Pin the same semantic version across PHP, JavaScript and Java. For extra
guarantee, maintain a shared `fixtures/test-vectors.json` file and load it in
each test suite so all languages validate the same cases.

### 5. Input masks

Use the mask `XX.XXX.XXX/XXXX-XX` and accept uppercase letters and digits in the
first 12 positions. Lowercase input should be rejected or upper-cased before
validation.

### 6. Migration

Existing numeric CNPJs remain valid. Only widen database columns and update
regular expressions; do not revalidate or regenerate existing records.

## About Brazilian states

CNPJ validation is federal and uniform across Brazil. There are no state-specific
rules in the check-digit calculation.

## License

MIT © lib-cnpj contributors
