import { Injectable } from '@nestjs/common';
import { UpsertRuleDto } from './dto/upsert-rule.dto';

type ManagedRuleDefinition = {
  name: string;
  sourcePath: string;
  managed: boolean;
  content: string;
};

@Injectable()
export class AdminRulesService {
  private readonly backendBaseUrl =
    process.env.BACKEND_BASE_URL ?? 'http://localhost:8080';

  async listRules(): Promise<ManagedRuleDefinition[]> {
    const response = await fetch(`${this.backendBaseUrl}/api/v1/admin/rules`);
    if (!response.ok) {
      throw new Error(`Rules backend returned ${response.status}`);
    }

    return (await response.json()) as ManagedRuleDefinition[];
  }

  async saveRule(request: UpsertRuleDto): Promise<ManagedRuleDefinition> {
    const response = await fetch(`${this.backendBaseUrl}/api/v1/admin/rules`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(request),
    });

    if (!response.ok) {
      const body = await response.text();
      throw new Error(`Rules backend returned ${response.status}: ${body}`);
    }

    return (await response.json()) as ManagedRuleDefinition;
  }
}
