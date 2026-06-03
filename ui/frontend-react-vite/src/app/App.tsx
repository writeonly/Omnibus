import { useEffect, useMemo, useState } from "react";

import { Shell } from "../components/Shell";
import type { RouteKey } from "../components/Shell";

import { Login } from "../features/auth/login/Login";
import { NextBid } from "../features/rule/nextBid/NextBid";
import { RestBidding } from "../features/workflow/restBidding/RestBidding";
import { Register } from "../features/user/register/Register";

const routeByPath: Record<string, RouteKey> = {
  "/workflow/rest-bidding": "rest-bidding",
  "/rule/next-bid": "next-bid",
  "/user/register": "register",
  "/auth/login": "login"
};

const pathByRoute: Record<RouteKey, string> = {
  "rest-bidding": "/workflow/rest-bidding",
  "next-bid": "/rule/next-bid",
  "register": "/user/register",
  "login": "/auth/login"
};

function readInitialTheme(): boolean {
  const saved = localStorage.getItem("theme");

  if (saved) {
    return saved === "dark";
  }

  return window.matchMedia("(prefers-color-scheme: dark)").matches;
}

export function App() {
  const [activeRoute, setActiveRoute] = useState<RouteKey>(
    () => routeByPath[window.location.pathname] ?? "register"
  );

  const [isDark, setIsDark] = useState<boolean>(readInitialTheme);

  useEffect(() => {
    document.documentElement.classList.toggle("dark-theme", isDark);
    localStorage.setItem("theme", isDark ? "dark" : "light");
  }, [isDark]);

  useEffect(() => {
    const currentPath = pathByRoute[activeRoute];

    if (window.location.pathname !== currentPath) {
      window.history.replaceState(null, "", currentPath);
    }
  }, [activeRoute]);

  useEffect(() => {
    function handlePopState() {
      setActiveRoute(routeByPath[window.location.pathname] ?? "register");
    }

    window.addEventListener("popstate", handlePopState);

    return () => {
      window.removeEventListener("popstate", handlePopState);
    };
  }, []);

  const content = useMemo(() => {
    switch (activeRoute) {
      case "next-bid":
        return <NextBid />;
      case "rest-bidding":
        return <RestBidding />;
      case "register":
        return <Register />;
      case "login":
        return <Login />;
      default:
        return <NextBid />; 
    }
  }, [activeRoute]);

  function changeRoute(route: RouteKey) {
    if (route === activeRoute) return;

    setActiveRoute(route);
    window.history.pushState(null, "", pathByRoute[route]);
  }

  return (
    <Shell
      activeRoute={activeRoute}
      isDark={isDark}
      onRouteChange={changeRoute}
      onThemeToggle={() => setIsDark((v) => !v)}
    >
      {content}
    </Shell>
  );
}
