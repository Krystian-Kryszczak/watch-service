package app.factory.rpc

import app.PopMailerServiceGrpc
import app.SmtpMailerServiceGrpc
import app.config.grpc.GrpcClientConfig
import app.config.grpc.mailer.GrpcMailerChannelConfig
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import io.micronaut.context.annotation.Factory
import jakarta.inject.Named
import jakarta.inject.Singleton

@Factory
class MailerGrpcServicesFactory(
    private val grpcClientConfig: GrpcClientConfig,
    private val grpcMailerChannelConfig: GrpcMailerChannelConfig
) {

    @Singleton
    @Named("mailer")
    fun mailerServiceManagedChannel(): ManagedChannel {
        val builder = ManagedChannelBuilder.forTarget(grpcMailerChannelConfig.forAddress)

        if (grpcMailerChannelConfig.plaintext == true)
            builder.usePlaintext()

        val mailerMaxRetryAttempts = grpcMailerChannelConfig.maxRetryAttempts
                ?: grpcClientConfig.maxRetryAttempts
        if (mailerMaxRetryAttempts != null)
            builder.maxRetryAttempts(mailerMaxRetryAttempts)

        return builder.build()
    }

    @Singleton
    fun smtpMailerServiceStub(@Named("mailer") channel: ManagedChannel): SmtpMailerServiceGrpc.SmtpMailerServiceStub =
            SmtpMailerServiceGrpc.newStub(channel)

    @Singleton
    fun popMailerServiceStub(@Named("mailer") channel: ManagedChannel): PopMailerServiceGrpc.PopMailerServiceStub =
            PopMailerServiceGrpc.newStub(channel)
}
