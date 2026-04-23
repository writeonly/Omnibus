import { Injectable, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import Keycloak from 'keycloak-js';
import { firstValueFrom } from 'rxjs';

type AuthConfig = {
  url: string;
  realm: string;
  clientId: string;
};

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private readonly httpClient = inject(HttpClient);
  private keycloak?: Keycloak;
  private refreshHandle?: number;

  readonly ready = signal(false);
  readonly authenticated = signal(false);
  readonly isAdmin = signal(false);
  readonly username = signal('');

  async init(): Promise<void> {
    if (this.ready()) {
      return;
    }

    const config = await firstValueFrom(
      this.httpClient.get<AuthConfig>('http://localhost:3000/api/auth/config'),
    );

    this.keycloak = new Keycloak({
      url: config.url,
      realm: config.realm,
      clientId: config.clientId,
    });

    const authenticated = await this.keycloak.init({
      pkceMethod: 'S256',
      checkLoginIframe: false,
    });

    this.applyState(authenticated);

    if (authenticated) {
      this.startRefreshLoop();
    }
  }

  login(): void {
    void this.keycloak?.login({
      redirectUri: window.location.origin,
    });
  }

  logout(): void {
    window.clearInterval(this.refreshHandle);
    void this.keycloak?.logout({
      redirectUri: window.location.origin,
    });
  }

  authorizationHeader(): string | undefined {
    return this.keycloak?.token ? `Bearer ${this.keycloak.token}` : undefined;
  }

  private applyState(authenticated: boolean): void {
    const roles = this.keycloak?.realmAccess?.roles ?? [];
    const preferredUsername = this.keycloak?.tokenParsed?.['preferred_username'];
    this.authenticated.set(authenticated);
    this.isAdmin.set(roles.includes('admin'));
    this.username.set(typeof preferredUsername === 'string' ? preferredUsername : '');
    this.ready.set(true);
  }

  private startRefreshLoop(): void {
    this.refreshHandle = window.setInterval(() => {
      void this.keycloak?.updateToken(30).then((authenticated: boolean) => {
        this.applyState(authenticated);
      });
    }, 20000);
  }
}
