import js from '@eslint/js';

export default [
  {
    ignores: ['dist/**']
  },
  js.configs.recommended,
  {
    files: ['src/**/*.js', 'tests/**/*.js', 'benchmark/**/*.js'],
    languageOptions: {
      ecmaVersion: 2022,
      sourceType: 'module',
      globals: {
        console: 'readonly',
        process: 'readonly',
        performance: 'readonly'
      }
    },
    rules: {
      'no-unused-vars': ['error', { argsIgnorePattern: '^_' }],
      'prefer-const': 'error',
      'no-var': 'error',
      'eqeqeq': ['error', 'always'],
      'curly': ['error', 'all'],
      'no-console': 'off'
    }
  }
];
