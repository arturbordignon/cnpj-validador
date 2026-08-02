import { containsOnlyAllowedCharacters, strip } from './formatter.js';

const LENGTH = 14;
const BASE_LENGTH = 12;
const ASCII_ZERO = 48;
const ALL_ZEROS = '00000000000000';

const WEIGHTS_FIRST = [5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2];
const WEIGHTS_SECOND = [6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2];

export function isValid(value) {
  if (containsOnlyAllowedCharacters(value) === false) {
    return false;
  }

  const strippedValue = strip(value);

  if (strippedValue.length !== LENGTH) {
    return false;
  }

  if (strippedValue === ALL_ZEROS) {
    return false;
  }

  const base = strippedValue.slice(0, BASE_LENGTH);
  const informedCheckDigits = strippedValue.slice(BASE_LENGTH, LENGTH);

  if (checkDigitsAreNumeric(informedCheckDigits) === false) {
    return false;
  }

  const calculatedCheckDigits = calculateCheckDigits(base);

  if (calculatedCheckDigits === null) {
    return false;
  }

  if (calculatedCheckDigits !== informedCheckDigits) {
    return false;
  }

  return true;
}

export function calculateCheckDigits(base) {
  if (base.length !== BASE_LENGTH) {
    return null;
  }

  if (containsOnlyAlphanumericDigits(base) === false) {
    return null;
  }

  const firstDigit = calculateDigit(base, WEIGHTS_FIRST);
  const baseWithFirstDigit = base + firstDigit;
  const secondDigit = calculateDigit(baseWithFirstDigit, WEIGHTS_SECOND);

  return firstDigit + secondDigit;
}

function containsOnlyAlphanumericDigits(base) {
  for (let index = 0; index < base.length; index = index + 1) {
    if (isAlphanumericDigit(base[index]) === false) {
      return false;
    }
  }

  return true;
}

function checkDigitsAreNumeric(digits) {
  for (let index = 0; index < digits.length; index = index + 1) {
    if (isNumericDigit(digits[index]) === false) {
      return false;
    }
  }

  return true;
}

function isAlphanumericDigit(character) {
  if (character >= '0' && character <= '9') {
    return true;
  }

  if (character >= 'A' && character <= 'Z') {
    return true;
  }

  return false;
}

function isNumericDigit(character) {
  if (character >= '0' && character <= '9') {
    return true;
  }

  return false;
}

function calculateDigit(value, weights) {
  let sum = 0;

  for (let index = 0; index < value.length; index = index + 1) {
    const digitValue = value.charCodeAt(index) - ASCII_ZERO;
    sum = sum + (digitValue * weights[index]);
  }

  const remainder = sum % 11;

  if (remainder < 2) {
    return '0';
  }

  return String(11 - remainder);
}
