package pl.writeonly.omnibus.workflow.grpc

import net.devh.boot.grpc.client.inject.GrpcClient
import org.springframework.stereotype.Component
import pl.writeonly.omnibus.grpc.workflow.restbidding.v1.RestBiddingServiceGrpc
import pl.writeonly.omnibus.grpc.workflow.restbidding.v1.ManagedRuleDefinition
import pl.writeonly.omnibus.grpc.workflow.restbidding.v1.ManagedRuleUpsertRequest

@Component
class BiddingEngineGrpcClient(
    @GrpcClient("rule-service")
    private val biddingService: RestBiddingServiceGrpc.RestBiddingServiceBlockingStub,
) {
    fun saveManagedRule(name: String, content: String): ManagedRuleDefinition =
        biddingService.saveManagedRule(
            ManagedRuleUpsertRequest.newBuilder()
                .setName(name)
                .setContent(content)
                .build(),
        )
}
