import { Injectable, signal } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { ErrorResponse } from './error.model';

@Injectable({ providedIn: 'root' })
export class ErrorService {
  readonly message = signal<string | null>(null);
  readonly status = signal<number | null>(null);

  clear() {
    this.message.set(null);
    this.status.set(null);
  }

  setFromHttpError(err: HttpErrorResponse) {
    if (err.status === 0) {
      this.status.set(0);
      this.message.set('Brak połączenia z serwerem.');
      return;
    }

    const body = err.error as Partial<ErrorResponse> | string | null | undefined;
    if (body && typeof body === 'object') {
      const msg = (body.message as string) || err.statusText || 'Wystąpił błąd.';
      this.status.set(err.status);
      this.message.set(msg);
      return;
    }

    this.status.set(err.status);
    this.message.set(typeof body === 'string' && body.trim() ? body : (err.message || 'Wystąpił błąd.'));
  }
}

