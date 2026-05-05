import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react";
import type { NextBidResponse, System } from "../../../core/api/bffApiClient";

interface NextBidRequest {
  hand: string;
  bidding: string;
  system: System;
}

export const nextBidApi = createApi({
  reducerPath: "nextBidApi",
  baseQuery: fetchBaseQuery({
    baseUrl: "/api" // albo Twój BFF gateway
  }),
  endpoints: (builder) => ({
    recommendBid: builder.mutation<NextBidResponse, NextBidRequest>({
      query: (body) => ({
        url: "/recommend-bid",
        method: "POST",
        body
      })
    })
  })
});

export const { useRecommendBidMutation } = nextBidApi;
