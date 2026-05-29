import "~/styles/globals.css";

import { type Metadata } from "next";
import { Fraunces, Space_Grotesk } from "next/font/google";

import { TRPCReactProvider } from "~/trpc/react";

export const metadata: Metadata = {
  title: "Omnibus Registration",
  description: "Registration flow through tRPC, Gateway, Modulith, gRPC, and Keycloak",
  icons: [{ rel: "icon", url: "/favicon.ico" }],
};

const display = Fraunces({
  subsets: ["latin"],
  variable: "--font-display",
});

const sans = Space_Grotesk({
  subsets: ["latin"],
  variable: "--font-sans",
});

export default function RootLayout({
  children,
}: Readonly<{ children: React.ReactNode }>) {
  return (
    <html lang="en" className={`${display.variable} ${sans.variable}`}>
      <body>
        <TRPCReactProvider>{children}</TRPCReactProvider>
      </body>
    </html>
  );
}
