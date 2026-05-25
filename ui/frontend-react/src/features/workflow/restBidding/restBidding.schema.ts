import { z } from "zod";
import type { System } from "../../../core/api/bffApiClient";

export const restBiddingSchema = z.object({
  northHand: z.string().min(1, "North hand is required"),
  southHand: z.string().min(1, "South hand is required"),
  bidding: z.string(),
  system: z.enum(["POLISH_CLUB", "STANDARD_AMERICAN"])
});

export type RestBiddingForm = z.infer<typeof restBiddingSchema>;
