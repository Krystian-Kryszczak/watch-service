package app.cloud.gcp

import app.cloud.Cloud
import jakarta.inject.Singleton

@Singleton
class GoogleCloud(override val name: String = "Google Cloud") : Cloud
