package pl.writeonly.omnibus.named.system

class Hand(private val points: UInt = 0u) {

    fun points(): UInt {
        return points;
    }

    fun doublePoints(): UInt {
        return points() / 2u;
    }


}


enum class  Rank {
    TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING, ACE
}

enum class Suit {
    CLUBS, DIAMONDS, HEARTS, SPADES
}