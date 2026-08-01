package com.libcnpj;

public final class CnpjValidator {

    private static final int LENGTH = 14;
    private static final int BASE_LENGTH = 12;
    private static final int ASCII_ZERO = 48;
    private static final String ALL_ZEROS = "00000000000000";

    private static final int[] WEIGHTS_FIRST = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
    private static final int[] WEIGHTS_SECOND = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

    private CnpjValidator() {
    }

    public static boolean isValid(String value) {
        if (CnpjFormatter.containsOnlyAllowedCharacters(value) == false) {
            return false;
        }

        String clean = CnpjFormatter.strip(value);

        if (clean.length() != LENGTH) {
            return false;
        }

        if (ALL_ZEROS.equals(clean)) {
            return false;
        }

        String base = clean.substring(0, BASE_LENGTH);
        String informedDigits = clean.substring(BASE_LENGTH, LENGTH);

        if (checkDigitsAreNumeric(informedDigits) == false) {
            return false;
        }

        String calculated = calculateCheckDigits(base);

        if (calculated == null) {
            return false;
        }

        return calculated.equals(informedDigits);
    }

    public static String calculateCheckDigits(String base) {
        if (base.length() != BASE_LENGTH) {
            return null;
        }

        if (baseIsValid(base) == false) {
            return null;
        }

        String firstDigit = calculateDigit(base, WEIGHTS_FIRST);
        String baseWithFirstDigit = base + firstDigit;
        String secondDigit = calculateDigit(baseWithFirstDigit, WEIGHTS_SECOND);

        return firstDigit + secondDigit;
    }

    private static boolean baseIsValid(String base) {
        for (int index = 0; index < base.length(); index = index + 1) {
            if (isAlphanumericDigit(base.charAt(index)) == false) {
                return false;
            }
        }

        return true;
    }

    private static boolean checkDigitsAreNumeric(String digits) {
        for (int index = 0; index < digits.length(); index = index + 1) {
            if (isNumericDigit(digits.charAt(index)) == false) {
                return false;
            }
        }

        return true;
    }

    private static boolean isAlphanumericDigit(char character) {
        if (character >= '0' && character <= '9') {
            return true;
        }

        if (character >= 'A' && character <= 'Z') {
            return true;
        }

        return false;
    }

    private static boolean isNumericDigit(char character) {
        if (character >= '0' && character <= '9') {
            return true;
        }

        return false;
    }

    private static String calculateDigit(String value, int[] weights) {
        int sum = 0;

        for (int index = 0; index < value.length(); index = index + 1) {
            int digitValue = value.charAt(index) - ASCII_ZERO;
            sum = sum + (digitValue * weights[index]);
        }

        int remainder = sum % 11;

        if (remainder < 2) {
            return "0";
        }

        return Integer.toString(11 - remainder);
    }
}
