<?php

declare(strict_types=1);

require __DIR__ . '/../vendor/autoload.php';

use LibCnpj\Cnpj;

function runBenchmark(string $label, string $value, int $iterations): void
{
    $start = hrtime(true);

    for ($index = 0; $index < $iterations; $index = $index + 1) {
        Cnpj::isValid($value);
    }

    $end = hrtime(true);
    $milliseconds = ($end - $start) / 1e6;
    $operationsPerSecond = (int) round($iterations / ($milliseconds / 1000));

    echo sprintf(
        "PHP: %s - %d validations in %.2f ms (~%d ops/sec)%s",
        $label,
        $iterations,
        $milliseconds,
        $operationsPerSecond,
        PHP_EOL
    );
}

$iterations = 100_000;

runBenchmark('numeric', '11.222.333/0001-81', $iterations);
runBenchmark('alphanumeric', '12.ABC.345/01DE-35', $iterations);
