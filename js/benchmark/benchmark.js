import { isValid } from '../src/cnpj.js';

function runBenchmark(label, value, iterations) {
  const start = performance.now();

  for (let index = 0; index < iterations; index = index + 1) {
    isValid(value);
  }

  const end = performance.now();
  const milliseconds = end - start;
  const operationsPerSecond = Math.round(iterations / (milliseconds / 1000));

  console.log(
    'JS: %s - %d validations in %d ms (~%d ops/sec)',
    label,
    iterations,
    milliseconds.toFixed(2),
    operationsPerSecond
  );
}

const iterations = 100_000;

runBenchmark('numeric', '11.222.333/0001-81', iterations);
runBenchmark('alphanumeric', '12.ABC.345/01DE-35', iterations);
