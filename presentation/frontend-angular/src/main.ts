import { provideZoneChangeDetection } from "@angular/core";
import 'zone.js';
import { bootstrapApplication } from '@angular/platform-browser';
import { provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';
import { AppComponent } from './app/app.component';
import { routes } from "app/app.routes";
import { provideRouter } from "@angular/router";

bootstrapApplication(AppComponent, {
  providers: [
    provideZoneChangeDetection(), provideHttpClient(
      withInterceptorsFromDi()
    ),
    provideRouter(routes)
  ],
}).catch((error) => console.error('[Bootstrap Error]', error));
