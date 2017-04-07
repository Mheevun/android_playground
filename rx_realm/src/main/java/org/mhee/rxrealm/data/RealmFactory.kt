package org.mhee.rxrealm.data

import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.realm.Realm
import io.realm.RealmConfiguration

class RealmFactory(var realmConfiguration: RealmConfiguration? = null) {
    val realmOnUI: Flowable<Realm> by lazy {
        val realm = createRealm()
        Flowable.defer { Flowable.just(realm) }
                .subscribeOn(AndroidSchedulers.mainThread())
//                .doOnTerminate { realm.close() }
                .share()
    }

    fun createRealm(): Realm {
        if (realmConfiguration == null)
            return Realm.getDefaultInstance()
        else
            return Realm.getInstance(realmConfiguration)
    }

}