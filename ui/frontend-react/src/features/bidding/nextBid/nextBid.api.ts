import { baseApi } from "../../../core/api/baseApi";
import { nextBidSchema } from "./nextBid.schema";
import type { NextBidResponse } from "../../../core/api/bffApiClient";

export const nextBidApi = baseApi.injectEndpoints({
  endpoints: (builder) => ({
    recommendBid: builder.mutation<
      NextBidResponse,
      typeof nextBidSchema._input
    >({
      query: (body) => ({
        url: "/bidding/next-bid",
        method: "POST",
        body
      }),

      extraOptions: {
        meta: {
          schema: nextBidSchema
        }
      }
    })
  })
});

export const { useRecommendBidMutation } = nextBidApi;
