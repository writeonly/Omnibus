export interface BidRecommendRequest {
  hand: string;
  bidding?: string;
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

