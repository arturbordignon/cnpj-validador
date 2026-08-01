import { describe, it } from 'node:test';
import assert from 'node:assert/strict';
import { readFileSync } from 'node:fs';
import { fileURLToPath } from 'node:url';
import { dirname, join } from 'node:path';
import { isValid, format, strip, isFormatted, calculateCheckDigits } from '../src/cnpj.js';

const __dirname = dirname(fileURLToPath(import.meta.url));
const fixturePath = join(__dirname, '..', '..', 'fixtures', 'test-vectors.json');
const fixture = JSON.parse(readFileSync(fixturePath, 'utf8'));

describe('CNPJ validation from shared fixtures', function () {
  for (const item of fixture.valid) {
    it(`accepts ${item.description}: ${item.value}`, function () {
      assert.equal(isValid(item.value), true);
    });
  }

  for (const item of fixture.invalid) {
    it(`rejects ${item.description}: ${item.value}`, function () {
      assert.equal(isValid(item.value), false);
    });
  }
});

describe('CNPJ formatting from shared fixtures', function () {
  for (const item of fixture.formatCases) {
    it(`formats ${item.raw}`, function () {
      assert.equal(format(item.raw), item.formatted);
    });
  }
});

describe('CNPJ check digit calculation from shared fixtures', function () {
  for (const item of fixture.checkDigits) {
    it(`calculates digits for ${item.base}`, function () {
      assert.equal(calculateCheckDigits(item.base), item.digits);
    });
  }
});

describe('CNPJ formatting helpers', function () {
  it('strips mask characters', function () {
    assert.equal(strip('11.222.333/0001-81'), '11222333000181');
    assert.equal(strip('12.ABC.345/01DE-35'), '12ABC34501DE35');
  });

  it('detects formatted values', function () {
    assert.equal(isFormatted('11.222.333/0001-81'), true);
    assert.equal(isFormatted('12.ABC.345/01DE-35'), true);
    assert.equal(isFormatted('11222333000181'), false);
    assert.equal(isFormatted('11.222.333/0001-8'), false);
  });

  it('returns original value when length is wrong', function () {
    assert.equal(format('1122233'), '1122233');
  });
});

describe('CNPJ check digit invalid input', function () {
  it('returns null for invalid base length', function () {
    assert.equal(calculateCheckDigits('11222333000'), null);
  });

  it('returns null for invalid base characters', function () {
    assert.equal(calculateCheckDigits('11222333000a'), null);
  });

  it('returns null for empty base', function () {
    assert.equal(calculateCheckDigits(''), null);
  });
});
