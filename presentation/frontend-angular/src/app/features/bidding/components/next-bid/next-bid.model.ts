export type System = 'POLISH_CLUB' | 'STANDARD_AMERICAN';

export interface BidFormState {
  hand: string;
  bidding: string;
  system: System;
}
