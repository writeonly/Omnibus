import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react";
import type { RestBiddingResponse } from "../../../core/api/bffApiClient";

export interface RestBiddingRequest {
  northHand: string;
  southHand: string;
  bidding: string;
  system: "POLISH_CLUB" | "STANDARD_AMERICAN";
}

export const restBiddingApi = createApi({
  reducerPath: "restBiddingApi",
  baseQuery: fetchBaseQuery({
    baseUrl: "http://localhost:3000"
  }),
  endpoints: (builder) => ({
    recommendBidding: builder.mutation<
      RestBiddingResponse,
      RestBiddingRequest
    >({
      query: (body) => ({
        url: "/bidding/rest-bid",
        method: "POST",
        body
      })
    })
  })
});

export const { useRecommendBiddingMutation } = restBiddingApi;