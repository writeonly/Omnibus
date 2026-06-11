import { Component, inject, OnInit } from "@angular/core";
import { MatToolbarModule } from "@angular/material/toolbar";
import { MatCardModule } from "@angular/material/card";
import { ThemeService } from "./core/theme/theme.service";
import { MatSlideToggleModule } from "@angular/material/slide-toggle";
import { NavigationEnd, Router, RouterOutlet } from "@angular/router";
import { filter } from "rxjs";
import { MatButtonToggleModule } from "@angular/material/button-toggle";
import { KeycloakService } from "keycloak-angular";
import { NgIf } from "@angular/common";
import { MatButtonModule } from "@angular/material/button";
import { AuthService } from "./core/auth/auth.service";
import { FormsModule } from "@angular/forms";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatInputModule } from "@angular/material/input";
import { MatDialog, MatDialogModule } from "@angular/material/dialog";
import { MatProgressSpinnerModule } from "@angular/material/progress-spinner";

@Component({
  selector: "app-root",
  standalone: true,
  imports: [
    MatToolbarModule,
    MatCardModule,
    MatSlideToggleModule,
    RouterOutlet,
    MatButtonToggleModule,
    MatButtonModule,
    FormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatDialogModule,
    MatProgressSpinnerModule,
  ],
  templateUrl: "./app.component.html",
})
export class AppComponent implements OnInit {
  readonly theme = inject(ThemeService);
  private readonly router = inject(Router);
  private readonly keycloak = inject(KeycloakService);
  private readonly auth = inject(AuthService);
  private readonly dialog = inject(MatDialog);

  activeRoute = "";

  isLoggedIn = false;
  username?: string;

  constructor() {
    this.activeRoute = this.router.url;

    this.router.events
      .pipe(filter((event) => event instanceof NavigationEnd))
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
        console.warn("Profile load failed", e);
      }
    } else {
      this.username = undefined;
    }
  }

  login() {
    this.auth.login();
  }

  logout() {
    this.auth.logout();
  }

  onRouteChange(route: string) {
    console.log('navigate to:', route);
    this.router.navigateByUrl(route);
  }
}
