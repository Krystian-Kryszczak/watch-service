package app.config.grpc

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldNotContain
import io.kotest.matchers.shouldBe
import io.micronaut.context.ApplicationContext
import io.micronaut.test.extensions.kotest.annotation.MicronautTest

@MicronautTest
class GrpcClientConfigTest(private val grpcClientConfig: GrpcClientConfig) : StringSpec({

    "grpc client config values should not contain null" {
        listOf(
            grpcClientConfig.maxRetryAttempts
        ) shouldNotContain null
    }

    "grpc client configuration values should be the same as specified in the application context" {
        val prefix = "grpc.client"

        val items: MutableMap<String, Any> = HashMap()
        items["${prefix}.max-retry-attempts"] = 10

        val ctx = ApplicationContext.run(items)
        val ctxGrpcClientConfig = ctx.getBean(GrpcClientConfig::class.java)

        ctxGrpcClientConfig.maxRetryAttempts shouldBe 10

        ctx.close()
    }
})
