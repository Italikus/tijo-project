import { Component, signal } from '@angular/core';
import { RouterOutlet, RouterLink, Router } from '@angular/router';
import { ErrorAlertComponent } from './shared/error-alert.component';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, RouterLink, ErrorAlertComponent],
  templateUrl: './app.html',
  styleUrls: ['./app.css']
})
export class App {
  protected readonly title = signal('tijo-frontend');
  constructor(private router: Router) {}

  isActive(path: string) {
    return this.router.url.startsWith(path);
  }
}
