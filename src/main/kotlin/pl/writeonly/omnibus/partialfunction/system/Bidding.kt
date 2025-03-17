package pl.writeonly.omnibus.partialfunction.system

import io.vavr.collection.List

class Bidding(private val bidding: List<Bid>) {
    fun isEmpty(): Boolean {
        return bidding.isEmpty
    }
}