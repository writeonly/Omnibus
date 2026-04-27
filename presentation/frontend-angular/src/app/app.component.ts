import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { BffApiService } from './core/api/bff-api.service';
import { AuthService } from './core/services/auth.service';
import { AuctionAnalyzeResponse } from '@core/models/auction.dto';

import { FullBiddingCalculatorComponent } from './features/bidding/components/full-bidding-calculator/full-bidding-calculator.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    FullBiddingCalculatorComponent
  ],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})
export class AppComponent implements OnInit {

  private readonly bff = inject(BffApiService);
  readonly authService = inject(AuthService);

  // =========================
  // bidding state
  // =========================

  readonly northHand = signal('AQJ KQ2 A43 J742');
  readonly southHand = signal('T97 A854 Q76 K98');
  readonly auction = signal('');
  readonly system = signal('POLISH_CLUB');

  readonly loading = signal(false);
  readonly error = signal<string | null>(null);
  readonly result = signal<AuctionAnalyzeResponse | null>(null);

  ngOnInit(): void {
    void this.authService.init();
  }

  submit(): void {
    this.loading.set(true);
    this.error.set(null);
    this.result.set(null);

    this.bff.analyzeAuction({
      northHand: this.northHand(),
      southHand: this.southHand(),
      auction: this.auction(),
      system: this.system(),
    }).subscribe({
      next: (response: AuctionAnalyzeResponse) => {
        this.result.set(response);
        this.loading.set(false);
      },
      error: (err: unknown) => {
        this.error.set(err instanceof Error ? err.message : 'Failed');
        this.loading.set(false);
      }
    });
  }

  login(): void {
    this.authService.login();
  }

  logout(): void {
    this.authService.logout();
  }
}
