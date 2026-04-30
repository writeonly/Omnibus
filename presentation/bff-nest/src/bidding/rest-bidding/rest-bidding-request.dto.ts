import { IsNotEmpty, IsString } from 'class-validator';

export class RestBiddingRequestDto {
  @IsString()
  @IsNotEmpty()
  northHand!: string;

  @IsString()
  @IsNotEmpty()
  souhHand!: string;

  @IsString()
  @IsNotEmpty()
  bidding!: string;

  @IsString()
  @IsNotEmpty()
  system!: string;
}
