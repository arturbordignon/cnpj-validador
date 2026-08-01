<?php

declare(strict_types=1);

namespace LibCnpj;

final class Cnpj
{
    public static function isValid(string $value): bool
    {
        return CnpjValidator::isValid($value);
    }

    public static function format(string $value): string
    {
        return CnpjFormatter::format($value);
    }

    public static function strip(string $value): string
    {
        return CnpjFormatter::strip($value);
    }

    public static function isFormatted(string $value): bool
    {
        return CnpjFormatter::isFormatted($value);
    }

    public static function calculateCheckDigits(string $base): ?string
    {
        return CnpjValidator::calculateCheckDigits($base);
    }
}
