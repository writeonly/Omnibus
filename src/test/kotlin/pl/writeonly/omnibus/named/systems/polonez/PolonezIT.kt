import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import jakarta.inject.Inject
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import pl.writeonly.omnibus.named.system.Bidding
import pl.writeonly.omnibus.named.system.Context
import pl.writeonly.omnibus.named.system.Hand
import pl.writeonly.omnibus.named.systems.polonez.Polonez

import io.vavr.collection.List
import pl.writeonly.omnibus.named.system.Bid

@SpringBootTest
@ActiveProfiles("test")
class PolonezIT : StringSpec({

    @Inject
    lateinit var polonez: Polonez

    "should" {
        val hand = Hand()
        val bidding = Bidding(List.empty())
        val context = Context(hand, bidding)
        val bid = polonez.apply(context)

        bid shouldBe Bid.Pass
    }
})