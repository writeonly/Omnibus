"use client";

import { useState } from "react";
import type { FormEvent } from "react";

import { api } from "~/trpc/react";
import { loginSchema } from "./login.schema";

const initialForm = {
  username: "",
  password: "",
};

function previewToken(token: string): string {
  if (token.length <= 36) return token;
  return `${token.slice(0, 18)}...${token.slice(-14)}`;
}

export function LoginForm() {
  const [form, setForm] = useState(initialForm);
  const [validationError, setValidationError] = useState<string | null>(null);
  const [session, setSession] = useState<{
    accessToken: string;
    refreshToken: string;
    tokenType: string;
  } | null>(null);
  const login = api.auth.login.useMutation();
  const logout = api.auth.logout.useMutation({
    onSuccess: () => {
      setSession(null);
    },
  });

  async function submit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    setValidationError(null);

    const parsed = loginSchema.safeParse(form);
    if (!parsed.success) {
      setValidationError(parsed.error.issues[0]?.message ?? "Check the form and try again");
      return;
    }

    const response = await login.mutateAsync(parsed.data);
    setSession({
      accessToken: response.accessToken,
      refreshToken: response.refreshToken ?? "",
      tokenType: response.tokenType,
    });
  }

  async function submitLogout() {
    if (session === null || session.refreshToken.length === 0) return;

    await logout.mutateAsync({
      accessToken: session.accessToken,
      refreshToken: session.refreshToken,
    });
  }

  return (
    <form className="register-card auth-card--login" onSubmit={submit}>
      <div className="register-card__header">
        <p className="eyebrow">Keycloak JWT</p>
        <h1>Sign in</h1>
        <p>
          Login stays lean: React calls tRPC, Next calls Auth Service over HTTP,
          Auth Service asks Keycloak for tokens, and the JWT returns to the client.
        </p>
      </div>

      <div className="register-grid">
        <label>
          <span>Username</span>
          <input
            value={form.username}
            onChange={(event) => setForm({ ...form, username: event.target.value })}
            autoComplete="username"
          />
        </label>
        <label>
          <span>Password</span>
          <input
            value={form.password}
            onChange={(event) => setForm({ ...form, password: event.target.value })}
            autoComplete="current-password"
            type="password"
          />
        </label>
      </div>

      {(validationError !== null || login.error !== null) && (
        <p className="register-message register-message--error">
          {validationError ?? login.error?.message ?? "Login failed"}
        </p>
      )}

      {session !== null && (
        <div className="register-message register-message--success token-preview">
          <span>{session.tokenType} token received</span>
          <code>{previewToken(session.accessToken)}</code>
        </div>
      )}

      {logout.error !== null && (
        <p className="register-message register-message--error">
          {logout.error.message}
        </p>
      )}

      <button className="register-button login-button" type="submit" disabled={login.isPending}>
        {login.isPending ? "Signing in..." : "Login and get JWT"}
      </button>

      <button
        className="register-button logout-button"
        type="button"
        disabled={session === null || logout.isPending}
        onClick={submitLogout}
      >
        {logout.isPending ? "Logging out..." : "Logout, revoke and blacklist"}
      </button>
    </form>
  );
}
