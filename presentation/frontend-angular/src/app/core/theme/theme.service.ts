import { Injectable, signal } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class ThemeService {
  private readonly STORAGE_KEY = 'theme';

  readonly isDark = signal(false);

  constructor() {
    const saved = localStorage.getItem(this.STORAGE_KEY);

    if (saved) {
      this.setTheme(saved === 'dark');
    } else {
      const prefersDark = window.matchMedia('(prefers-color-scheme: dark)').matches;
      this.setTheme(prefersDark);
    }
  }

  toggle(): void {
    this.setTheme(!this.isDark());
  }

  setTheme(isDark: boolean): void {
    this.isDark.set(isDark);

    const root = document.documentElement;

    if (isDark) {
      root.classList.add('dark-theme');
      localStorage.setItem(this.STORAGE_KEY, 'dark');
    } else {
      root.classList.remove('dark-theme');
      localStorage.setItem(this.STORAGE_KEY, 'light');
    }
  }
}
