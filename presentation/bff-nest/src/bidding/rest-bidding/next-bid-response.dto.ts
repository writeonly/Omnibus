import { IsNotEmpty, IsString } from "class-validator";

export class RestBiddingRequestDtoDto {
  @IsString()
  @IsNotEmpty()
  bidding!: string;
}
