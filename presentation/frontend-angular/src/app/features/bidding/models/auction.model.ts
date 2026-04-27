export type Seat = 'NORTH' | 'SOUTH' | 'EAST' | 'WEST';

export interface Bid {
  seat: Seat;
  call: string; // "1♠", "2NT", "PASS"
}

export interface Auction {
  dealer: Seat;
  vulnerability: 'NONE' | 'NS' | 'EW' | 'BOTH';

  bids: Bid[];
}
