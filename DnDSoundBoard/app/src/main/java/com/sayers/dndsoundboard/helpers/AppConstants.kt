package com.sayers.dndsoundboard.helpers

import android.content.Context
import java.util.*

object AppConstants {
    // supported file formats
    @JvmField
    val FILE_EXTN = listOf("jpg", "jpeg", "png")
    const val IMAGES_SUBDIRECTORY = "images"
    const val FILE_PROVIDER_DOMAIN_AUTHORITY = "com.auditcomply.fileprovider"
    const val SYNCING_NOTIFICATION_ID = 0
    const val UPLOADING_NOTIFICATION_ID = 1

    fun getMSALConfigPath (activity: Context): String {
       return "/data/data/${activity.packageName}/files/data.json"
    }
}