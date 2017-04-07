package org.mhee.rxrealm.application

import android.app.Application

class RxRealmApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        val realmInit = RealmInitializer()
        realmInit.initRealm(this)
        realmInit.initRealmDefaultConfig("test.realm")
    }


}