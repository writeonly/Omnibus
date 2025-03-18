package pl.writeonly.omnibus.named.systems.polonez

import io.kotest.core.spec.style.StringSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import io.vavr.collection.List
import jakarta.inject.Inject
import org.springframework.boot.test.context.SpringBootTest
import pl.writeonly.omnibus.OmbibusApplication
import pl.writeonly.omnibus.named.system.Bid
import pl.writeonly.omnibus.named.system.Bidding
import pl.writeonly.omnibus.named.system.Context
import pl.writeonly.omnibus.named.system.Hand
import pl.writeonly.omnibus.named.system.Level
import pl.writeonly.omnibus.named.system.Suit
import pl.writeonly.omnibus.named.system.Trump

@SpringBootTest(classes = [OmbibusApplication::class])
class PolonezIT : StringSpec() {

    @Inject
    private lateinit var polonez: Polonez

    init {
        extension(SpringExtension)
        "pass" {
            val hand = Hand()
            val bidding = Bidding(List.empty())
            val context = Context(hand, bidding)
            val bid = polonez.apply(context)

            bid shouldBe Bid.Pass
        }

        "1C" {
            val hand = Hand(12u)
            val bidding = Bidding(List.empty())
            val context = Context(hand, bidding)
            val bid = polonez.apply(context)

            bid shouldBe Bid.LevelBid(Level.ONE, Trump.SuitTrump(Suit.CLUBS))
        }

        "1NT" {
            val hand = Hand(24u)
            val bidding = Bidding(List.empty())
            val context = Context(hand, bidding)
            val bid = polonez.apply(context)

            bid shouldBe Bid.LevelBid(Level.ONE, Trump.NoTrump)
        }
    }
}
