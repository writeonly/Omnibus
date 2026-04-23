import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { catchError, Observable, throwError } from 'rxjs';
import { AuthService } from './auth.service';

export type ManagedRuleDefinition = {
  name: string;
  sourcePath: string;
  managed: boolean;
  content: string;
};

type UpsertRuleRequest = {
  name: string;
  content: string;
};

@Injectable({
  providedIn: 'root',
})
export class AdminRulesApiService {
  private readonly httpClient = inject(HttpClient);
  private readonly authService = inject(AuthService);

  listRules(): Observable<ManagedRuleDefinition[]> {
    return this.httpClient
      .get<ManagedRuleDefinition[]>('/api/admin/rules', {
        headers: this.headers(),
      })
      .pipe(catchError((error) => throwError(() => new Error(this.message(error)))));
  }

  saveRule(request: UpsertRuleRequest): Observable<ManagedRuleDefinition> {
    return this.httpClient
      .post<ManagedRuleDefinition>('/api/admin/rules', request, {
        headers: this.headers(),
      })
      .pipe(catchError((error) => throwError(() => new Error(this.message(error)))));
  }

  private headers(): HttpHeaders {
    const authorization = this.authService.authorizationHeader();
    return authorization
      ? new HttpHeaders({
          Authorization: authorization,
        })
      : new HttpHeaders();
  }

  private message(error: { error?: { message?: string } }): string {
    return error?.error?.message ?? 'Admin API request failed';
  }
}
