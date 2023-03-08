package app.config.grpc.mailer

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldNotContain
import io.kotest.matchers.shouldBe
import io.micronaut.context.ApplicationContext
import io.micronaut.test.extensions.kotest.annotation.MicronautTest

@MicronautTest
class MailerGrpcClientConfigTest(private val grpcMailerChannelConfig: GrpcMailerChannelConfig) : StringSpec({

    "mailer grpc client configuration values should not contain null" {
        listOf(
            grpcMailerChannelConfig.forAddress,
            grpcMailerChannelConfig.plaintext,
            grpcMailerChannelConfig.maxRetryAttempts
        ) shouldNotContain null
    }

    "mailer grpc client configuration values should be the same as specified in the application context" {
        val prefix = "grpc.channels.mailer"

        val items: MutableMap<String, Any> = HashMap()
        items["${prefix}.for-address"] = "127.0.0.1:50011"
        items["${prefix}.plaintext"] = true
        items["${prefix}.max-retry-attempts"] = 10

        val ctx = ApplicationContext.run(items)
        val grpcMailerConfig = ctx.getBean(GrpcMailerChannelConfig::class.java)

        grpcMailerConfig.forAddress shouldBe "127.0.0.1:50011"
        grpcMailerConfig.plaintext shouldBe true
        grpcMailerConfig.maxRetryAttempts shouldBe 10

        ctx.close()
    }
})
