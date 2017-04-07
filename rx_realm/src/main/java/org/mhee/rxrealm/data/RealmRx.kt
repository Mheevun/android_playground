package org.mhee.rxrealm.data

import android.util.Log
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Flowable
import io.realm.OrderedRealmCollectionChangeListener
import io.realm.Realm
import io.realm.RealmObject
import io.realm.RealmResults

val TAG:String = "RealmRx"
inline fun <reified T : RealmObject> Realm.transactionAsync(crossinline insertFunction: (T) -> Unit): Completable {
    return Completable.create {
        emitter ->
        executeTransactionAsync({
            realm ->
            Log.v(TAG, "execute transaction")
            insertFunction(realm.createObject(T::class.java))
        }, {
            Log.v(TAG, "execute transaction result: success")
            emitter.onComplete()
        }, {
            transactionError ->
            Log.v(TAG, "execute transaction result: error")
            emitter.onError(transactionError)
        })
    }
}

inline fun <reified T : RealmObject> Realm.transaction(crossinline insertFunction: (T) -> Unit): Completable {
    return Completable.fromCallable {
        executeTransaction{ realm ->
            insertFunction(realm.createObject(T::class.java))
        }
        println("Realm.transaction complete")
    }
}

inline fun <reified T : RealmObject> RealmResults<T>.observeInsert(): Flowable<T> {
    return Flowable.create({
        emitter ->
        Log.v(TAG, "start observeInsert with result count: $size")
        for (result in this) {
            Log.v(TAG, "emit stored result (not live): $result")
            emitter.onNext(result)
        }

        val callback = OrderedRealmCollectionChangeListener<RealmResults<T>> {
            realmResult, changeSet ->
            Log.v(TAG, "receive changeSet: $changeSet")
            for(range in changeSet.insertionRanges){
                val lastInsertIndex = range.startIndex + range.length - 1
                for(insetIndex in range.startIndex..lastInsertIndex){
                    Log.d(TAG, "emit new inserted result: ${realmResult[insetIndex]}")
                    emitter.onNext(realmResult[insetIndex])
                }
            }
        }
        addChangeListener(callback)
        emitter.setCancellable {
            Log.v("Realm", "remove change listener")
            removeChangeListener(callback)
        }
    }, BackpressureStrategy.BUFFER)
}