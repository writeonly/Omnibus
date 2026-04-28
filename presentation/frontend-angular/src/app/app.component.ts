import { Component } from '@angular/core';
import { BiddingRecommenderComponent } from '@features/bidding/components/bidding-recommender/bidding-recommender.component';
import { BidRecommenderComponent } from '@features/bidding/components/bid-recommender/bid-recommender.component';


@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    BidRecommenderComponent,
    BiddingRecommenderComponent
  ],
  templateUrl: './app.component.html',
})
export class AppComponent {}
