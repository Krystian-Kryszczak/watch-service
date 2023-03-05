package app.service.blob.media.audio

import app.service.blob.media.AzureMediaService
import com.azure.storage.blob.BlobServiceClient
import jakarta.inject.Singleton

@Singleton
class AzureAudioService(blobServiceClient: BlobServiceClient): AzureMediaService(blobServiceClient, "audio"), AudioService
