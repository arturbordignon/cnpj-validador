<?php

declare(strict_types=1);

namespace LibCnpj;

final class CnpjValidator
{
    private const LENGTH = 14;
    private const BASE_LENGTH = 12;
    private const ASCII_ZERO = 48;
    private const ALL_ZEROS = '00000000000000';

    /**
     * Weights for the first check digit (positions 1..12).
     */
    private const WEIGHTS_FIRST = [5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2];

    /**
     * Weights for the second check digit (positions 1..13).
     */
    private const WEIGHTS_SECOND = [6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2];

    public static function isValid(string $value): bool
    {
        if (CnpjFormatter::containsOnlyAllowedCharacters($value) === false) {
            return false;
        }

        $strippedValue = CnpjFormatter::strip($value);

        if (strlen($strippedValue) !== self::LENGTH) {
            return false;
        }

        if ($strippedValue === self::ALL_ZEROS) {
            return false;
        }

        $base = substr($strippedValue, 0, self::BASE_LENGTH);
        $informedCheckDigits = substr($strippedValue, self::BASE_LENGTH, 2);

        if (self::checkDigitsAreNumeric($informedCheckDigits) === false) {
            return false;
        }

        $calculatedCheckDigits = self::calculateCheckDigits($base);

        if ($calculatedCheckDigits === null) {
            return false;
        }

        if ($calculatedCheckDigits !== $informedCheckDigits) {
            return false;
        }

        return true;
    }

    public static function calculateCheckDigits(string $base): ?string
    {
        if (strlen($base) !== self::BASE_LENGTH) {
            return null;
        }

        if (self::containsOnlyAlphanumericDigits($base) === false) {
            return null;
        }

        $firstDigit = self::calculateDigit($base, self::WEIGHTS_FIRST);

        $baseWithFirstDigit = $base . $firstDigit;
        $secondDigit = self::calculateDigit($baseWithFirstDigit, self::WEIGHTS_SECOND);

        return $firstDigit . $secondDigit;
    }

    private static function containsOnlyAlphanumericDigits(string $base): bool
    {
        $length = strlen($base);

        for ($index = 0; $index < $length; $index = $index + 1) {
            if (self::isAlphanumericDigit($base[$index]) === false) {
                return false;
            }
        }

        return true;
    }

    private static function checkDigitsAreNumeric(string $digits): bool
    {
        $length = strlen($digits);

        for ($index = 0; $index < $length; $index = $index + 1) {
            if (self::isNumericDigit($digits[$index]) === false) {
                return false;
            }
        }

        return true;
    }

    private static function isAlphanumericDigit(string $character): bool
    {
        if ($character >= '0' && $character <= '9') {
            return true;
        }

        if ($character >= 'A' && $character <= 'Z') {
            return true;
        }

        return false;
    }

    private static function isNumericDigit(string $character): bool
    {
        if ($character >= '0' && $character <= '9') {
            return true;
        }

        return false;
    }

    /**
     * @param array<int, int> $weights
     */
    private static function calculateDigit(string $value, array $weights): string
    {
        $sum = 0;
        $length = strlen($value);

        for ($index = 0; $index < $length; $index = $index + 1) {
            $digitValue = ord($value[$index]) - self::ASCII_ZERO;
            $sum = $sum + ($digitValue * $weights[$index]);
        }

        $remainder = $sum % 11;

        if ($remainder < 2) {
            return '0';
        }

        return (string) (11 - $remainder);
    }
}
