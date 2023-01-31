package app.rpc

import app.MailerServiceGrpc
import io.grpc.ManagedChannelBuilder
import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton

@Factory
class GrpcClients {
    @Singleton
    fun mailer(): MailerServiceGrpc.MailerServiceStub {
        val channel = ManagedChannelBuilder.forAddress("127.0.0.1", 50011).usePlaintext().build()
        return MailerServiceGrpc.newStub(channel)
    }
//    @Singleton
//    fun mailer(@GrpcChannel("mailer") channel: ManagedChannel): MailerServiceGrpc.MailerServiceStub = MailerServiceGrpc.newStub(channel)
}
