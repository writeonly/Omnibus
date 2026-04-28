export interface BidRecommendRequest {
  hand: string;
  auction?: string;
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
  auction: string;
  evaluatedSeat: string;
  southHand: string;
  northHand: string;
  bid: string;
}

