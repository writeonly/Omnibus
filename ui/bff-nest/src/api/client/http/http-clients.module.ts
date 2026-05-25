import { Global, Module } from '@nestjs/common';

import { HttpTransport } from './http-transport';
import { UserHttpClient } from './user-http.client';
import { WorkflowHttpClient } from './workflow-http.client';
import { BiddingHttpClient } from './bidding-http.client';

@Global()
@Module({
  providers: [
    HttpTransport,
    UserHttpClient,
    WorkflowHttpClient,
    BiddingHttpClient,
  ],
  exports: [
    UserHttpClient,
    WorkflowHttpClient,
    BiddingHttpClient,
  ],
})
export class HttpClientsModule {}
