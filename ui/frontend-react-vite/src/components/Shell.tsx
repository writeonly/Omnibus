import type { ReactNode } from 'react';

export type RouteKey = 'next-bid' | 'rest-bidding';

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

        <nav className="route-toggle" aria-label="Bidding mode">
          <button
            className={activeRoute === 'next-bid' ? 'active' : ''}
            type="button"
            onClick={() => onRouteChange('next-bid')}
          >
            Next bid
          </button>
          <button
            className={activeRoute === 'rest-bidding' ? 'active' : ''}
            type="button"
            onClick={() => onRouteChange('rest-bidding')}
          >
            Rest bidding
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
