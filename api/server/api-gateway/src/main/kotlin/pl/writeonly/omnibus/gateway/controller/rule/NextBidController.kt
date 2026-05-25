package pl.writeonly.omnibus.gateway.controller.rule

import net.devh.boot.grpc.client.inject.GrpcClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.writeonly.omnibus.grpc.rule.nextbid.v1.NextBidRequest
import pl.writeonly.omnibus.grpc.rule.nextbid.v1.NextBidResponse
import pl.writeonly.omnibus.grpc.rule.nextbid.v1.NextBidServiceGrpc

@RestController
@RequestMapping("/api/rule/next-bid")
class NextBidController(
    @GrpcClient("nextBidService")
    private val nextBidService: NextBidServiceGrpc.NextBidServiceBlockingStub
) {
    @PostMapping
    fun register(@RequestBody body: NextBidRequest): NextBidResponse {
        return nextBidService.nextBid(body)
    }
}