package pl.writeonly.omnibus.gateway.controller.workflow

import net.devh.boot.grpc.client.inject.GrpcClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.writeonly.omnibus.grpc.workflow.restbidding.v1.RestBiddingRequest
import pl.writeonly.omnibus.grpc.workflow.restbidding.v1.RestBiddingResponse
import pl.writeonly.omnibus.grpc.workflow.restbidding.v1.RestBiddingServiceGrpc

@RestController
@RequestMapping("/api/workflow/rest-bidding")
class RestBiddingController(
    @GrpcClient("restBiddingService")
    private val restBiddingService: RestBiddingServiceGrpc.RestBiddingServiceBlockingStub
) {
    @PostMapping
    fun register(@RequestBody body: RestBiddingRequest): RestBiddingResponse {
        return restBiddingService.restBiddingBid(body)
    }

}