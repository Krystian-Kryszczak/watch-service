package app.cloud.azure.factory

import com.azure.core.credential.TokenCredential
import com.azure.storage.blob.BlobServiceClient
import com.azure.storage.blob.BlobServiceClientBuilder
import io.micronaut.context.annotation.Factory
import io.micronaut.core.annotation.NonNull
import jakarta.inject.Singleton

@Factory
class AzureFactory {
    @Singleton
    fun blobServiceClient(@NonNull tokenCredential: TokenCredential): BlobServiceClient {
        return BlobServiceClientBuilder()
            .credential(tokenCredential)
            .endpoint(System.getenv("AZURE_BLOB_ENDPOINT"))
            .buildClient()
    }
}
