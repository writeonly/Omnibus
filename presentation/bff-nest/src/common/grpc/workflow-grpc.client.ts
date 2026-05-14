import { Injectable } from '@nestjs/common';

import { GrpcTransport } from './grpc-transport';
import { concat, encodeStringField } from './protobuf';

@Injectable()
export class WorkflowGrpcClient {
  private readonly target = process.env.API_GATEWAY_GRPC_TARGET ?? 'http://localhost:8080';

  constructor(private readonly transport: GrpcTransport) {}

  async startRulePublication(input: {
    name: string;
    content: string;
    requestedBy: string;
  }): Promise<void> {
    const payload = concat([
      encodeStringField(1, input.name),
      encodeStringField(2, input.content),
      encodeStringField(3, input.requestedBy),
    ]);

    await this.transport.unary(
      this.target,
      '/omnibus.v1.WorkflowService/StartRulePublication',
      payload
    );
  }
}
