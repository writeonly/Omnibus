import { Injectable } from '@nestjs/common';
import { UpsertRuleDto } from './dto/upsert-rule.dto';

type ManagedRuleDefinition = {
  name: string;
  sourcePath: string;
  managed: boolean;
  content: string;
};

type RulePublicationSubmission = {
  processInstanceKey: string;
  bpmnProcessId: string;
  status: string;
  ruleName: string;
  requestedBy: string;
};

@Injectable()
export class AdminRulesService {
  private readonly backendBaseUrl =
    process.env.BIDDING_ENGINE_BASE_URL ?? 'http://localhost:8081';
  private readonly workflowBaseUrl =
    process.env.WORKFLOW_BASE_URL ?? 'http://localhost:8082';

  async listRules(): Promise<ManagedRuleDefinition[]> {
    const response = await fetch(`${this.backendBaseUrl}/api/v1/admin/rules`);
    if (!response.ok) {
      throw new Error(`Rules backend returned ${response.status}`);
    }

    return (await response.json()) as ManagedRuleDefinition[];
  }

  async saveRule(
    request: UpsertRuleDto,
    requestedBy: string,
  ): Promise<RulePublicationSubmission> {
    const response = await fetch(`${this.workflowBaseUrl}/api/v1/rule-publications`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        ...request,
        requestedBy,
      }),
    });

    if (!response.ok) {
      const body = await response.text();
      throw new Error(`Workflow service returned ${response.status}: ${body}`);
    }

    return (await response.json()) as RulePublicationSubmission;
  }
}
