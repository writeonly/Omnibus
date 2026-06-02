import { LoginForm } from "~/features/auth/login/LoginForm";
import { RegisterForm } from "~/features/auth/register/RegisterForm";
import { HydrateClient } from "~/trpc/server";

export default async function Home() {
  return (
    <HydrateClient>
      <main className="register-page">
        <div className="register-hero">
          <p className="eyebrow">Client to Keycloak</p>
          <h2>Identity flows, wired end to end.</h2>
          <p>
            Registration takes the scenic route through the platform. Login is
            intentionally direct: React to tRPC, Next to Auth Service, Auth
            Service to Keycloak, JWT back to the client.
          </p>
        </div>
        <div className="auth-card-stack">
          <RegisterForm />
          <LoginForm />
        </div>
      </main>
    </HydrateClient>
  );
}
