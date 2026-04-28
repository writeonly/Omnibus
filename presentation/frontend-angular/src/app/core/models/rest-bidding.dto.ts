export interface RestBiddingRequest {
  northHand: string;
  southHand: string;
  bidding: string;
  system: string;
}

export interface RestBiddingResponse {
  summary: string;
  bestLine: string[];
  explanation: string;
  system: string;
  bidding: string;
}
