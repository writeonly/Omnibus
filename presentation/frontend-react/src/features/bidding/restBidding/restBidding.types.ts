import type { System } from "../../../core/api/bffApiClient";

export interface RestBiddingForm {
  northHand: string;
  southHand: string;
  bidding: string;
  system: System;
}
