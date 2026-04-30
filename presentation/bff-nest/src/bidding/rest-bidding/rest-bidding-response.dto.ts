import { IsNotEmpty, IsString } from "class-validator";

export class RestBiddingResponseDto {
  @IsString()
  @IsNotEmpty()
  bidding!: string;
}
