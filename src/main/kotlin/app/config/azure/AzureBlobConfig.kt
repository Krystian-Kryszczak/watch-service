package app.config.azure

import io.micronaut.context.annotation.ConfigurationProperties

@ConfigurationProperties("azure.blob")
class AzureBlobConfig {
    var endpoint: String? = null
}
