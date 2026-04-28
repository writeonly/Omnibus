import { Component } from '@angular/core';
import { RestBiddingComponent } from '@features/bidding/rest-bidding/rest-bidding.component';
import { NextBidComponent } from '@features/bidding/next-bid/next-bid.component';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatCardModule } from '@angular/material/card';

@Component({
    selector: 'app-root',
    imports: [
      NextBidComponent,
      RestBiddingComponent,
      MatToolbarModule,
      MatCardModule
    ],
    templateUrl: './app.component.html'
})
export class AppComponent {}
