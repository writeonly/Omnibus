import { Injectable } from "@angular/core";

import {
  nextBidControllerRecommend,
  restBiddingControllerRecommend,
  healthControllerHealth,

  NextBidRequestDto,
  RestBiddingRequestDto,

  healthControllerHealthResponse,
  nextBidControllerRecommendResponse,
  restBiddingControllerRecommendResponse,
} from '../../../generated/orval';

export type HealthResponse = healthControllerHealthResponse['data'];
export type NextBidResponse = nextBidControllerRecommendResponse['data'];
export type RestBiddingResponseDto = restBiddingControllerRecommendResponse['data']

@Injectable({ providedIn: 'root' })
export class OrvalBffApi {

  async health(): Promise<HealthResponse> {
    const res = await healthControllerHealth();
    return res.data;
  }

  async nextBid(dto: NextBidRequestDto): Promise<NextBidResponse> {
    const res = await nextBidControllerRecommend(dto);
    return res.data;
  }

  async restBidding(dto: RestBiddingRequestDto): Promise<RestBiddingResponseDto> {
    const res = await restBiddingControllerRecommend(dto);
    return res.data;
  }
}