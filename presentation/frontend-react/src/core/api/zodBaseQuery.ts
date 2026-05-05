import type { BaseQueryFn } from "@reduxjs/toolkit/query";
import { fetchBaseQuery } from "@reduxjs/toolkit/query";
import type { ZodSchema } from "zod";

type ZodMeta = {
  schema?: ZodSchema;
};

export const zodBaseQuery =
  (baseUrl: string): BaseQueryFn<any, unknown, unknown, ZodMeta> =>
  async (args, api, extraOptions) => {
    const baseQuery = fetchBaseQuery({ baseUrl });

    const { schema } = extraOptions?.meta || {};

    // 🧠 1. VALIDATION BEFORE REQUEST
    if (schema && args.body) {
      const result = schema.safeParse(args.body);

      if (!result.success) {
        return {
          error: {
            status: 400,
            data: result.error.flatten()
          }
        };
      }

      args.body = result.data;
    }

    // 🧠 2. NORMAL REQUEST
    return baseQuery(args, api, extraOptions);
  };
