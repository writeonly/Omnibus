export * from "./auth.service";
import { AuthService } from "./auth.service";
export * from "./health.service";
import { HealthService } from "./health.service";
export * from "./nextBid.service";
import { NextBidService } from "./nextBid.service";
export * from "./restBidding.service";
import { RestBiddingService } from "./restBidding.service";
export const APIS = [
  AuthService,
  HealthService,
  NextBidService,
  RestBiddingService,
];
