import { useEffect, useMemo, useState } from "react";

import { Shell } from "./components/Shell";
import type { RouteKey } from "./components/Shell";

import { NextBid } from "./features/rule/nextBid/NextBid";
import { RestBidding } from "./features/workflow/restBidding/RestBidding";

const routeByPath: Record<string, RouteKey> = {
  "/rule/next-bid": "next-bid",
  "/workflow/rest-bidding": "rest-bidding"
};

const pathByRoute: Record<RouteKey, string> = {
  "next-bid": "/rule/next-bid",
  "rest-bidding": "/workflow/rest-bidding"
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
    () => routeByPath[window.location.pathname] ?? "next-bid"
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
      setActiveRoute(routeByPath[window.location.pathname] ?? "next-bid");
    }

    window.addEventListener("popstate", handlePopState);

    return () => {
      window.removeEventListener("popstate", handlePopState);
    };
  }, []);

  const content = useMemo(() => {
    switch (activeRoute) {
      case "rest-bidding":
        return <RestBidding />;
      case "next-bid":
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
