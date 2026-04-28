import { IsNotEmpty, IsOptional, IsString } from 'class-validator';

export class RecommendationRequestDto {
  @IsString()
  @IsNotEmpty()
  northHand!: string;

  @IsString()
  @IsNotEmpty()
  southHand!: string;

  @IsString()
  @IsOptional()
  bidding?: string;

  @IsString()
  @IsNotEmpty()
  system!: string;
}
