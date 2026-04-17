import { IsNotEmpty, IsOptional, IsString } from 'class-validator';

export class RecommendationRequestDto {
  @IsString()
  @IsNotEmpty()
  hand!: string;

  @IsString()
  @IsOptional()
  auction?: string;

  @IsString()
  @IsNotEmpty()
  system!: string;
}
