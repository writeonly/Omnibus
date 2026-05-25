import { Routes } from '@angular/router';

import { NextBidComponent } from './features/rule/next-bid/next-bid.component';
import { RestBiddingComponent } from './features/workflow/rest-bidding/rest-bidding.component';

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'rule/next-bid',
    pathMatch: 'full',
  },
  {
    path: 'rule/next-bid',
    component: NextBidComponent,
  },
  {
    path: 'workflow/rest-bidding',
    component: RestBiddingComponent,
  },
  {
    path: '**',
    redirectTo: 'rule/next-bid',
  },
];