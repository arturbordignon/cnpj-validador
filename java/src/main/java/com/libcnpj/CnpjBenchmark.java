package com.libcnpj;

public final class CnpjBenchmark {

    private CnpjBenchmark() {
    }

    public static void main(String[] args) {
        int iterations = 100_000;

        runBenchmark("numeric", "11.222.333/0001-81", iterations);
        runBenchmark("alphanumeric", "12.ABC.345/01DE-35", iterations);
    }

    private static void runBenchmark(String label, String value, int iterations) {
        long start = System.nanoTime();

        for (int index = 0; index < iterations; index = index + 1) {
            Cnpj.isValid(value);
        }

        long end = System.nanoTime();
        double milliseconds = (end - start) / 1_000_000.0;
        long operationsPerSecond = Math.round(iterations / (milliseconds / 1000));

        System.out.printf(
            "Java: %s - %d validations in %.2f ms (~%d ops/sec)%n",
            label,
            iterations,
            milliseconds,
            operationsPerSecond
        );
    }
}
