import { IsNotEmpty, IsOptional, IsString } from 'class-validator';

export class NextBidRequestDto {
  @IsString()
  @IsNotEmpty()
  hand!: string;

  @IsString()
  @IsOptional()
  bidding?: string;

  @IsString()
  @IsNotEmpty()
  system!: string;
}
