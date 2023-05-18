package com.sayers.dndsoundboard

import android.app.Application
import com.sayers.dndsoundboard.models.Migration
import io.realm.Realm
import io.realm.RealmConfiguration

class DnDSoundBoardApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Create base DB
        Realm.init(this)
        val realmConfig = RealmConfiguration.Builder()
            .name("dndSoundBoard.realm")
            .schemaVersion(61)
            .allowWritesOnUiThread(true)
            .migration(Migration())
            .build()
        Realm.setDefaultConfiguration(realmConfig)
    }

    override fun onTerminate() {
        Realm.getDefaultInstance().close()
        super.onTerminate()
    }

    companion object {
        private const val TAG = "DnDSoundBoard"
    }
}