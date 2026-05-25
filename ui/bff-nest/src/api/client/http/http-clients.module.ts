import { Global, Module } from '@nestjs/common';

import { HttpTransport } from './http-transport';
import { BiddingHttpClient } from './bidding-http.client';
import { WorkflowHttpClient } from './workflow-http.client';
import { UserHttpClient } from './user-http.client';

@Global()
@Module({
  providers: [
    HttpTransport,
    BiddingHttpClient,
    WorkflowHttpClient,
    UserHttpClient,
  ],
  exports: [
    BiddingHttpClient,
    WorkflowHttpClient,
    UserHttpClient,
  ],
})
export class HttpClientsModule {}
