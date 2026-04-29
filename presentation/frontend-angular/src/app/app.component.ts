import { Component, inject } from '@angular/core';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatCardModule } from '@angular/material/card';
import { ThemeService } from './core/theme/theme.service';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { NavigationEnd, Router, RouterOutlet } from '@angular/router';
import { filter } from 'rxjs';
import { MatButtonToggleModule } from '@angular/material/button-toggle';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    MatToolbarModule,
    MatCardModule,
    MatSlideToggleModule,
    RouterOutlet,
    MatButtonToggleModule
  ],
  templateUrl: './app.component.html'
})
export class AppComponent {
  readonly theme = inject(ThemeService);
  private readonly router = inject(Router);

  activeRoute = '';

  constructor() {
    this.activeRoute = this.router.url;

    this.router.events
      .pipe(filter(event => event instanceof NavigationEnd))
      .subscribe((event: any) => {
        this.activeRoute = event.urlAfterRedirects;
      });
  }

  onRouteChange(route: string) {
    this.router.navigateByUrl(route);
  }
}
