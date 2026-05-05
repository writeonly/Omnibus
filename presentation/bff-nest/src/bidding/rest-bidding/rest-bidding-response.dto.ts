import { IsNotEmpty, IsOptional, IsString } from 'class-validator';

export class RestBiddingResponseDto {
  @IsString()
  @IsNotEmpty()
  bidding!: string;

  @IsString()
  @IsOptional()
  explanation?: string;
}
