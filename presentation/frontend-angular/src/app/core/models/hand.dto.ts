export interface HandAnalyzeRequest {
  hand: string; // np. "AKQJ.T987.32.AKQ"
}

export interface HandAnalyzeResponse {
  hcp: number;
  shape: string;

  controls?: number;
}
