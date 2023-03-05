package app.service.blob.media.audio

import app.service.blob.media.LocalMediaService
import jakarta.inject.Singleton

@Singleton
class LocalAudioService: LocalMediaService("audio"), AudioService
