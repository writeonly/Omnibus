import { useState } from "react";
import type { FormEvent } from "react";

import { bffApiClient } from "../../../core/api/bffApiClient";
import type { RegisterUserResponse } from "../../../core/api/bffApiClient";
import { validateRegistration } from "./register.schema";
import type { RegisterFormData } from "./register.schema";

const initialForm: RegisterFormData = {
  username: "",
  email: "",
  password: "",
  firstName: "",
  lastName: "",
};

export function RegisterView() {
  const [form, setForm] = useState(initialForm);
  const [error, setError] = useState<string | null>(null);
  const [result, setResult] = useState<RegisterUserResponse | null>(null);
  const [submitting, setSubmitting] = useState(false);

  async function submit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    setError(null);
    setResult(null);

    const validationError = validateRegistration(form);
    if (validationError !== null) {
      setError(validationError);
      return;
    }

    setSubmitting(true);

    try {
      const response = await bffApiClient.registerUser({
        username: form.username.trim(),
        email: form.email.trim(),
        password: form.password,
        firstName: form.firstName.trim() || undefined,
        lastName: form.lastName.trim() || undefined,
      });

      setResult(response);
    } catch (exception) {
      setError(exception instanceof Error ? exception.message : "Registration failed");
    } finally {
      setSubmitting(false);
    }
  }

  return (
    <section className="section-card">
      <form className="form" onSubmit={submit}>
        <article className="bid-card registration-card">
          <header className="card-header">
            <span className="flow-label">React HTTP to Keycloak</span>
            <h1>Register player account</h1>
            <p>
              Client → React Vite → Nest BFF → API Gateway → Modulith → gRPC User Service →
              PostgreSQL outbox → bus → Auth Service function → Keycloak.
            </p>
          </header>

          <div className="form-layout registration-layout">
            <label className="field">
              <span>Username</span>
              <input
                value={form.username}
                onChange={(event) => setForm({ ...form, username: event.target.value })}
                autoComplete="username"
              />
            </label>

            <label className="field">
              <span>Email</span>
              <input
                value={form.email}
                onChange={(event) => setForm({ ...form, email: event.target.value })}
                autoComplete="email"
                type="email"
              />
            </label>

            <label className="field">
              <span>Password</span>
              <input
                value={form.password}
                onChange={(event) => setForm({ ...form, password: event.target.value })}
                autoComplete="new-password"
                type="password"
              />
            </label>

            <label className="field">
              <span>First name</span>
              <input
                value={form.firstName}
                onChange={(event) => setForm({ ...form, firstName: event.target.value })}
                autoComplete="given-name"
              />
            </label>

            <label className="field">
              <span>Last name</span>
              <input
                value={form.lastName}
                onChange={(event) => setForm({ ...form, lastName: event.target.value })}
                autoComplete="family-name"
              />
            </label>

            {error !== null && <div className="result-panel error-panel">{error}</div>}

            {result !== null && (
              <details className="result-panel success-panel" open>
                <summary>{result.username} queued for auth provisioning</summary>
                <p>
                  User ID {result.userId}. Current status: {result.status}. The outbox relay will
                  publish the registration event to the bus for Auth Service and Keycloak.
                </p>
              </details>
            )}
          </div>

          <footer className="card-actions">
            <button className="primary-action" type="submit" disabled={submitting}>
              {submitting ? "Registering..." : "Register through BFF"}
            </button>
            <button
              className="secondary-action"
              type="button"
              onClick={() => {
                setForm(initialForm);
                setError(null);
                setResult(null);
              }}
            >
              Reset
            </button>
          </footer>
        </article>
      </form>
    </section>
  );
}

