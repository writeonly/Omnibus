import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react";
import type { NextBidResponse } from "../../../core/api/bffApiClient";

export interface NextBidRequest {
  hand: string;
  bidding: string;
  system: "POLISH_CLUB" | "STANDARD_AMERICAN";
}

export const nextBidApi = createApi({
  reducerPath: "nextBidApi",
  baseQuery: fetchBaseQuery({
    baseUrl: "http://localhost:3000"
  }),
  endpoints: (builder) => ({
    recommendBid: builder.mutation<NextBidResponse, NextBidRequest>({
      query: (body) => ({
        url: "/bidding/next-bid",
        method: "POST",
        body
      })
    })
  })
});

export const { useRecommendBidMutation } = nextBidApi;
