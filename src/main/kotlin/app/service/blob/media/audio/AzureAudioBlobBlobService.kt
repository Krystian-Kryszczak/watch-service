package app.service.blob.media.audio

import app.service.blob.media.AzureMediaBlobService
import com.azure.storage.blob.BlobServiceClient
import jakarta.inject.Singleton

@Singleton
class AzureAudioBlobBlobService(blobServiceClient: BlobServiceClient): AzureMediaBlobService(blobServiceClient, "audio"), AudioBlobBlobService
