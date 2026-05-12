import { Injectable } from "@angular/core";

declare const window: any;

@Injectable({ providedIn: 'root' })
export class ConfigService {

  config = {
    keycloak: {
      url: 'http://localhost:8180',
      realm: 'omnibus',
      clientId: 'omnibus-frontend'
    }
  };

  async load() {
    try {
      const env = window.__env;

      if (env?.KEYCLOAK_URL) {
        this.config = {
          keycloak: {
            url: env.KEYCLOAK_URL,
            realm: env.KEYCLOAK_REALM,
            clientId: env.KEYCLOAK_CLIENT
          }
        };
      } else {
        console.warn('Using default local Keycloak config');
      }
    } catch (e) {
      console.warn('Config load failed, using defaults', e);
    }
  }
}
