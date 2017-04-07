package org.mhee.rxrealm.data

import io.reactivex.Completable
import io.reactivex.Flowable
import io.realm.Realm
import io.realm.RealmObject
import io.realm.RealmResults

class DataRepository (val realmFactory: RealmFactory) {
    private val TAG: String? = DataRepository::class.simpleName

    val realm by lazy {
        realmFactory.createRealm()
    }

    inline fun <reified T : RealmObject> insert(crossinline insertFunction: (T) -> Unit): Completable {
        val realmInsert = realmFactory.createRealm()
        return realmInsert
                .transaction<T> { data -> insertFunction(data) }
                .doOnTerminate { realmInsert.close() }
    }

    inline fun <reified T : RealmObject> insertAsync(crossinline insertFunction: (T) -> Unit): Completable {
        return realm.transactionAsync<T> { data -> insertFunction(data) }
    }

    inline fun <reified T : RealmObject> getAndObserveInsert(crossinline queryFunction: (Realm) -> RealmResults<T>): Flowable<T> {
        return queryFunction(realm).observeInsert()
    }

    fun close(){
        realm.close()
    }
}
