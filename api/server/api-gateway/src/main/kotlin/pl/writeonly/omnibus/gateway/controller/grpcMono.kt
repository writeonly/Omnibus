package pl.writeonly.omnibus.gateway.controller

import io.grpc.stub.StreamObserver
import reactor.core.publisher.Mono

fun <T : Any> grpcMono(call: (StreamObserver<T>) -> Unit): Mono<T> {
    return Mono.create { sink ->
        call(object : StreamObserver<T> {

            override fun onNext(value: T) {
                sink.success(value)
            }

            override fun onError(t: Throwable) {
                sink.error(t)
            }

            override fun onCompleted() {
            }
        })
    }
}