import { z } from "zod";

export const nextBidSchema = z.object({
  hand: z.string().min(1, "Hand is required"),
  bidding: z.string(),
  system: z.enum(["POLISH_CLUB", "STANDARD_AMERICAN"])
});

export type NextBidForm = z.infer<typeof nextBidSchema>;
