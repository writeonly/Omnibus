import { TRPCError } from "@trpc/server";

import { env } from "~/env";
import { createTRPCRouter, publicProcedure } from "~/server/api/trpc";
import { registerUserSchema } from "~/features/auth/register/register.schema";

type RegistrationResponse = {
  userId: string;
  username: string;
  email: string;
  status: string;
};

export const registrationRouter = createTRPCRouter({
  register: publicProcedure
    .input(registerUserSchema)
    .mutation(async ({ input }) => {
      const response = await fetch(`${env.API_GATEWAY_URL}/api/user/users/register`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(input),
      });

      if (!response.ok) {
        throw new TRPCError({
          code: response.status === 409 ? "CONFLICT" : "BAD_REQUEST",
          message: await response.text(),
        });
      }

      return response.json() as Promise<RegistrationResponse>;
    }),
});

