package pl.writeonly.omnibus.named.systems.polonez

import io.kotest.core.spec.style.StringSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import jakarta.inject.Inject
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

import io.vavr.collection.List
import pl.writeonly.omnibus.OmbibusApplication
import pl.writeonly.omnibus.named.system.*

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