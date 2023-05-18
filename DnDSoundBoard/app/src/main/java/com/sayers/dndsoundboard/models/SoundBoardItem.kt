package com.sayers.dndsoundboard.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.UUID

open class SoundBoardItem: RealmObject() {
    @PrimaryKey
    var id = UUID.randomUUID().toString()
    var name: String = "Test name"
    var image: String = ""
    var sound: String = ""
}