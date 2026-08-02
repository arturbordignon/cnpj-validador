export default {
  input: 'src/cnpj.js',
  output: [
    {
      file: 'dist/cnpj.cjs',
      format: 'cjs',
      exports: 'named'
    },
    {
      file: 'dist/cnpj.esm.js',
      format: 'esm'
    },
    {
      file: 'dist/cnpj.umd.js',
      format: 'umd',
      name: 'LibCnpj',
      exports: 'named'
    }
  ]
};
