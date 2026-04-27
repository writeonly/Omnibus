export type Suit = 'S' | 'H' | 'D' | 'C';

export interface Hand {
  cards: Record<Suit, string[]>;

  hcp: number;
  shape: string;
}