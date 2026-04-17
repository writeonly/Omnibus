import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RecommendationResponse } from '../../services/recommendation-api.service';

@Component({
  selector: 'app-recommendation-result',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './recommendation-result.component.html',
  styleUrl: './recommendation-result.component.css',
})
export class RecommendationResultComponent {
  @Input({ required: true }) result!: RecommendationResponse;
}
