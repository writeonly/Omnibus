import { useState } from "react";
import type { FormEvent } from "react";

import { bffApiClient } from "../../../core/api/bffApiClient";
import type { LoginUserResponse } from "../../../core/api/bffApiClient";
import { validateLogin } from "./login.schema";
import type { LoginFormData } from "./login.schema";

const initialForm: LoginFormData = {
  username: "",
  email: "",
  password: "",
  firstName: "",
  lastName: "",
};

export function LoginView() {
  const [form, setForm] = useState(initialForm);
  const [error, setError] = useState<string | null>(null);
  const [result, setResult] = useState<LoginUserResponse | null>(null);
  const [submitting, setSubmitting] = useState(false);

  async function submit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    setError(null);
    setResult(null);

    const validationError = validateLogin(form);
    if (validationError !== null) {
      setError(validationError);
      return;
    }

    setSubmitting(true);

    try {
      const response = await bffApiClient.loginUser({
        username: form.username.trim(),
        password: form.password,
      });

      setResult(response);
    } catch (exception) {
      setError(exception instanceof Error ? exception.message : "Login failed");
    } finally {
      setSubmitting(false);
    }
  }

  return (
    <section className="section-card">
      <form className="form" onSubmit={submit}>
        <article className="bid-card registration-card">
          <header className="card-header">
            <h1>Login player account</h1>
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
              <span>Password</span>
              <input
                value={form.password}
                onChange={(event) => setForm({ ...form, password: event.target.value })}
                autoComplete="new-password"
                type="password"
              />
            </label>

            {error !== null && <div className="result-panel error-panel">{error}</div>}

            {result !== null && (
              <details className="result-panel success-panel" open>
                <summary>{result.username} queued for auth provisioning</summary>
                <p>
                  User ID {result.userId}. Current status: {result.status}. The outbox relay will
                  publish the login event to the bus for Auth Service and Keycloak.
                </p>
              </details>
            )}
          </div>

          <footer className="card-actions">
            <button className="primary-action" type="submit" disabled={submitting}>
              {submitting ? "Logining..." : "Login"}
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

