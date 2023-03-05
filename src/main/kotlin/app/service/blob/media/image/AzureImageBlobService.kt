package app.service.blob.media.image

import app.service.blob.media.AzureMediaBlobService
import com.azure.storage.blob.BlobServiceClient
import jakarta.inject.Singleton

@Singleton
class AzureImageBlobService(blobServiceClient: BlobServiceClient): AzureMediaBlobService(blobServiceClient, "image"), ImageBlobService
