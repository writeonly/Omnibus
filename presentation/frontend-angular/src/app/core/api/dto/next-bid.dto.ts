export interface NextBidRequest {
  hand: string;
  bidding?: string;
  system?: string;
}

export interface NextBidResponse {
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

