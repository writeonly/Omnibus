import { Routes } from '@angular/router';

import { NextBidCalculatorComponent } from './features/bidding/components/next-bid-calculator/next-bid-calculator.component';
import { FullBiddingCalculatorComponent } from './features/bidding/components/full-bidding-calculator/full-bidding-calculator.component';

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
        component: NextBidCalculatorComponent,
      },
      {
        path: 'full',
        component: FullBiddingCalculatorComponent,
      },
    ],
  },
];
