import { Component, computed, inject, signal } from "@angular/core";
import {
  ReactiveFormsModule,
  FormControl,
  FormGroup,
  Validators,
} from "@angular/forms";
import { DestroyRef } from "@angular/core";
import { takeUntilDestroyed } from "@angular/core/rxjs-interop";

import { LoginService } from "./login.service";
import { LoginResponse } from "./login.model";

import { MatCardModule } from "@angular/material/card";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatInputModule } from "@angular/material/input";
import { MatButtonModule } from "@angular/material/button";

@Component({
  selector: "app-login",
  standalone: true,
  imports: [
    ReactiveFormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
  ],
  templateUrl: "./login.component.html",
})
export class LoginComponent {
  private readonly service = inject(LoginService);
  private readonly destroyRef = inject(DestroyRef);

  // =========================
  // FORM (Reactive Forms)
  // =========================
  readonly form = new FormGroup({
    username: new FormControl<string>("", {
      nonNullable: true,
      validators: [Validators.required],
    }),
    email: new FormControl<string>("", {
      nonNullable: true,
      validators: [Validators.required, Validators.email],
    }),
    password: new FormControl<string>("", {
      nonNullable: true,
      validators: [Validators.required],
    }),
    firstName: new FormControl<string>("", {
      nonNullable: true,
    }),
    lastName: new FormControl<string>("", {
      nonNullable: true,
    }),
  });

  // =========================
  // UI STATE
  // =========================
  readonly loading = signal(false);
  readonly error = signal<string | null>(null);
  readonly result = signal<LoginResponse | null>(null);

  // =========================
  // VIEW MODEL (optional but clean)
  // =========================
  readonly vm = computed(() => ({
    loading: this.loading(),
    error: this.error(),
    result: this.result(),
    formValid: this.form.valid,
  }));

  // =========================
  // ACTIONS
  // =========================
  login(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.loading.set(true);
    this.error.set(null);
    this.result.set(null);

    this.service
      .login(this.form.getRawValue())
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (res) => {
          this.result.set(res);
          this.loading.set(false);
        },
        error: (err) => {
          this.error.set(
            err?.error?.message ?? "Registration failed"
          );
          this.loading.set(false);
        },
      });
  }

  reset(): void {
    this.form.reset({
      username: "",
      password: "",
    });

    this.error.set(null);
    this.result.set(null);
  }
}
