package pl.writeonly.omnibus.gateway.controller.workflow

//import net.devh.boot.grpc.client.inject.GrpcClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
//import pl.writeonly.omnibus.gateway.controller.grpcMono
//import pl.writeonly.omnibus.grpc.workflow.restbidding.v1.RestBiddingRequest
//import pl.writeonly.omnibus.grpc.workflow.restbidding.v1.RestBiddingResponse
//import pl.writeonly.omnibus.grpc.workflow.restbidding.v1.RestBiddingServiceGrpc
import reactor.core.publisher.Mono

// @RestController
// @RequestMapping("/api/workflow/rest-bidding")
// class RestBiddingController(
//     @GrpcClient("restBiddingService")
//     private val stub: RestBiddingServiceGrpc.RestBiddingServiceStub
// ) {
//     @PostMapping
//     fun restBiddingBid(@RequestBody body: RestBiddingRequest): Mono<RestBiddingResponse> {
//         return grpcMono { observer ->
//             stub.restBidding(body, observer)
//         }
//     }
// }