import { RegisterForm } from "~/features/auth/register/RegisterForm";
import { HydrateClient } from "~/trpc/server";

export default async function Home() {
  return (
    <HydrateClient>
      <main className="register-page">
        <div className="register-hero">
          <p className="eyebrow">Client to Keycloak</p>
          <h2>One registration, many little handshakes.</h2>
          <p>
            The UI only talks to tRPC. Everything behind it keeps its boundary:
            HTTP at the gateway, gRPC into User Service, outbox for durable
            publishing, and Spring Cloud Function for the Keycloak side effect.
          </p>
        </div>
        <RegisterForm />
      </main>
    </HydrateClient>
  );
}
