package app.service.blob.media.video

import app.service.blob.media.AzureMediaBlobService
import com.azure.storage.blob.BlobServiceClient
import jakarta.inject.Singleton

@Singleton
class AzureVideoBlobService(blobServiceClient: BlobServiceClient): AzureMediaBlobService(blobServiceClient, "video"), VideoBlobService
