import { Component, Input, Output, EventEmitter } from '@angular/core';

import { CommonModule } from '@angular/common';

@Component({
    selector: 'app-feature-panel',
    imports: [CommonModule],
    templateUrl: './feature-panel.component.html'
})
export class FeaturePanelComponent {
  @Input() title = '';
  @Input() subtitle = '';
  @Input() loading = false;
  @Input() error: string | null = null;
  @Input() hasResult = false;

  @Output() submit = new EventEmitter<void>();
  @Output() reset = new EventEmitter<void>();
}