package pl.writeonly.omnibus.named.system

import io.vavr.collection.List

class Bidding(val raw: List<Bid>) {
    fun isEmpty(): Boolean = raw.isEmpty

    fun trim(): Bidding = Bidding(trimRecursive(raw))

    private tailrec fun trimRecursive(bidding: List<Bid>): List<Bid> =
        if (bidding.isEmpty || bidding.head() != Bid.Pass) {
            bidding
        } else {
            trimRecursive(bidding.tail())
        }
}
