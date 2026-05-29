package pl.writeonly.omnibus.modulith.controller.rule

import net.devh.boot.grpc.client.inject.GrpcClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.writeonly.omnibus.modulith.controller.grpcMono
import pl.writeonly.omnibus.grpc.rule.nextbid.v1.NextBidRequest
import pl.writeonly.omnibus.grpc.rule.nextbid.v1.NextBidResponse
import pl.writeonly.omnibus.grpc.rule.nextbid.v1.NextBidServiceGrpc
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/rule/next-bid")
class NextBidController(
    @GrpcClient("nextBidService")
    private val stub: NextBidServiceGrpc.NextBidServiceStub
) {
    @PostMapping
    fun nextBid(@RequestBody body: NextBidRequest): Mono<NextBidResponse> {
        return grpcMono { observer ->
            stub.nextBid(body, observer)
        }
    }
}