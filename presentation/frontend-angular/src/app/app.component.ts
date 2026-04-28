import { Component } from '@angular/core';
import { RestBiddingComponent } from '@features/bidding/components/rest-bidding/rest-bidding.component';
import { NextBidComponent } from '@features/bidding/components/next-bid/next-bid.component';


@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    NextBidComponent,
    RestBiddingComponent
  ],
  templateUrl: './app.component.html',
})
export class AppComponent {}
