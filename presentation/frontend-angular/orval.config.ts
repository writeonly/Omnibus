export default {
  api: {
    input: 'http://localhost:3000/api-json',
    output: {
      target: './src/generated/orval.ts',
      client: 'fetch',
      clean: true,
    },
  },
};