const MASK_CHARACTERS = /[./-]/g;
const FORMATTED_PATTERN = /^[A-Z0-9]{2}\.[A-Z0-9]{3}\.[A-Z0-9]{3}\/[A-Z0-9]{4}-[0-9]{2}$/;
const ALLOWED_CHARACTERS_PATTERN = /^[A-Z0-9./-]+$/;

export function strip(value) {
  return value.replace(MASK_CHARACTERS, '');
}

export function format(value) {
  const strippedValue = strip(value);

  if (strippedValue.length !== 14) {
    return value;
  }

  return (
    strippedValue.slice(0, 2) +
    '.' +
    strippedValue.slice(2, 5) +
    '.' +
    strippedValue.slice(5, 8) +
    '/' +
    strippedValue.slice(8, 12) +
    '-' +
    strippedValue.slice(12, 14)
  );
}

export function isFormatted(value) {
  return FORMATTED_PATTERN.test(value);
}

export function containsOnlyAllowedCharacters(value) {
  return ALLOWED_CHARACTERS_PATTERN.test(value);
}
