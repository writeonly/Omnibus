import { signal } from '@angular/core';

export abstract class BaseFeatureComponent<TResult> {

  readonly loading = signal(false);
  readonly error = signal<string | null>(null);
  readonly result = signal<TResult | null>(null);

  protected abstract apiCall(payload: any): any;
}