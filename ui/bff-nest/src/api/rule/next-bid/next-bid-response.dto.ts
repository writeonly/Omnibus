import { IsNotEmpty, IsOptional, IsString } from 'class-validator';

export class NextBidResponseDto {
  @IsString()
  @IsNotEmpty()
  bid!: string;

  @IsString()
  @IsOptional()
  explanation?: string;
}
