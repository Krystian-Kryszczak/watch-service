package app.factory.azure

import app.config.azure.AzureBlobConfig
import com.azure.core.credential.TokenCredential
import com.azure.storage.blob.BlobServiceClient
import com.azure.storage.blob.BlobServiceClientBuilder
import io.micronaut.context.annotation.Factory
import io.micronaut.core.annotation.NonNull
import jakarta.inject.Singleton

@Factory
class AzureFactory(private val azureBlobConfig: AzureBlobConfig) {
    @Singleton
    fun blobServiceClient(@NonNull tokenCredential: TokenCredential): BlobServiceClient {
        return BlobServiceClientBuilder()
            .credential(tokenCredential)
            .endpoint(azureBlobConfig.endpoint)
            .buildClient()
    }
}
