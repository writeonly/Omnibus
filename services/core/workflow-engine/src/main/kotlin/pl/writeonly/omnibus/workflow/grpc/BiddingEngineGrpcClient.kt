package pl.writeonly.omnibus.workflow.grpc

import net.devh.boot.grpc.client.inject.GrpcClient
import org.springframework.stereotype.Component
import pl.writeonly.omnibus.grpc.bidding.v1.BiddingServiceGrpc
import pl.writeonly.omnibus.grpc.bidding.v1.ManagedRuleDefinition
import pl.writeonly.omnibus.grpc.bidding.v1.ManagedRuleUpsertRequest

@Component
class BiddingEngineGrpcClient(
    @GrpcClient("bidding-engine")
    private val biddingService: BiddingServiceGrpc.BiddingServiceBlockingStub,
) {
    fun saveManagedRule(name: String, content: String): ManagedRuleDefinition =
        biddingService.saveManagedRule(
            ManagedRuleUpsertRequest.newBuilder()
                .setName(name)
                .setContent(content)
                .build(),
        )
}
