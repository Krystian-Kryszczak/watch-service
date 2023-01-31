package app.cloud.gcp.service.media.audio

import app.service.blob.media.AbstractMediaBlobService
import app.service.blob.media.audio.AudioBlobService
import app.storage.blob.media.audio.AudioStorage
import jakarta.inject.Named
import jakarta.inject.Singleton

@Singleton
class GoogleCloudAudioBlobService(@Named("googlecloud") audioStorage: AudioStorage): AbstractMediaBlobService(audioStorage), AudioBlobService
