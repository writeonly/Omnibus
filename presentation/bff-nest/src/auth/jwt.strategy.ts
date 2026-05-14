import { Injectable } from '@nestjs/common';
import { PassportStrategy } from '@nestjs/passport';
import { ExtractJwt, Strategy } from 'passport-jwt';
import * as jwksRsa from 'jwks-rsa';

@Injectable()
export class JwtStrategy extends PassportStrategy(Strategy) {
  constructor() {
    const publicKeycloakUrl = withoutTrailingSlash(
      process.env.KEYCLOAK_PUBLIC_URL ?? process.env.KEYCLOAK_URL ?? 'http://localhost:8180'
    );
    const internalKeycloakUrl = withoutTrailingSlash(
      process.env.KEYCLOAK_INTERNAL_URL ?? process.env.KEYCLOAK_URL ?? publicKeycloakUrl
    );
    const realm = process.env.KEYCLOAK_REALM ?? 'omnibus';

    super({
      secretOrKeyProvider: jwksRsa.passportJwtSecret({
        cache: true,
        rateLimit: true,
        jwksRequestsPerMinute: 5,
        jwksUri: `${internalKeycloakUrl}/realms/${realm}/protocol/openid-connect/certs`,
      }),

      jwtFromRequest: ExtractJwt.fromAuthHeaderAsBearerToken(),
      audience: process.env.KEYCLOAK_CLIENT_ID ?? 'omnibus-frontend',
      issuer: `${publicKeycloakUrl}/realms/${realm}`,
      algorithms: ['RS256'],
    });
  }

  validate(payload: any) {
    return {
      userId: payload.sub,
      username: payload.preferred_username,
      email: payload.email,
      roles: payload.realm_access?.roles || [],
    };
  }
}

function withoutTrailingSlash(value: string): string {
  return value.endsWith('/') ? value.slice(0, -1) : value;
}
