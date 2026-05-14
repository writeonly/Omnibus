import { Injectable } from '@nestjs/common';

export interface KeycloakClientConfig {
  url: string;
  realm: string;
  clientId: string;
}

@Injectable()
export class AuthService {
  private readonly publicKeycloakUrl = withoutTrailingSlash(
    process.env.KEYCLOAK_PUBLIC_URL ?? process.env.KEYCLOAK_URL ?? 'http://localhost:8180'
  );
  private readonly realm = process.env.KEYCLOAK_REALM ?? 'omnibus';
  private readonly clientId = process.env.KEYCLOAK_CLIENT_ID ?? 'omnibus-frontend';

  getClientConfig(): KeycloakClientConfig {
    return {
      url: this.publicKeycloakUrl,
      realm: this.realm,
      clientId: this.clientId,
    };
  }

  buildLoginUrl(redirectUri: string): string {
    return this.buildOpenIdUrl('auth', {
      client_id: this.clientId,
      redirect_uri: redirectUri,
      response_type: 'code',
      scope: 'openid profile email',
    });
  }

  buildRegistrationUrl(redirectUri: string): string {
    return this.buildOpenIdUrl('registrations', {
      client_id: this.clientId,
      redirect_uri: redirectUri,
      response_type: 'code',
      scope: 'openid profile email',
    });
  }

  buildLogoutUrl(redirectUri: string): string {
    return this.buildOpenIdUrl('logout', {
      client_id: this.clientId,
      post_logout_redirect_uri: redirectUri,
    });
  }

  getMe(user: any) {
    return {
      id: user.userId,
      username: user.username,
      email: user.email,
      roles: user.roles,
    };
  }

  private buildOpenIdUrl(endpoint: string, params: Record<string, string>): string {
    const query = new URLSearchParams(params);
    return `${this.publicKeycloakUrl}/realms/${this.realm}/protocol/openid-connect/${endpoint}?${query}`;
  }
}

function withoutTrailingSlash(value: string): string {
  return value.endsWith('/') ? value.slice(0, -1) : value;
}
