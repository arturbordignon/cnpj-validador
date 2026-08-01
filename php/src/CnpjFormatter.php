<?php

declare(strict_types=1);

namespace LibCnpj;

final class CnpjFormatter
{
    private const MASK_CHARACTERS = ['.', '/', '-'];
    private const FORMATTED_PATTERN = '/^[A-Z0-9]{2}\.[A-Z0-9]{3}\.[A-Z0-9]{3}\/[A-Z0-9]{4}-[0-9]{2}$/';
    private const ALLOWED_CHARACTERS_PATTERN = '/^[A-Z0-9.\/-]+$/';

    public static function strip(string $value): string
    {
        return str_replace(self::MASK_CHARACTERS, '', $value);
    }

    public static function format(string $value): string
    {
        $clean = self::strip($value);

        if (strlen($clean) !== 14) {
            return $value;
        }

        return sprintf(
            '%s.%s.%s/%s-%s',
            substr($clean, 0, 2),
            substr($clean, 2, 3),
            substr($clean, 5, 3),
            substr($clean, 8, 4),
            substr($clean, 12, 2)
        );
    }

    public static function isFormatted(string $value): bool
    {
        if (preg_match(self::FORMATTED_PATTERN, $value) !== 1) {
            return false;
        }

        return true;
    }

    public static function containsOnlyAllowedCharacters(string $value): bool
    {
        if (preg_match(self::ALLOWED_CHARACTERS_PATTERN, $value) !== 1) {
            return false;
        }

        return true;
    }
}
