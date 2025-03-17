package pl.writeonly.omnibus.named.systems.polonez

import io.kotest.core.spec.style.StringSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import jakarta.inject.Inject
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import pl.writeonly.omnibus.named.system.Bidding
import pl.writeonly.omnibus.named.system.Context
import pl.writeonly.omnibus.named.system.Hand
import pl.writeonly.omnibus.named.systems.polonez.Polonez

import io.vavr.collection.List
import org.springframework.beans.factory.annotation.Autowired
import pl.writeonly.omnibus.OmbibusApplication
import pl.writeonly.omnibus.named.system.Bid

@SpringBootTest(classes = [OmbibusApplication::class])
@ActiveProfiles("test")
class PolonezIT : StringSpec() {

    @Inject
    private lateinit var polonez: Polonez

    init {
        extension(SpringExtension)
        "should" {
            val hand = Hand()
            val bidding = Bidding(List.empty())
            val context = Context(hand, bidding)
            val bid = polonez.apply(context)

            bid shouldBe Bid.Pass
        }
    }
}