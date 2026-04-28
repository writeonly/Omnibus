export interface BiddingRecommendRequest {
  northHand: string;
  southHand: string;
  bidding: string;
  system: string;
}

export interface BiddingRecommendResponse {
  summary: string;
  bestLine: string[];
  explanation: string;
  system: string;
  bidding: string;
}
