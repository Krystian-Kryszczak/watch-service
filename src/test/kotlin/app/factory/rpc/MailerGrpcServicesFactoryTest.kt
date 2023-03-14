package app.factory.rpc

import app.PopMailerServiceGrpc
import app.SmtpMailerServiceGrpc
import io.grpc.ManagedChannel
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldNotBe
import io.micronaut.test.extensions.kotest.annotation.MicronautTest
import jakarta.inject.Named

@MicronautTest
class MailerGrpcServicesFactoryTest(
    @Named("mailer") private val mailerChannel: ManagedChannel,
    private val smtpMailerService: SmtpMailerServiceGrpc.SmtpMailerServiceStub,
    private val popMailerService: PopMailerServiceGrpc.PopMailerServiceStub
): StringSpec({

    "mailer chanel inject test" {
        mailerChannel shouldNotBe null
    }

    "smtp mailer service inject test" {
        smtpMailerService shouldNotBe null
    }

    "pop mailer service inject test" {
        popMailerService shouldNotBe null
    }
})
