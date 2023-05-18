package com.sayers.dndsoundboard.models
import io.realm.DynamicRealm
import io.realm.RealmMigration
import java.util.Locale

class Migration : RealmMigration{
    override  fun migrate(realm: DynamicRealm, oldVersion: Long, newVersion: Long){

        // DynamicRealm exposes an editable schema
        val schema = realm.schema

        // Migrate to version 1: Renaming Template property
//        if (oldVersion == 0L){
//            schema["Template"]
//                .renameField("hasRequirementCapas", "hasRequirementNonConformances")
//            oldVersion++
//        }

        check(oldVersion >= newVersion) { String.format(Locale.UK, "Migration missing from v%d to v%d. Did you forget to add oldVersion++?", oldVersion, newVersion) }
    }
}