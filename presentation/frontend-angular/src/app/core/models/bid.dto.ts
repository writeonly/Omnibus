export interface BidRecommendRequest {
  hand: string;
  bidding?: string;
  seat: 'NORTH' | 'SOUTH' | 'EAST' | 'WEST';
  system?: string;
}

export interface BidRecommendResponse {
  recommendedBid: string;
  explanation: string;

  candidates?: {
    bid: string;
    reason: string;
  }[];
  system: string;
  bidding: string;
  evaluatedSeat: string;
  southHand: string;
  northHand: string;
  bid: string;
}

