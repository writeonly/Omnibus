import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RecommendationApiService, RecommendationResponse } from './services/recommendation-api.service';
import { RecommendationResultComponent } from './components/recommendation-result/recommendation-result.component';
import { AuthService } from './services/auth.service';
import {
  AdminRulesApiService,
  ManagedRuleDefinition,
  RulePublicationSubmission,
} from './services/admin-rules-api.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, FormsModule, RecommendationResultComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
})
export class AppComponent implements OnInit {
  private readonly recommendationApiService = inject(RecommendationApiService);
  private readonly adminRulesApiService = inject(AdminRulesApiService);
  readonly authService = inject(AuthService);

  readonly northHand = signal('AQJ KQ2 A43 J742');
  readonly southHand = signal('T97 A854 Q76 K98');
  readonly auction = signal('');
  readonly system = signal('POLISH_CLUB');
  readonly loading = signal(false);
  readonly error = signal('');
  readonly result = signal<RecommendationResponse | null>(null);
  readonly adminRuleName = signal('balanced-18-19-nt');
  readonly adminRuleContent = signal(`package rules

import com.omnibus.bidding.rules.BiddingFacts;
import com.omnibus.bidding.rules.CandidateBid;

rule "OPEN_ONE_NOTRUMP_18_19"
salience 95
when
    BiddingFacts(balanced == true, hcp >= 18, hcp <= 19)
then
    insert(new CandidateBid("2NT", 95, "Managed rule: balanced 18-19 HCP hand"));
end`);
  readonly adminRules = signal<ManagedRuleDefinition[]>([]);
  readonly adminLoading = signal(false);
  readonly adminError = signal('');
  readonly adminSuccess = signal('');
  readonly lastSubmission = signal<RulePublicationSubmission | null>(null);

  ngOnInit(): void {
    void this.initializeAuth();
  }

  submit(): void {
    this.loading.set(true);
    this.error.set('');

    this.recommendationApiService
      .recommend({
        northHand: this.northHand(),
        southHand: this.southHand(),
        auction: this.auction(),
        system: this.system(),
      })
      .subscribe({
        next: (response) => {
          this.result.set(response);
          this.loading.set(false);
        },
        error: (error: Error) => {
          this.error.set(error.message);
          this.loading.set(false);
        },
      });
  }

  login(): void {
    this.authService.login();
  }

  logout(): void {
    this.authService.logout();
  }

  loadRules(): void {
    this.adminLoading.set(true);
    this.adminError.set('');
    this.adminSuccess.set('');

    this.adminRulesApiService.listRules().subscribe({
      next: (rules) => {
        this.adminRules.set(rules);
        this.adminLoading.set(false);
      },
      error: (error: Error) => {
        this.adminError.set(error.message);
        this.adminLoading.set(false);
      },
    });
  }

  saveRule(): void {
    this.adminLoading.set(true);
    this.adminError.set('');
    this.adminSuccess.set('');
    this.lastSubmission.set(null);

    this.adminRulesApiService
      .saveRule({
        name: this.adminRuleName(),
        content: this.adminRuleContent(),
      })
      .subscribe({
        next: (submission) => {
          this.lastSubmission.set(submission);
          this.adminSuccess.set(
            `Proces Camunda wystartował dla reguły ${submission.ruleName}. Instancja: ${submission.processInstanceKey}.`,
          );
          window.setTimeout(() => this.loadRules(), 1500);
        },
        error: (error: Error) => {
          this.adminError.set(error.message);
          this.adminLoading.set(false);
        },
      });
  }

  private async initializeAuth(): Promise<void> {
    try {
      await this.authService.init();
      if (this.authService.isAdmin()) {
        this.loadRules();
      }
    } catch (error) {
      this.adminError.set('Nie udało się zainicjalizować logowania Keycloak.');
    }
  }
}
