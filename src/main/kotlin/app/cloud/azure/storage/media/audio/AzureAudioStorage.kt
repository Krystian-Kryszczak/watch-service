package app.cloud.azure.storage.media.audio

import app.cloud.azure.storage.AzureStorage
import app.storage.blob.media.audio.AudioStorage
import com.azure.storage.blob.BlobServiceClient
import jakarta.inject.Singleton
import app.cloud.Cloud.MediaType.Audio

@Singleton
class AzureAudioStorage(blobServiceClient: BlobServiceClient): AudioStorage, AzureStorage(blobServiceClient, Audio)
