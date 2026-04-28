import { signal } from '@angular/core';

export abstract class BaseFeatureComponent<TForm, TResult> {

  protected abstract getInitialForm(): TForm;
  protected abstract apiCall(payload: TForm): any;

  readonly form = signal<TForm>(this.getInitialForm());
  readonly loading = signal(false);
  readonly error = signal<string | null>(null);
  readonly result = signal<TResult | null>(null);

  update<K extends keyof TForm>(key: K, value: TForm[K]) {
    this.form.update(f => ({
      ...f,
      [key]: value,
    }));
  }

  submit(): void {
    this.loading.set(true);
    this.error.set(null);
    this.result.set(null);

    this.apiCall(this.form()).subscribe({
      next: (res: TResult) => {
        this.result.set(res);
        this.loading.set(false);
      },
      error: () => {
        this.error.set('Request failed');
        this.loading.set(false);
      }
    });
  }

  reset(): void {
    this.form.set(this.getInitialForm());
    this.result.set(null);
    this.error.set(null);
  }
}
