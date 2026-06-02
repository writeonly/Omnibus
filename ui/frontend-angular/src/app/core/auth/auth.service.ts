import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";

export interface RegisterUserRequest {
  username: string;
  email: string;
  password: string;
  firstName?: string;
  lastName?: string;
}

export interface RegisterUserResponse {
  userId: string;
  username: string;
  email: string;
  status: string;
}

@Injectable({ providedIn: "root" })
export class AuthService {
  constructor(private http: HttpClient) {}

  me() {
    return this.http.get("/api/auth/me");
  }

  login() {
    window.location.href = this.redirectUrl("/api/auth/login");
  }

  register(request: RegisterUserRequest): Observable<RegisterUserResponse> {
    return this.http.post<RegisterUserResponse>("/api/auth/register", request);
  }

  logout() {
    window.location.href = this.redirectUrl("/api/auth/logout");
  }

  private redirectUrl(path: string): string {
    const redirectUri = encodeURIComponent(window.location.origin);
    return `${path}?redirectUri=${redirectUri}`;
  }
}
