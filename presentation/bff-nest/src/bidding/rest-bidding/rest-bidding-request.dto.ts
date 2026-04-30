import { IsNotEmpty, IsString } from 'class-validator';

export class RestBiddingRequestDto {
  @IsString()
  @IsNotEmpty()
  northHand!: string;

  @IsString()
  @IsNotEmpty()
  southHand!: string;

  @IsString()
  @IsNotEmpty()
  bidding!: string;

  @IsString()
  @IsNotEmpty()
  system!: string;
}
