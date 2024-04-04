package pl.writeonly.omnibus.data

object Quarter {
    fun quartersForColor(color: List<Card>): Int {
        return countCard(color, Card.A, 4) +
                countCard(color, Card.K, 3) +
                countCard(color, Card.Q, 2)
    }

    fun countCard(color: List<Card>, card: Card, strong: Int): Int {
        return (if (color.contains(card)) strong else 0)
    }
}