package com.syncup.domain.model

import java.util.UUID

/**
 * Domain model for shared files/resources
 */
data class SharedFile(
    val id: String = UUID.randomUUID().toString(),
    val projectId: String,
    val fileName: String,
    val fileSize: Long,
    val fileType: String, // MIME type
    val downloadUrl: String,
    val cloudProvider: CloudProvider = CloudProvider.GOOGLE_DRIVE,
    val uploadedBy: String, // userId
    val uploadedAt: Long = System.currentTimeMillis(),
    val versions: List<FileVersion> = emptyList(),
    val lastModified: Long = System.currentTimeMillis(),
    val isArchived: Boolean = false
)

enum class CloudProvider {
    GOOGLE_DRIVE, ONEDRIVE, DROPBOX, LOCAL
}

/**
 * File version tracking for resource versioning
 */
data class FileVersion(
    val id: String = UUID.randomUUID().toString(),
    val version: Int,
    val fileName: String,
    val downloadUrl: String,
    val uploadedBy: String,
    val uploadedAt: Long = System.currentTimeMillis(),
    val changeDescription: String? = null
)

/**
 * Resource bar item - quick links to documents
 */
data class ResourceLink(
    val id: String = UUID.randomUUID().toString(),
    val projectId: String,
    val title: String,
    val url: String,
    val icon: String? = null,
    val category: ResourceCategory = ResourceCategory.BRIEF,
    val addedBy: String, // userId
    val addedAt: Long = System.currentTimeMillis()
)

enum class ResourceCategory {
    BRIEF, UI_KIT, SPREADSHEET, DOCUMENT, LINK, OTHER
}
