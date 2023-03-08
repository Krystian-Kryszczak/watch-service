package app.config.grpc.mailer

import io.micronaut.context.annotation.ConfigurationProperties

@ConfigurationProperties("grpc.channels.mailer")
class GrpcMailerChannelConfig {
    var forAddress: String? = null
    var plaintext: Boolean? = null
    var maxRetryAttempts: Int? = null
}
