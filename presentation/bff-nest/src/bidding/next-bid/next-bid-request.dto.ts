import { IsNotEmpty, IsString } from 'class-validator';

export class NextBidRequestDtoDto {
  @IsString()
  @IsNotEmpty()
  hand!: string;

  @IsString()
  @IsNotEmpty()
  bidding!: string;

  @IsString()
  @IsNotEmpty()
  system!: string;
}
