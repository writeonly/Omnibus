import type { ReactNode } from 'react';

export type RouteKey = 'rest-bidding' | 'next-bid' | 'register' | 'login';

interface ShellProps {
  activeRoute: RouteKey;
  isDark: boolean;
  onRouteChange: (route: RouteKey) => void;
  onThemeToggle: () => void;
  children: ReactNode;
}

export function Shell({ activeRoute, isDark, onRouteChange, onThemeToggle, children }: ShellProps) {
  return (
    <>
      <header className="toolbar">
        <div className="brand">
          <span className="logo">Omnibus</span>
          <span className="subtitle">Bridge bidding engine</span>
        </div>

        <nav className="route-toggle" aria-label="Feature mode">
          <button
            className={activeRoute === 'rest-bidding' ? 'active' : ''}
            type="button"
            onClick={() => onRouteChange('rest-bidding')}
          >
            Rest bidding
          </button>
          <button
            className={activeRoute === 'next-bid' ? 'active' : ''}
            type="button"
            onClick={() => onRouteChange('next-bid')}
          >
            Next bid
          </button>
          <button
            className={activeRoute === 'login' ? 'active' : ''}
            type="button"
            onClick={() => onRouteChange('login')}
          >
            Login
          </button>
          <button
            className={activeRoute === 'register' ? 'active' : ''}
            type="button"
            onClick={() => onRouteChange('register')}
          >
            Register
          </button>
        </nav>

        <label className="theme-toggle">
          <input type="checkbox" checked={isDark} onChange={onThemeToggle} />
          <span>{isDark ? 'Dark' : 'Light'}</span>
        </label>
      </header>

      <main className="shell">{children}</main>
    </>
  );
}
