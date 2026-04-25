import {
  ForbiddenException,
  Injectable,
  UnauthorizedException,
} from '@nestjs/common';
import { createRemoteJWKSet, decodeJwt, jwtVerify } from 'jose';

type KeycloakConfig = {
  url: string;
  realm: string;
  clientId: string;
};

type AuthContext = {
  username: string;
  subject: string;
};

@Injectable()
export class AuthService {
  private readonly keycloakUrl = process.env.KEYCLOAK_URL ?? 'http://localhost:9090';
  private readonly keycloakInternalUrl =
    process.env.KEYCLOAK_INTERNAL_URL ?? this.keycloakUrl;
  private readonly realm = process.env.KEYCLOAK_REALM ?? 'omnibus';
  private readonly clientId = process.env.KEYCLOAK_CLIENT_ID ?? 'omnibus-frontend';
  private readonly jwks = createRemoteJWKSet(
    new URL(
      `${this.keycloakInternalUrl}/realms/${this.realm}/protocol/openid-connect/certs`,
    ),
  );

  getConfig(): KeycloakConfig {
    return {
      url: this.keycloakUrl,
      realm: this.realm,
      clientId: this.clientId,
    };
  }

  async requireAdmin(authorizationHeader?: string): Promise<void> {
    await this.requireAdminContext(authorizationHeader);
  }

  async requireAdminContext(authorizationHeader?: string): Promise<AuthContext> {
    const token = this.extractBearerToken(authorizationHeader);

    try {
      await jwtVerify(token, this.jwks, {
        typ: 'JWT',
      });
    } catch (error) {
      throw new UnauthorizedException('Invalid or expired access token');
    }

    const payload = decodeJwt(token) as {
      sub?: string;
      preferred_username?: string;
      realm_access?: { roles?: string[] };
    };

    const roles = payload.realm_access?.roles ?? [];
    if (!roles.includes('admin')) {
      throw new ForbiddenException('Administrator role is required');
    }

    return {
      username: payload.preferred_username ?? payload.sub ?? 'admin',
      subject: payload.sub ?? 'unknown',
    };
  }

  private extractBearerToken(authorizationHeader?: string): string {
    if (!authorizationHeader?.startsWith('Bearer ')) {
      throw new UnauthorizedException('Missing bearer token');
    }

    return authorizationHeader.slice('Bearer '.length).trim();
  }
}
