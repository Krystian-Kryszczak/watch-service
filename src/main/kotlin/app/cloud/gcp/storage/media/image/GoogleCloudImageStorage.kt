package app.cloud.gcp.storage.media.image

import app.storage.blob.media.image.ImageStorage
import app.cloud.gcp.storage.GoogleCloudStorage
import app.cloud.Cloud.MediaType.Image
import com.google.cloud.storage.Storage
import jakarta.inject.Singleton

@Singleton
class GoogleCloudImageStorage(storage: Storage): GoogleCloudStorage(storage, Image), ImageStorage
