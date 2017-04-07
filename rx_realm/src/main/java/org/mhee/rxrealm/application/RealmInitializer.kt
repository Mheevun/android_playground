package org.mhee.rxrealm.application

import android.content.Context
import io.realm.Realm
import io.realm.RealmConfiguration

/**
 * Created by cnr on 4/6/2017.
 */
class RealmInitializer {

    fun initRealm(context:Context){
        Realm.init(context)
    }

    fun initRealmDefaultConfig(name:String){
        val config = RealmConfiguration.Builder()
                .name(name)
                .schemaVersion(5)
                .deleteRealmIfMigrationNeeded()
                .build()
        Realm.setDefaultConfiguration(config)
    }
}