import { Injectable } from '@nestjs/common';

import { GrpcTransport } from './grpc-transport';
import {
  concat,
  decodeFields,
  encodeStringField,
  intField,
  messageFields,
  stringField,
} from './protobuf';

export interface BiddingRecommendationRequest {
  northHand: string;
  southHand: string;
  auction?: string;
  system: string;
}

export interface BiddingRecommendationResponse {
  recommendedBid: string;
  explanation: string;
  bidding: string;
  candidates: Array<{
    bid: string;
    priority: number;
    reason: string;
  }>;
}

@Injectable()
export class BiddingGrpcClient {
  private readonly target = process.env.API_GATEWAY_GRPC_TARGET ?? 'http://localhost:8080';

  constructor(private readonly transport: GrpcTransport) {}

  async recommendBid(
    request: BiddingRecommendationRequest
  ): Promise<BiddingRecommendationResponse> {
    const payload = concat([
      encodeStringField(1, request.northHand),
      encodeStringField(2, request.southHand),
      encodeStringField(3, request.auction ?? ''),
      encodeStringField(4, request.system),
    ]);

    const response = await this.transport.unary(
      this.target,
      '/omnibus.v1.BiddingService/RecommendBid',
      payload
    );
    const fields = decodeFields(response);

    return {
      bidding: stringField(fields, 5),
      recommendedBid: stringField(fields, 6),
      explanation: stringField(fields, 7),
      candidates: messageFields(fields, 8).map((candidate) => {
        const candidateFields = decodeFields(candidate);

        return {
          bid: stringField(candidateFields, 1),
          priority: intField(candidateFields, 2),
          reason: stringField(candidateFields, 3),
        };
      }),
    };
  }

  async saveManagedRule(name: string, content: string): Promise<void> {
    const payload = concat([encodeStringField(1, name), encodeStringField(2, content)]);

    await this.transport.unary(this.target, '/omnibus.v1.BiddingService/SaveManagedRule', payload);
  }
}
