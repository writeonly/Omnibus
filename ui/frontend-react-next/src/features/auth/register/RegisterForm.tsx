"use client";

import { useState } from "react";
import type { FormEvent } from "react";

import { api } from "~/trpc/react";
import { registerUserSchema } from "./register.schema";

const initialForm = {
  username: "",
  email: "",
  password: "",
  firstName: "",
  lastName: "",
};

export function RegisterForm() {
  const [form, setForm] = useState(initialForm);
  const [validationError, setValidationError] = useState<string | null>(null);
  const register = api.registration.register.useMutation();

  async function submit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    setValidationError(null);

    const parsed = registerUserSchema.safeParse(form);
    if (!parsed.success) {
      setValidationError(parsed.error.issues[0]?.message ?? "Check the form and try again");
      return;
    }

    await register.mutateAsync(parsed.data);
  }

  return (
    <form className="register-card" onSubmit={submit}>
      <div className="register-card__header">
        <p className="eyebrow">Omnibus identity</p>
        <h1>Create your account</h1>
        <p>
          This registration travels through React, tRPC, the API Gateway, Modulith,
          gRPC, User Service, PostgreSQL outbox, Kafka, Auth Server, and Keycloak.
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
          <span>Email</span>
          <input
            value={form.email}
            onChange={(event) => setForm({ ...form, email: event.target.value })}
            autoComplete="email"
            type="email"
          />
        </label>
        <label>
          <span>Password</span>
          <input
            value={form.password}
            onChange={(event) => setForm({ ...form, password: event.target.value })}
            autoComplete="new-password"
            type="password"
          />
        </label>
        <label>
          <span>First name</span>
          <input
            value={form.firstName}
            onChange={(event) => setForm({ ...form, firstName: event.target.value })}
            autoComplete="given-name"
          />
        </label>
        <label>
          <span>Last name</span>
          <input
            value={form.lastName}
            onChange={(event) => setForm({ ...form, lastName: event.target.value })}
            autoComplete="family-name"
          />
        </label>
      </div>

      {(validationError !== null || register.error !== null) && (
        <p className="register-message register-message--error">
          {validationError ?? register.error?.message ?? "Registration failed"}
        </p>
      )}

      {register.data && (
        <p className="register-message register-message--success">
          Account saved as {register.data.username}. Auth provisioning status:{" "}
          {register.data.status}.
        </p>
      )}

      <button className="register-button" type="submit" disabled={register.isPending}>
        {register.isPending ? "Creating account..." : "Start registration flow"}
      </button>
    </form>
  );
}
