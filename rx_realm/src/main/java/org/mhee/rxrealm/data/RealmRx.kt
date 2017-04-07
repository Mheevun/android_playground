package org.mhee.rxrealm.data

import android.util.Log
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Flowable
import io.realm.OrderedRealmCollectionChangeListener
import io.realm.Realm
import io.realm.RealmObject
import io.realm.RealmResults

val TAG: String = "RealmRx"
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
        executeTransaction { realm ->
            insertFunction(realm.createObject(T::class.java))
        }
        println("Realm.transaction complete")
    }
}

inline fun <reified T : RealmObject> RealmResults<T>.asFlowable(): Flowable<T> {
    return Flowable.create({
        emitter ->
        Log.v(TAG, "start asFlowable with result count: $size")
        for (result in this) {
            if (!emitter.isCancelled) {
                Log.v(TAG, "emit stored result (not live): $result")
                emitter.onNext(result)
            }
        }
        emitter.onComplete()
    }, BackpressureStrategy.BUFFER)
}

inline fun <reified T : RealmObject> RealmResults<T>.observeInsert(): Flowable<T> {
    return Flowable.create({
        emitter ->
        try {
            if (!emitter.isCancelled) {
                val callback = createChangeListener { emitter.onNext(it) }
                Log.v(TAG, "addChangeListener")
                addChangeListener(callback)
                emitter.setCancellable {
                    Log.v(TAG, "removeChangeListener")
                    removeChangeListener(callback)
                }
            }
        } catch (ex: Exception) {
            emitter.onError(ex)
        }
    }, BackpressureStrategy.BUFFER)
}

inline fun <reified T : RealmObject> RealmResults<T>.createChangeListener(crossinline insertCallback: (T) -> Unit): OrderedRealmCollectionChangeListener<RealmResults<T>> {
    return OrderedRealmCollectionChangeListener {
        realmResult, changeSet ->
        Log.v(TAG, "receive changeSet: $changeSet")
        for (range in changeSet.insertionRanges) {
            val lastInsertIndex = range.startIndex + range.length - 1
            for (insetIndex in range.startIndex..lastInsertIndex) {
                Log.d(TAG, "emit new inserted result: ${realmResult[insetIndex]}")
                insertCallback(realmResult[insetIndex])
            }
        }
    }
}
