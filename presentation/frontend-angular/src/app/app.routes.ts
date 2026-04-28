import { Routes } from '@angular/router';

import { NextBidComponent } from './features/bidding/next-bid/next-bid.component';
import { RestBiddingComponent } from './features/bidding/rest-bidding/rest-bidding.component';

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
        component: NextBidComponent,
      },
      {
        path: 'full',
        component: RestBiddingComponent,
      },
    ],
  },
];
