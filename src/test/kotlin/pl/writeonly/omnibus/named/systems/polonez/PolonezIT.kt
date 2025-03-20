package pl.writeonly.omnibus.named.systems.polonez

import io.kotest.core.spec.style.StringSpec
import io.kotest.datatest.withData
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import io.vavr.collection.List
import jakarta.inject.Inject
import org.springframework.boot.test.context.SpringBootTest
import pl.writeonly.omnibus.OmnibusApplication
import pl.writeonly.omnibus.named.system.BidParser.parse
import pl.writeonly.omnibus.named.system.Bidding
import pl.writeonly.omnibus.named.system.Context
import pl.writeonly.omnibus.named.system.Hands

@SpringBootTest(classes = [OmnibusApplication::class])
class PolonezIT : StringSpec() {

    @Suppress("VariableDefinition")
    @Inject
    private lateinit var polonez: Polonez

    init {
        extension(SpringExtension)

        withData(
            nameFn = { "${it.handString} -> ${it.expectedBid}" },
            listOf(
                TestCase("AKQJ T987 6543 2", "pass"),
                TestCase("A432 A432 A432 A", "1K"),
                TestCase("A432 A432 A A432", "1C"),
                TestCase("AKQJ AKQJ AKQJ A", "1NT")
            )
        ) { (handString, expectedBid) ->
            val hand = Hands.fromString(handString)
            val bidding = Bidding(List.empty())
            val context = Context(hand, bidding)
            val bid = polonez.apply(context)

            bid shouldBe parse(expectedBid)
        }
    }

    data class TestCase(val handString: String, val expectedBid: String)
}
