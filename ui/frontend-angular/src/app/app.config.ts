import { ApplicationConfig, APP_INITIALIZER } from '@angular/core';
import { provideRouter } from '@angular/router';
import { routes } from './app.routes';

import { ConfigService } from '@core/ConfigService';
import { KeycloakService } from 'keycloak-angular';

export function initApp(config: ConfigService, keycloak: KeycloakService) {
  return async () => {
    await config.load();

    return keycloak.init({
      config: config.config.keycloak,
      initOptions: {
        onLoad: 'check-sso',
        pkceMethod: 'S256',
        checkLoginIframe: false,
      },
    });
  };
}

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),

    ConfigService,
    KeycloakService,

    {
      provide: APP_INITIALIZER,
      multi: true,
      useFactory: initApp,
      deps: [ConfigService, KeycloakService],
    },
  ],
};