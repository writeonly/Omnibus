package pl.writeonly.omnibus.jakarta.named.polonez.rule

import io.kotest.core.spec.style.StringSpec
import io.kotest.datatest.withData
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import io.vavr.collection.List
import jakarta.inject.Inject
import org.springframework.boot.test.context.SpringBootTest
import pl.writeonly.omnibus.OmnibusApplication
import pl.writeonly.omnibus.jakarta.common.system.BidParser.parse
import pl.writeonly.omnibus.jakarta.common.system.Bidding
import pl.writeonly.omnibus.jakarta.common.system.Context
import pl.writeonly.omnibus.jakarta.common.system.Hands
import pl.writeonly.omnibus.jakarta.named.polonez.Polonez

@SpringBootTest(classes = [OmnibusApplication::class])
class ResponsesIT : StringSpec() {

    @Suppress("VariableDefinition")
    @Inject
    private lateinit var polonez: Polonez

    init {
        extension(SpringExtension)

        withData(
            nameFn = { "${it.handString} -> ${it.expectedBid}" },
            listOf(
//                TestCase("AKT9 T987 6543 2", "1C", "1D"),
//                TestCase("AKQJ T987 6543 2", "1C", "1D"),
//                TestCase("AKQJ A987 6543 2", "1C", "1D"),
//                TestCase("A432 A432 A432 A", "1C", "1D"),
                TestCase("A432 A432 A A432", "1C", "3NT"),
                TestCase("AKQJ AKQJ AKQJ A", "1C", "3NT")
            )
        ) { (handString, opening, expectedBid) ->
            val hand = Hands.fromString(handString)
            val bidding = Bidding(List.of(parse(opening), parse("pass")))
            val context = Context(hand, bidding)
            val bid = polonez.apply(context)

            bid shouldBe parse(expectedBid)
        }
    }

    data class TestCase(val handString: String, val opening: String, val expectedBid: String)
}
