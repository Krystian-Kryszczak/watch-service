package app.service.blob.media.video

import app.service.blob.media.AzureMediaService
import com.azure.storage.blob.BlobServiceClient
import jakarta.inject.Singleton

@Singleton
class AzureVideoService(blobServiceClient: BlobServiceClient): AzureMediaService(blobServiceClient, "video"), VideoService
