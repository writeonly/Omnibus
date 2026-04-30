import { IsNotEmpty, IsString } from "class-validator";

export class NextBidResponseDto {
  @IsString()
  @IsNotEmpty()
  bid!: string;
}
