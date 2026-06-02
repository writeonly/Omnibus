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
    NgIf,
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

  register() {
    this.dialog.open(RegisterDialogComponent);
  }

  logout() {
    this.auth.logout();
  }

  onRouteChange(route: string) {
    this.router.navigateByUrl(route);
  }
}

@Component({
  selector: "app-register-dialog",
  standalone: true,
  imports: [
    FormsModule,
    MatButtonModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    NgIf,
    MatProgressSpinnerModule,
  ],
  styles: [
    `
      .register-dialog {
        display: grid;
        gap: 12px;
        min-width: min(420px, 80vw);
      }

      .register-message {
        margin: 0;
        font-size: 0.9rem;
      }
    `,
  ],
  template: `
    <h2 mat-dialog-title>Create account</h2>
    <div mat-dialog-content class="register-dialog">
      <mat-form-field appearance="outline">
        <mat-label>Username</mat-label>
        <input matInput [(ngModel)]="form.username" autocomplete="username" />
      </mat-form-field>
      <mat-form-field appearance="outline">
        <mat-label>Email</mat-label>
        <input
          matInput
          type="email"
          [(ngModel)]="form.email"
          autocomplete="email"
        />
      </mat-form-field>
      <mat-form-field appearance="outline">
        <mat-label>Password</mat-label>
        <input
          matInput
          type="password"
          [(ngModel)]="form.password"
          autocomplete="new-password"
        />
      </mat-form-field>
      <mat-form-field appearance="outline">
        <mat-label>First name</mat-label>
        <input
          matInput
          [(ngModel)]="form.firstName"
          autocomplete="given-name"
        />
      </mat-form-field>
      <mat-form-field appearance="outline">
        <mat-label>Last name</mat-label>
        <input
          matInput
          [(ngModel)]="form.lastName"
          autocomplete="family-name"
        />
      </mat-form-field>
      <p *ngIf="message" class="register-message">{{ message }}</p>
    </div>
    <div mat-dialog-actions align="end">
      <button mat-button mat-dialog-close [disabled]="submitting">
        Cancel
      </button>
      <button
        mat-raised-button
        color="primary"
        (click)="submit()"
        [disabled]="submitting"
      >
        <mat-spinner *ngIf="submitting" diameter="18"></mat-spinner>
        <span *ngIf="!submitting">Create</span>
      </button>
    </div>
  `,
})
export class RegisterDialogComponent {
  private readonly auth = inject(AuthService);

  form = {
    username: "",
    email: "",
    password: "",
    firstName: "",
    lastName: "",
  };
  submitting = false;
  message = "";

  submit() {
    this.submitting = true;
    this.message = "";

    this.auth.register(this.form).subscribe({
      next: (result) => {
        this.submitting = false;
        this.message = `Account ${result.username} accepted. You can log in once provisioning finishes.`;
      },
      error: (error) => {
        this.submitting = false;
        this.message = error?.error?.message ?? "Registration failed";
      },
    });
  }
}
