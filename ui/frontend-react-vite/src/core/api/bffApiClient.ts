export type System = "POLISH_CLUB" | "STANDARD_AMERICAN";

export interface NextBidRequest {
  hand: string;
  bidding?: string;
  system?: System;
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

export interface RestBiddingRequest {
  northHand: string;
  southHand: string;
  bidding: string;
  system: System;
}

export interface RestBiddingResponse {
  summary: string;
  bestLine: string[];
  explanation: string;
  system: string;
  bidding: string;
}

export interface RegisterUserRequest {
  username: string;
  email: string;
  password: string;
  firstName?: string;
  secondName?: string;
  thirdName?: string;
  lastName?: string;
}

export interface RegisterUserResponse {
  userId: string;
  username: string;
  email: string;
  status: string;
}

const baseUrl = "/api";

async function postJson<TResponse, TPayload>(
  path: string,
  payload: TPayload,
): Promise<TResponse> {
  const response = await fetch(`${baseUrl}${path}`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(payload),
  });

  if (!response.ok) {
    throw new Error(`Request failed with status ${response.status}`);
  }

  return response.json() as Promise<TResponse>;
}

export const bffApiClient = {
  recommendBidding(payload: RestBiddingRequest): Promise<RestBiddingResponse> {
    return postJson("/workflow/rest-bidding", payload);
  },

  recommendBid(payload: NextBidRequest): Promise<NextBidResponse> {
    return postJson("/rule/next-bid", payload);
  },

  registerUser(payload: RegisterUserRequest): Promise<RegisterUserResponse> {
    return postJson("/user/register", payload);
  },
};
