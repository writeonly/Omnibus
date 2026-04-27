export interface AuctionAnalyzeRequest {
  northHand: string;
  southHand: string;
  auction: string;
  system: string;
}

export interface AuctionAnalyzeResponse {
  summary: string;
  bestLine: string[];
  explanation: string;
  system: string;
  auction: string;
}
