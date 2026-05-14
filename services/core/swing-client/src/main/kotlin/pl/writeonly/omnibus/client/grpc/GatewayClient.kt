package pl.writeonly.omnibus.client.grpc
import io.grpc.ManagedChannelBuilder

class GatewayClient {

    private val channel = ManagedChannelBuilder
        .forAddress("localhost", 9090)
        .usePlaintext()
        .build()

    private val stub = GatewayGrpc.newBlockingStub(channel)

    fun callRuleEngine(): String {
        val request = RuleRequest.newBuilder()
            .setInput("test")
            .build()

        val response = stub.evaluateRule(request)
        return response.result
    }
}