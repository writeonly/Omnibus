import { Injectable } from '@nestjs/common';

@Injectable()
export class AuthService {
  getMe(user: any) {
    return {
      id: user.userId,
      username: user.username,
      email: user.email,
      roles: user.roles,
    };
  }
}
