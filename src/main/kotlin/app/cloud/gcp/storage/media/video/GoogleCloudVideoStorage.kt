package app.cloud.gcp.storage.media.video

import app.storage.blob.media.image.ImageStorage
import app.cloud.gcp.storage.GoogleCloudStorage
import app.cloud.Cloud.MediaType.Video
import com.google.cloud.storage.Storage
import jakarta.inject.Singleton

@Singleton
class GoogleCloudVideoStorage(storage: Storage): GoogleCloudStorage(storage, Video), ImageStorage
