import { configureStore } from "@reduxjs/toolkit";
import { nextBidApi } from "../features/bidding/nextBid/nextBidApi";

export const store = configureStore({
  reducer: {
    [nextBidApi.reducerPath]: nextBidApi.reducer
  },
  middleware: (getDefaultMiddleware) =>
    getDefaultMiddleware().concat(nextBidApi.middleware)
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;