package app.config.grpc.mailer

import io.micronaut.context.annotation.Configuration
import io.micronaut.context.annotation.ConfigurationProperties

@ConfigurationProperties("grpc.channels.mailer")
class MailerGrpcClient(
    plaintext: Boolean? = null
)
