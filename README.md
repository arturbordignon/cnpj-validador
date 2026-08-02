# lib-cnpj

Validação e formatação rápida de CNPJ para PHP, JavaScript e Java, sem dependências.

Suporta tanto o CNPJ **numérico** tradicional quanto o novo CNPJ **alfanumérico**
introduzido pela Instrução Normativa RFB nº 2.229/2024 e detalhado na
**Nota Técnica Conjunta 2025.001**. O formato alfanumérico coexiste com o
numérico e **não** substitui os CNPJs já existentes.

> English version: [README_EN.md](README_EN.md)

![CI](https://github.com/lib-cnpj/cnpj-validator/actions/workflows/ci.yml/badge.svg)
[![License: MIT](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
[![Packagist](https://img.shields.io/packagist/v/lib-cnpj/php)](https://packagist.org/packages/lib-cnpj/php)
[![npm](https://img.shields.io/npm/v/@lib-cnpj/js)](https://www.npmjs.com/package/@lib-cnpj/js)
[![Maven Central](https://img.shields.io/maven-central/v/com.libcnpj/lib-cnpj)](https://search.maven.org/artifact/com.libcnpj/lib-cnpj)

## Usando apenas uma linguagem

O repositório é um monorepo, mas você pode usar apenas a pasta da linguagem que
precisa. Para PHP, copie ou publique apenas o conteúdo da pasta `php/`; as
pastas `js/` e `java/` podem ser ignoradas.

Em um fluxo de publicação real, cada linguagem vira um pacote separado:

- PHP: `composer require lib-cnpj/php`
- JavaScript: `npm install @lib-cnpj/js`
- Java: `com.libcnpj:lib-cnpj:1.0.0`

Cada pacote tem **zero dependências em tempo de execução** e ocupa poucos
quilobytes.

## Funcionalidades

- Valida CNPJs numéricos legados e os novos CNPJs alfanuméricos.
- Formata e remove a máscara do CNPJ (`XX.XXX.XXX/XXXX-XX`).
- Calcula os dígitos verificadores a partir dos 12 caracteres base.
- Validação pura: sem chamadas de rede ou APIs externas.
- Implementação leve e de baixa latência.
- Código limpo sem operadores ternários (`?:`, `??`, `?->`).

## Formato

O CNPJ possui 14 posições:

| Posições | Conteúdo | Exemplo |
|----------|----------|---------|
| 1–8      | Raiz (alfanumérica no novo formato) | `12ABC345` |
| 9–12     | Ordem do estabelecimento | `01DE` |
| 13–14    | Dígitos verificadores numéricos | `35` |

Máscara de exibição: `XX.XXX.XXX/XXXX-XX`

## Algoritmo

O algoritmo oficial da SERPRO utiliza módulo 11 com pesos que variam de 2 a 9
da direita para a esquerda:

- Pesos do primeiro dígito verificador: `5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2`
- Pesos do segundo dígito verificador: `6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2`

Cada caractere é convertido para um valor numérico subtraindo 48 do seu código
ASCII (`'0'..'9'` → 0..9, `'A'..'Z'` → 17..42).

Se o resto da divisão da soma ponderada por 11 for menor que 2, o dígito é 0;
caso contrário, é `11 - resto`.

## PHP

### Instalação

```bash
composer require lib-cnpj/php
```

### Uso

Você só precisa carregar o autoloader do Composer e usar a classe `Cnpj`.
Não é necessário importar `CnpjValidator` ou `CnpjFormatter` manualmente.

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

### Uso sem Composer

Se o projeto não usa Composer, gere o arquivo único em `php/dist/libcnpj.php`:

```bash
cd php
php build.php
```

Depois inclua apenas esse arquivo:

```php
<?php
require 'caminho/para/php/dist/libcnpj.php';

var_dump(Cnpj::isValid('11.222.333/0001-81')); // bool(true)
```

## JavaScript

### Instalação

```bash
npm install @lib-cnpj/js
```

### Uso

```js
import { isValid, format, strip, calculateCheckDigits } from '@lib-cnpj/js';

console.log(isValid('11.222.333/0001-81')); // true
console.log(isValid('12.ABC.345/01DE-35')); // true
console.log(isValid('00000000000000'));     // false

console.log(format('11222333000181'));        // 11.222.333/0001-81
console.log(strip('11.222.333/0001-81'));     // 11222333000181
console.log(calculateCheckDigits('12ABC34501DE')); // '35'
```

### Uso com jQuery / navegadores antigos

Gere o build UMD localmente:

```bash
cd js
npm install
npm run build
```

Depois inclua o arquivo gerado com uma tag `<script>`:

```html
<script src="js/dist/cnpj.umd.js"></script>
<script>
  if (LibCnpj.isValid('12.ABC.345/01DE-35')) {
    // válido
  }

  document.getElementById('cnpj').value = LibCnpj.format('11222333000181');
</script>
```

Ou use diretamente de um CDN após publicar no npm:

```html
<script src="https://cdn.jsdelivr.net/npm/@lib-cnpj/js@latest/dist/cnpj.umd.js"></script>
```

Com jQuery:

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

### Instalação

Com Maven:

```xml
<dependency>
    <groupId>com.libcnpj</groupId>
    <artifactId>lib-cnpj</artifactId>
    <version>1.0.0</version>
</dependency>
```

Com Gradle:

```groovy
implementation 'com.libcnpj:lib-cnpj:1.0.0'
```

### Uso

```java
import com.libcnpj.Cnpj;

public class Exemplo {
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

## Testes

```bash
# PHP
cd php
composer install
composer check    # lint + análise estática + testes

# JavaScript
cd js
npm install
npm run check     # lint + testes + build

# Java
cd java
mvn test          # checkstyle roda automaticamente na fase validate
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

Os três conjuntos de testes carregam os mesmos casos do arquivo
`fixtures/test-vectors.json`, garantindo consistência entre as linguagens.

## Compatibilidade

### PHP

- Requer PHP **7.4 ou superior**.
- Zero dependências em tempo de execução.
- Funciona em projetos legados desde que estejam no PHP 7.4+.
- Projetos em PHP 5.6 precisam ser atualizados para usar esta biblioteca.

### JavaScript

- O código-fonte usa ES modules e é testado no Node **18+**.
- O build UMD (`dist/cnpj.umd.js`) funciona em navegadores antigos e com
  jQuery sem necessidade de bundler.
- O build CommonJS (`dist/cnpj.cjs`) funciona com `require()` em projetos
  Node antigos.

### Java

- Requer Java **8 ou superior**.
- Zero dependências em tempo de execução.

## Guia de integração para projetos grandes

### 1. Armazene o CNPJ como texto sem máscara

Use `VARCHAR(14)` (ou equivalente) e sempre armazene o valor sem máscara.
Colunas numéricas não suportam letras, então migre-as antes do formato
alfanumérico entrar em produção.

### 2. Valide em toda fronteira

- **Frontend (JS):** valide no evento `blur` e antes do envio; formate apenas
  para exibição.
- **Backend (PHP/Java):** valide nos controllers, camadas de caso de uso e
  antes da persistência.
- **Relatórios / exportações:** formate somente na camada de apresentação.

### 3. Normalize antes de comparar

Sempre compare os valores sem máscara. Dois CNPJs são iguais quando seus 14
caracteres sem máscura coincidem:

```
"11.222.333/0001-81" == "11222333000181"
```

### 4. Mantenha as três implementações sincronizadas

Fixe a mesma versão semântica para PHP, JavaScript e Java. Para garantia
adicional, mantenha o arquivo compartilhado `fixtures/test-vectors.json` e
carregue-o em cada suíte de testes, assim todas as linguagens validam os
mesmos casos.

### 5. Máscaras de entrada

Use a máscara `XX.XXX.XXX/XXXX-XX` e aceite letras maiúsculas e dígitos nas
12 primeiras posições. Entradas minúsculas devem ser rejeitadas ou convertidas
para maiúsculas antes da validação.

### 6. Migração

Os CNPJs numéricos existentes continuam válidos. Apenas amplie as colunas do
banco de dados e atualize as expressões regulares; não revalide ou regenere
registros existentes.

## Sobre estados brasileiros

A validação do CNPJ é federal e uniforme em todo o Brasil. Não existem regras
específicas por estado no cálculo dos dígitos verificadores.

## Licença

MIT © lib-cnpj contributors
