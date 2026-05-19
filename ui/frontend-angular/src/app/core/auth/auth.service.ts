import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({ providedIn: 'root' })
export class AuthService {
  constructor(private http: HttpClient) { }

  me() {
    return this.http.get('/api/auth/me');
  }

  login() {
    window.location.href = this.redirectUrl('/api/auth/login');
  }

  register() {
    window.location.href = this.redirectUrl('/api/auth/register');
  }

  logout() {
    window.location.href = this.redirectUrl('/api/auth/logout');
  }

  private redirectUrl(path: string): string {
    const redirectUri = encodeURIComponent(window.location.origin);
    return `${path}?redirectUri=${redirectUri}`;
  }
}
