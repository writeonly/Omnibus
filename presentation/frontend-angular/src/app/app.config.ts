import { ApplicationConfig, provideAppInitializer, inject } from '@angular/core';
import { KeycloakService } from 'keycloak-angular';

export const appConfig: ApplicationConfig = {
  providers: [
    KeycloakService,

    provideAppInitializer(() => {
      const keycloak = inject(KeycloakService); // 🔥 to musi być inject

      return keycloak.init({
        config: {
          url: 'http://localhost:8181',
          realm: 'omnibus',
          clientId: 'omnibus-frontend',
        },
        initOptions: {
          onLoad: 'check-sso',
          pkceMethod: 'S256',
          checkLoginIframe: false,
        },
      });
    }),
  ],
};
