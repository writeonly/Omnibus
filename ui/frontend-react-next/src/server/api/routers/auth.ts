import { TRPCError } from "@trpc/server";

import { env } from "~/env";
import { loginSchema, logoutSchema } from "~/features/auth/login/login.schema";
import { createTRPCRouter, publicProcedure } from "~/server/api/trpc";

type LoginResponse = {
  accessToken: string;
  refreshToken?: string | null;
  tokenType: string;
  expiresIn?: number | null;
};

export const authRouter = createTRPCRouter({
  login: publicProcedure.input(loginSchema).mutation(async ({ input }) => {
    const response = await fetch(`${env.AUTH_SERVER_URL}/auth/login`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(input),
    });

    if (!response.ok) {
      throw new TRPCError({
        code: response.status === 401 ? "UNAUTHORIZED" : "BAD_REQUEST",
        message: response.status === 401 ? "Invalid username or password" : await response.text(),
      });
    }

    return response.json() as Promise<LoginResponse>;
  }),

  logout: publicProcedure.input(logoutSchema).mutation(async ({ input }) => {
    const response = await fetch(`${env.AUTH_SERVER_URL}/auth/logout`, {
      method: "POST",
      headers: {
        Authorization: `Bearer ${input.accessToken}`,
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ refreshToken: input.refreshToken }),
    });

    if (!response.ok) {
      throw new TRPCError({
        code: "BAD_REQUEST",
        message: await response.text(),
      });
    }

    return response.json() as Promise<{ status: string }>;
  }),
});
