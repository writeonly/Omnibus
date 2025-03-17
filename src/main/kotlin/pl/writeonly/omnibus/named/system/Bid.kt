package pl.writeonly.omnibus.named.system

sealed class Bid {
    data class LevelBid(val level: Level, val trump: Trump) : Bid()
    data object Pass : Bid()
    data object Double : Bid()
    data object Redouble : Bid()

}


enum class Level {
    ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN
}

sealed class Trump {
    data class SuitTrump(val suit: Suit) : Trump()
    data object NoTrump : Trump()
}


