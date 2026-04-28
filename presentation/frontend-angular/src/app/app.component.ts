import { Component } from '@angular/core';
import { FullBiddingComponent } from '@features/bidding/components/full-bidding/full-bidding.component';
import { NextBidCalculatorComponent } from '@features/bidding/components/next-bid-calculator/next-bid-calculator.component';


@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    NextBidCalculatorComponent,
    FullBiddingComponent
  ],
  templateUrl: './app.component.html',
})
export class AppComponent {}
