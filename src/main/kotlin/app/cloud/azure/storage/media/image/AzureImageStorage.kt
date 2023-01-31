package app.cloud.azure.storage.media.image

import app.cloud.azure.storage.AzureStorage
import app.storage.blob.media.image.ImageStorage
import com.azure.storage.blob.BlobServiceClient
import jakarta.inject.Singleton
import app.cloud.Cloud.MediaType.Image

@Singleton
class AzureImageStorage(blobServiceClient: BlobServiceClient): ImageStorage, AzureStorage(blobServiceClient, Image)
