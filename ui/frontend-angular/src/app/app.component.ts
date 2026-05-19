import { Component, inject, OnInit } from '@angular/core';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatCardModule } from '@angular/material/card';
import { ThemeService } from './core/theme/theme.service';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { NavigationEnd, Router, RouterOutlet } from '@angular/router';
import { filter } from 'rxjs';
import { MatButtonToggleModule } from '@angular/material/button-toggle';
import { KeycloakService } from 'keycloak-angular';
import { NgIf } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { AuthService } from './core/auth/auth.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    MatToolbarModule,
    MatCardModule,
    MatSlideToggleModule,
    RouterOutlet,
    MatButtonToggleModule,
    NgIf,
    MatButtonModule
  ],
  templateUrl: './app.component.html'
})
export class AppComponent implements OnInit {
  readonly theme = inject(ThemeService);
  private readonly router = inject(Router);
  private readonly keycloak = inject(KeycloakService);
  private readonly auth = inject(AuthService);

  activeRoute = '';

  isLoggedIn = false;
  username?: string;

  constructor() {
    this.activeRoute = this.router.url;

    this.router.events
      .pipe(filter(event => event instanceof NavigationEnd))
      .subscribe((event: any) => {
        this.activeRoute = event.urlAfterRedirects;
      });
  }

  async ngOnInit() {
    await this.refreshAuthState();
  }

  async refreshAuthState() {
    this.isLoggedIn = await this.keycloak.isLoggedIn();

    if (this.isLoggedIn) {
      try {
        const profile = await this.keycloak.loadUserProfile();
        this.username = profile.username;
      } catch (e) {
        console.warn('Profile load failed', e);
      }
    } else {
      this.username = undefined;
    }
  }

  login() {
    this.auth.login();
  }

  register() {
    this.auth.register();
  }

  logout() {
    this.auth.logout();
  }

  onRouteChange(route: string) {
    this.router.navigateByUrl(route);
  }
}
