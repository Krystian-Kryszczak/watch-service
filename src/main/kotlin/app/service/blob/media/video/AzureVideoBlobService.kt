package app.service.blob.media.video

import app.service.blob.media.AzureMediaBlobService
import com.azure.storage.blob.BlobServiceClient
import io.micronaut.context.annotation.Primary
import jakarta.inject.Singleton

@Primary
@Singleton
class AzureVideoBlobService(blobServiceClient: BlobServiceClient): AzureMediaBlobService(blobServiceClient, "video"), VideoBlobService
