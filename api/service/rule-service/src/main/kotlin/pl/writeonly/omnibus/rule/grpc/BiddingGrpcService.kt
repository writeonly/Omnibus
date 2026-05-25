package pl.writeonly.omnibus.rule.grpc

import io.grpc.stub.StreamObserver
import net.devh.boot.grpc.server.service.GrpcService
import pl.writeonly.omnibus.rule.application.ManagedRuleAdminService
import pl.writeonly.omnibus.rule.application.RestBiddingService
import pl.writeonly.omnibus.grpc.rule.nextbid.v1.ListManagedRulesRequest
import pl.writeonly.omnibus.grpc.rule.nextbid.v1.ListManagedRulesResponse
import pl.writeonly.omnibus.grpc.rule.nextbid.v1.ManagedRuleDefinition
import pl.writeonly.omnibus.grpc.rule.nextbid.v1.ManagedRuleUpsertRequest
import pl.writeonly.omnibus.grpc.rule.nextbid.v1.NextBidRequest
import pl.writeonly.omnibus.grpc.rule.nextbid.v1.NextBidResponse
import pl.writeonly.omnibus.grpc.rule.nextbid.v1.NextBidServiceGrpc

@GrpcService
class BiddingGrpcService(
    private val biddingRecommendationService: RestBiddingService,
    private val managedRuleAdminService: ManagedRuleAdminService,
) : NextBidServiceGrpc.NextBidServiceImplBase() {
    override fun nextBid(
        request: NextBidRequest,
        responseObserver: StreamObserver<NextBidResponse>,
    ) {
        responseObserver.completeWith {
            biddingRecommendationService.recommend(request.toDomain()).toGrpc()
        }
    }

    override fun listManagedRules(
        request: ListManagedRulesRequest,
        responseObserver: StreamObserver<ListManagedRulesResponse>,
    ) {
        responseObserver.completeWith {
            ListManagedRulesResponse.newBuilder()
                .addAllRules(managedRuleAdminService.listRules().map { it.toGrpc() })
                .build()
        }
    }

    override fun saveManagedRule(
        request: ManagedRuleUpsertRequest,
        responseObserver: StreamObserver<ManagedRuleDefinition>,
    ) {
        responseObserver.completeWith {
            managedRuleAdminService.saveRule(request.toDomain()).toGrpc()
        }
    }

    private fun <T> StreamObserver<T>.completeWith(block: () -> T) {
        try {
            onNext(block())
            onCompleted()
        } catch (exception: RuntimeException) {
            onError(exception)
        }
    }
}
