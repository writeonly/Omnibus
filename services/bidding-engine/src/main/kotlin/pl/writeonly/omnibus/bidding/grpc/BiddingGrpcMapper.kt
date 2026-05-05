package pl.writeonly.omnibus.bidding.grpc

import pl.writeonly.omnibus.bidding.domain.ManagedRuleDefinition
import pl.writeonly.omnibus.bidding.domain.ManagedRuleUpsertRequest
import pl.writeonly.omnibus.bidding.domain.RecommendationRequest
import pl.writeonly.omnibus.bidding.domain.RecommendationResponse
import pl.writeonly.omnibus.bidding.rules.CandidateBid
import pl.writeonly.omnibus.grpc.bidding.v1.CandidateBid as GrpcCandidateBid
import pl.writeonly.omnibus.grpc.bidding.v1.ManagedRuleDefinition as GrpcManagedRuleDefinition
import pl.writeonly.omnibus.grpc.bidding.v1.ManagedRuleUpsertRequest as GrpcManagedRuleUpsertRequest
import pl.writeonly.omnibus.grpc.bidding.v1.RecommendationRequest as GrpcRecommendationRequest
import pl.writeonly.omnibus.grpc.bidding.v1.RecommendationResponse as GrpcRecommendationResponse

fun GrpcRecommendationRequest.toDomain(): RecommendationRequest =
    RecommendationRequest(
        northHand = northHand,
        southHand = southHand,
        auction = auction,
        system = system,
    )

fun RecommendationResponse.toGrpc(): GrpcRecommendationResponse =
    GrpcRecommendationResponse.newBuilder()
        .setSystem(system)
        .setEvaluatedSeat(evaluatedSeat)
        .setNorthHand(northHand)
        .setSouthHand(southHand)
        .setAuction(auction)
        .setRecommendedBid(recommendedBid)
        .setExplanation(explanation)
        .addAllCandidates(candidates.map { it.toGrpc() })
        .build()

fun CandidateBid.toGrpc(): GrpcCandidateBid =
    GrpcCandidateBid.newBuilder()
        .setBid(bid)
        .setPriority(priority)
        .setReason(reason)
        .build()

fun GrpcManagedRuleUpsertRequest.toDomain(): ManagedRuleUpsertRequest =
    ManagedRuleUpsertRequest(
        name = name,
        content = content,
    )

fun ManagedRuleDefinition.toGrpc(): GrpcManagedRuleDefinition =
    GrpcManagedRuleDefinition.newBuilder()
        .setName(name)
        .setSourcePath(sourcePath)
        .setManaged(managed)
        .setContent(content)
        .build()
