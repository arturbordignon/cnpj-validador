<?php

declare(strict_types=1);

namespace LibCnpj\Tests;

use LibCnpj\Cnpj;
use PHPUnit\Framework\TestCase;

final class CnpjTest extends TestCase
{
    private const FIXTURE_PATH = __DIR__ . '/../../fixtures/test-vectors.json';

    /**
     * @return array<string, array{0: string}>
     */
    public function validCnpjProvider(): array
    {
        $cases = [];

        foreach ($this->loadFixture()['valid'] as $item) {
            $cases[$item['description']] = [$item['value']];
        }

        return $cases;
    }

    /**
     * @return array<string, array{0: string}>
     */
    public function invalidCnpjProvider(): array
    {
        $cases = [];

        foreach ($this->loadFixture()['invalid'] as $item) {
            $cases[$item['description']] = [$item['value']];
        }

        return $cases;
    }

    /**
     * @return array<string, array{0: string, 1: string}>
     */
    public function checkDigitProvider(): array
    {
        $cases = [];

        foreach ($this->loadFixture()['checkDigits'] as $item) {
            $cases[$item['base']] = [$item['base'], $item['digits']];
        }

        return $cases;
    }

    /**
     * @return array<string, array{0: string, 1: string}>
     */
    public function formatProvider(): array
    {
        $cases = [];

        foreach ($this->loadFixture()['formatCases'] as $item) {
            $cases[$item['raw']] = [$item['raw'], $item['formatted']];
        }

        return $cases;
    }

    /**
     * @dataProvider validCnpjProvider
     */
    public function testValidCnpj(string $value): void
    {
        $this->assertTrue(Cnpj::isValid($value));
    }

    /**
     * @dataProvider invalidCnpjProvider
     */
    public function testInvalidCnpj(string $value): void
    {
        $this->assertFalse(Cnpj::isValid($value));
    }

    /**
     * @dataProvider checkDigitProvider
     */
    public function testCalculateCheckDigits(string $base, string $expectedDigits): void
    {
        $this->assertSame($expectedDigits, Cnpj::calculateCheckDigits($base));
    }

    /**
     * @dataProvider formatProvider
     */
    public function testFormat(string $raw, string $expected): void
    {
        $this->assertSame($expected, Cnpj::format($raw));
    }

    public function testStrip(): void
    {
        $this->assertSame('11222333000181', Cnpj::strip('11.222.333/0001-81'));
        $this->assertSame('12ABC34501DE35', Cnpj::strip('12.ABC.345/01DE-35'));
    }

    public function testIsFormatted(): void
    {
        $this->assertTrue(Cnpj::isFormatted('11.222.333/0001-81'));
        $this->assertTrue(Cnpj::isFormatted('12.ABC.345/01DE-35'));
        $this->assertFalse(Cnpj::isFormatted('11222333000181'));
        $this->assertFalse(Cnpj::isFormatted('11.222.333/0001-8'));
    }

    public function testFormatReturnsOriginalWhenLengthIsWrong(): void
    {
        $this->assertSame('1122233', Cnpj::format('1122233'));
    }

    public function testCalculateCheckDigitsReturnsNullForInvalidBase(): void
    {
        $this->assertNull(Cnpj::calculateCheckDigits('11222333000'));
        $this->assertNull(Cnpj::calculateCheckDigits('11222333000a'));
        $this->assertNull(Cnpj::calculateCheckDigits(''));
    }

    /**
     * @return array<string, mixed>
     */
    private function loadFixture(): array
    {
        $json = file_get_contents(self::FIXTURE_PATH);

        if ($json === false) {
            throw new \RuntimeException('Could not load test fixture');
        }

        $data = json_decode($json, true);

        if (is_array($data) === false) {
            throw new \RuntimeException('Invalid test fixture');
        }

        return $data;
    }
}
