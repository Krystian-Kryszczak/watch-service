package app.cloud.azure.service.media.audio

import app.service.blob.media.AbstractMediaBlobService
import app.service.blob.media.audio.AudioBlobService
import app.storage.blob.media.audio.AudioStorage
import jakarta.inject.Named
import jakarta.inject.Singleton
import java.util.*

@Singleton
class AzureAudioBlobService(@Named("azure") audioStorage: AudioStorage): AbstractMediaBlobService(audioStorage), AudioBlobService
