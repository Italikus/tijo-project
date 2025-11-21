import { Component, computed, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ErrorService } from '../core/error.service';

@Component({
  selector: 'app-error-alert',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div *ngIf="hasError()" class="alert error-alert" role="alert">
      <span *ngIf="status() !== null" class="badge">{{ status() }}</span>
      <span class="msg">{{ message() }}</span>
      <button class="close" type="button" (click)="clear()" aria-label="Zamknij">×</button>
    </div>
  `
})
export class ErrorAlertComponent {
  private readonly errors = inject(ErrorService);
  readonly message = computed(() => this.errors.message());
  readonly status = computed(() => this.errors.status());
  readonly hasError = computed(() => !!this.errors.message());

  clear() { this.errors.clear(); }
}

