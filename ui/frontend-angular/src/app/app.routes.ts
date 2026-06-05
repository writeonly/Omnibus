import { Routes } from "@angular/router";

import { NextBidComponent } from "./features/rule/next-bid/next-bid.component";
import { RestBiddingComponent } from "./features/workflow/rest-bidding/rest-bidding.component";
import { RegisterComponent } from "./features/user/register/register.component";

export const routes: Routes = [
  {
    path: "",
    redirectTo: "workflow/rest-bidding",
    pathMatch: "full",
  },
  {
    path: "rule/next-bid",
    component: NextBidComponent,
  },
  {
    path: "workflow/rest-bidding",
    component: RestBiddingComponent,
  },
  {
    path: "user/register",
    component: RegisterComponent,
  },
  {
    path: "**",
    redirectTo: "workflow/rest-bidding",
  },
];
