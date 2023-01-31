package app.cloud.azure.service.media.video

import app.service.blob.media.AbstractMediaBlobService
import app.service.blob.media.video.VideoBlobService
import app.storage.blob.media.video.VideoStorage
import jakarta.inject.Named
import jakarta.inject.Singleton

@Singleton
class AzureVideoBlobService(@Named("azure") videoStorage: VideoStorage): AbstractMediaBlobService(videoStorage), VideoBlobService
