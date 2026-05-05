import { Global, Module } from '@nestjs/common';

import { GrpcTransport } from './grpc-transport';
import { BiddingGrpcClient } from './bidding-grpc.client';
import { WorkflowGrpcClient } from './workflow-grpc.client';

@Global()
@Module({
  providers: [GrpcTransport, BiddingGrpcClient, WorkflowGrpcClient],
  exports: [BiddingGrpcClient, WorkflowGrpcClient],
})
export class GrpcClientsModule {}
