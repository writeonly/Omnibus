package pl.writeonly.omnibus.data

data class Hand(
    val spades: List<Card>,
    val hearts: List<Card>,
    val diamonds: List<Card>,
    val clubs: List<Card>,
)
