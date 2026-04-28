export interface NextBidRequest {
  hand: string;
  bidding: string;
  seat: 'NORTH' | 'SOUTH' | 'EAST' | 'WEST';
  system?: string;
}

export interface NextBidResponse {
  recommendedBid: string;
  explanation: string;
  candidates?: {
    bid: string;
    reason: string;
  }[];
}
