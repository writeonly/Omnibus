import { ApplicationConfig, APP_INITIALIZER } from '@angular/core';
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
