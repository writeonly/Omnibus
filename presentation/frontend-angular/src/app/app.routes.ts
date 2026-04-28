import { Routes } from '@angular/router';

import { BidRecommenderComponent } from './features/bidding/components/bid-recommender/bid-recommender.component';
import { BiddingRecommenderComponent } from './features/bidding/components/bidding-recommender/bidding-recommender.component';

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'bidding/next-bid',
    pathMatch: 'full',
  },
  {
    path: 'bidding',
    children: [
      {
        path: 'next-bid',
        component: BidRecommenderComponent,
      },
      {
        path: 'full',
        component: BiddingRecommenderComponent,
      },
    ],
  },
];
