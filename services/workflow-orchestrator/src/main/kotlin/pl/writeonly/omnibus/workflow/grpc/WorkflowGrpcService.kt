package pl.writeonly.omnibus.workflow.grpc

import io.grpc.stub.StreamObserver
import net.devh.boot.grpc.server.service.GrpcService
import pl.writeonly.omnibus.workflow.application.RulePublicationWorkflowService
import pl.writeonly.omnibus.grpc.workflow.v1.RulePublicationRequest
import pl.writeonly.omnibus.grpc.workflow.v1.RulePublicationSubmission
import pl.writeonly.omnibus.grpc.workflow.v1.WorkflowServiceGrpc

@GrpcService
class WorkflowGrpcService(
    private val rulePublicationWorkflowService: RulePublicationWorkflowService,
) : WorkflowServiceGrpc.WorkflowServiceImplBase() {
    override fun startRulePublication(
        request: RulePublicationRequest,
        responseObserver: StreamObserver<RulePublicationSubmission>,
    ) {
        responseObserver.completeWith {
            rulePublicationWorkflowService.startPublication(request.toDomain()).toGrpc()
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
