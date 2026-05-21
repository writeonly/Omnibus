import { Global, Module } from '@nestjs/common';

import { GrpcTransport } from './grpc-transport';
import { BiddingGrpcClient } from './bidding-grpc.client';
import { WorkflowGrpcClient } from './workflow-grpc.client';
import { UserGrpcClient } from './user-grpc.client';

@Global()
@Module({
  providers: [GrpcTransport, BiddingGrpcClient, WorkflowGrpcClient, UserGrpcClient],
  exports: [BiddingGrpcClient, WorkflowGrpcClient, UserGrpcClient],
})
export class GrpcClientsModule {}
