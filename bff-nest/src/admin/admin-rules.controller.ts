import {
  Body,
  Controller,
  Get,
  Headers,
  Post,
} from '@nestjs/common';
import { AuthService } from '../auth/auth.service';
import { AdminRulesService } from './admin-rules.service';
import { UpsertRuleDto } from './dto/upsert-rule.dto';

@Controller('admin/rules')
export class AdminRulesController {
  constructor(
    private readonly authService: AuthService,
    private readonly adminRulesService: AdminRulesService,
  ) {}

  @Get()
  async listRules(@Headers('authorization') authorization?: string) {
    await this.authService.requireAdmin(authorization);
    return this.adminRulesService.listRules();
  }

  @Post()
  async saveRule(
    @Headers('authorization') authorization: string | undefined,
    @Body() request: UpsertRuleDto,
  ) {
    const adminContext = await this.authService.requireAdminContext(authorization);
    return this.adminRulesService.saveRule(request, adminContext.username);
  }
}
