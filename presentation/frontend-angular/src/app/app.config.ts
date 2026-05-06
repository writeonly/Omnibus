import { ApplicationConfig, provideAppInitializer } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';

import { routes } from './app.routes';
import { KeycloakService } from 'keycloak-angular';

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),
    provideHttpClient(withInterceptorsFromDi()),

    KeycloakService,

    provideAppInitializer(() => {
      const keycloak = new KeycloakService(); // 🔥 KLUCZOWE

      return keycloak.init({
        config: {
          url: 'http://localhost:8181',
          realm: 'omnibus',
          clientId: 'omnibus-frontend',
        },
        initOptions: {
          onLoad: 'login-required',
          pkceMethod: 'S256',
          checkLoginIframe: false,
        },
      });
    }),
  ],
};
