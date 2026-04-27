import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BidRecommendResponse } from '@core/models/bid.dto';

@Component({
  selector: 'app-full-bidding-calculator',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './full-bidding-calculator.component.html',
  styleUrls: ['./full-bidding-calculator.component.css'],
})
export class FullBiddingCalculatorComponent {
  @Input({ required: true }) result!: any;
}
