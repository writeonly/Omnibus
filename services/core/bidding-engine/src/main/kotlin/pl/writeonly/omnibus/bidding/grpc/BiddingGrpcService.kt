package pl.writeonly.omnibus.bidding.grpc

import io.grpc.stub.StreamObserver
import net.devh.boot.grpc.server.service.GrpcService
import pl.writeonly.omnibus.bidding.application.ManagedRuleAdminService
import pl.writeonly.omnibus.bidding.application.RestBiddingService
import pl.writeonly.omnibus.grpc.bidding.v1.BiddingServiceGrpc
import pl.writeonly.omnibus.grpc.bidding.v1.ListManagedRulesRequest
import pl.writeonly.omnibus.grpc.bidding.v1.ListManagedRulesResponse
import pl.writeonly.omnibus.grpc.bidding.v1.ManagedRuleDefinition
import pl.writeonly.omnibus.grpc.bidding.v1.ManagedRuleUpsertRequest
import pl.writeonly.omnibus.grpc.bidding.v1.RecommendationRequest
import pl.writeonly.omnibus.grpc.bidding.v1.RecommendationResponse

@GrpcService
class BiddingGrpcService(
    private val biddingRecommendationService: RestBiddingService,
    private val managedRuleAdminService: ManagedRuleAdminService,
) : BiddingServiceGrpc.BiddingServiceImplBase() {
    override fun recommendBid(
        request: RecommendationRequest,
        responseObserver: StreamObserver<RecommendationResponse>,
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
