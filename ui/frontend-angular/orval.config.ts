export default {
  api: {
    input: "http://localhost:3001/api-json",
    output: {
      target: "./src/generated/orval.ts",
      client: "fetch",
      clean: true,
    },
  },
};
