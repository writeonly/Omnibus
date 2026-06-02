import { Injectable } from '@nestjs/common';

import { HttpTransport } from './http-transport';

export interface RegisterUserInput {
  username: string;
  email: string;
  password: string;
  firstName?: string;
  lastName?: string;
}

export interface RegisterUserResult {
  userId: string;
  username: string;
  email: string;
  status: string;
}

@Injectable()
export class UserHttpClient {
  private readonly target = process.env.API_GATEWAY_HTTP_TARGET ?? 'http://localhost:8080';

  constructor(private readonly transport: HttpTransport) {}

  async registerUser(input: RegisterUserInput): Promise<RegisterUserResult> {
    return this.transport.post(this.target, '/api/users/register', input);
  }
}
