import { Injectable } from '@nestjs/common';

import { GrpcTransport } from './grpc-transport';
import { concat, decodeFields, encodeStringField, stringField } from './protobuf';

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
export class UserGrpcClient {
  private readonly target = process.env.API_GATEWAY_GRPC_TARGET ?? 'http://localhost:8080';

  constructor(private readonly transport: GrpcTransport) {}

  async registerUser(input: RegisterUserInput): Promise<RegisterUserResult> {
    const payload = concat([
      encodeStringField(1, input.username),
      encodeStringField(2, input.email),
      encodeStringField(3, input.password),
      encodeStringField(4, input.firstName),
      encodeStringField(5, input.lastName),
    ]);

    const response = await this.transport.unary(
      this.target,
      '/omnibus.v1.UserService/RegisterUser',
      payload
    );
    const fields = decodeFields(response);

    return {
      userId: stringField(fields, 1),
      username: stringField(fields, 2),
      email: stringField(fields, 3),
      status: stringField(fields, 4),
    };
  }
}
