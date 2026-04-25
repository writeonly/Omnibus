import { IsNotEmpty, IsString } from 'class-validator';

export class UpsertRuleDto {
  @IsString()
  @IsNotEmpty()
  name!: string;

  @IsString()
  @IsNotEmpty()
  content!: string;
}
