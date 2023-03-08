package app.config.grpc

import io.micronaut.context.annotation.ConfigurationProperties

@ConfigurationProperties("grpc.client")
class GrpcClientConfig {
    var maxRetryAttempts: Int? = null
}
