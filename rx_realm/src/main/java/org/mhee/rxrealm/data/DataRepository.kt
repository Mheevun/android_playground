package org.mhee.rxrealm.data

import io.reactivex.Completable
import io.realm.RealmObject

class DataRepository (val realmFactory: RealmFactory) {
    val TAG: String? = DataRepository::class.simpleName

    val realm by lazy {
        realmFactory.createRealm()
    }

    inline fun <reified T : RealmObject> insert(crossinline insertFunction: (T) -> Unit): Completable {
        val realmInsert = realmFactory.createRealm()
        return realmInsert
                .transaction<T> { data -> insertFunction(data) }
                .doOnTerminate {
                    println("close realm insert")
                    realmInsert.close()
                }
    }

    inline fun <reified T : RealmObject> insertAsync(crossinline insertFunction: (T) -> Unit): Completable {
        return realm.transactionAsync<T> { data -> insertFunction(data) }
    }


    fun close(){
        realm.close()
    }
}
