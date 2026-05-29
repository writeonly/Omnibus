import { z } from "zod";

export const registerUserSchema = z.object({
  username: z.string().trim().min(3, "Username must contain at least 3 characters"),
  email: z.string().trim().email("Enter a valid email address"),
  password: z.string().min(8, "Password must contain at least 8 characters"),
  firstName: z.string().trim().optional(),
  lastName: z.string().trim().optional(),
});

export type RegisterUserInput = z.infer<typeof registerUserSchema>;

