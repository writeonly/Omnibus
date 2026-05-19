import { CallHandler, ExecutionContext, Injectable, NestInterceptor } from '@nestjs/common';
import type { Response } from 'express';
import { Observable, throwError } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';

import { OutboxService } from './outbox.service';
import type { HttpRequest } from './outbox.types';

@Injectable()
export class OutboxInterceptor implements NestInterceptor {
  constructor(private readonly outbox: OutboxService) {}

  intercept(context: ExecutionContext, next: CallHandler): Observable<unknown> {
    if (context.getType() !== 'http') {
      return next.handle();
    }

    const http = context.switchToHttp();
    const request = http.getRequest<HttpRequest>();
    const response = http.getResponse<Response>();
    const start = Date.now();

    return next.handle().pipe(
      tap((body) => {
        void this.outbox.recordHttpResult({
          request,
          response: body,
          statusCode: response.statusCode,
          durationMs: Date.now() - start,
        });
      }),
      catchError((error: Error) => {
        void this.outbox.recordHttpResult({
          request,
          error,
          statusCode: response.statusCode >= 400 ? response.statusCode : 500,
          durationMs: Date.now() - start,
        });

        return throwError(() => error);
      })
    );
  }
}
