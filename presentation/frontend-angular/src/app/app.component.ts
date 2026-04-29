import { Component, inject } from '@angular/core';
import { RestBiddingComponent } from '@features/bidding/rest-bidding/rest-bidding.component';
import { NextBidComponent } from '@features/bidding/next-bid/next-bid.component';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatCardModule } from '@angular/material/card';
import { ThemeService } from './core/theme/theme.service';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';

@Component({
  selector: 'app-root',
  imports: [
    NextBidComponent,
    RestBiddingComponent,
    MatToolbarModule,
    MatCardModule,
    MatSlideToggleModule
  ],
  templateUrl: './app.component.html'
})
export class AppComponent {
  readonly theme = inject(ThemeService);
}