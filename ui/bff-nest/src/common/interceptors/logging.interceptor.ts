import {
  Injectable,
  NestInterceptor,
  ExecutionContext,
  CallHandler,
  Logger,
} from '@nestjs/common';
import { catchError, tap } from 'rxjs/operators';
import { throwError } from 'rxjs';

@Injectable()
export class LoggingInterceptor implements NestInterceptor {
  private logger = new Logger('HTTP');

  intercept(context: ExecutionContext, next: CallHandler) {
    const req = context.switchToHttp().getRequest();

    const start = Date.now();

    this.logger.log(`➡️ ${req.method} ${req.url}`);

    return next.handle().pipe(
      tap(() => {
        this.logger.log(
          `✅ ${req.method} ${req.url} ${Date.now() - start}ms`,
        );
      }),
      catchError((err) => {
        this.logger.error(
          `❌ ${req.method} ${req.url}`,
          err.stack,
        );
        return throwError(() => err);
      }),
    );
  }
}