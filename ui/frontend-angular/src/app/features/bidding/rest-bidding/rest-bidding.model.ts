export type System = 'POLISH_CLUB' | 'STANDARD_AMERICAN';

export interface BiddingFormState {
  northHand: string;
  southHand: string;
  bidding: string;
  system: System;
}
