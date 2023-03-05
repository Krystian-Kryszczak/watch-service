package app.service.blob.media.image

import app.service.blob.media.AzureMediaService
import com.azure.storage.blob.BlobServiceClient
import jakarta.inject.Singleton

@Singleton
class AzureImageService(blobServiceClient: BlobServiceClient): AzureMediaService(blobServiceClient, "image"), ImageService
