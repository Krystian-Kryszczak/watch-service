package app.cloud.gcp.storage.media.audio

import app.storage.blob.media.audio.AudioStorage
import app.cloud.gcp.storage.GoogleCloudStorage
import app.cloud.Cloud.MediaType.Audio
import com.google.cloud.storage.Storage
import jakarta.inject.Singleton

@Singleton
class GoogleCloudAudioStorage(storage: Storage): GoogleCloudStorage(storage, Audio), AudioStorage
