import { Injectable } from '@nestjs/common';

import { HttpTransport } from './http-transport';

@Injectable()
export class WorkflowHttpClient {
  private readonly target =
    process.env.API_GATEWAY_HTTP_TARGET ??
    'http://localhost:8080';

  constructor(
    private readonly transport: HttpTransport
  ) {}

  async startRulePublication(input: {
    name: string;
    content: string;
    requestedBy: string;
  }): Promise<void> {
    await this.transport.post(
      this.target,
      '/api/workflows/rule-publication',
      input
    );
  }
}
