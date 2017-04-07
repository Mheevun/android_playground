package org.mhee.rxrealm.main

import android.databinding.ObservableField
import android.util.Log
import android.view.View
import io.reactivex.disposables.CompositeDisposable
import org.mhee.rxrealm.data.DataRepository
import org.mhee.rxrealm.data.RealmFactory
import org.mhee.rxrealm.model.DummyRealm

class MainViewModel {
    private val TAG: String? = MainViewModel::class.simpleName
    val newValue = ObservableField<String>("add new data")
    val lastValue = ObservableField<String>()

    private val dataRepository = DataRepository(RealmFactory())
    private val disposes = CompositeDisposable()

    init {
        disposes.add(
                dataRepository.get {
                    realm ->
                    Log.v(TAG, "start get value")
                    realm.where(DummyRealm::class.java).findAll()
                }
                        .doOnNext {
                            Log.v(TAG, "got value: ${it.value}")
                            lastValue.set(it.value)
                        }
                        .subscribe()
        )
    }

    fun addNewValue() {
        disposes.add(
                dataRepository.insertAsync<DummyRealm> {
                    realmObject ->
                    Log.v(TAG, "add value: '${newValue.get()}'")
                    realmObject.value = newValue.get()
                    newValue.set("")
                }.subscribe()
        )
    }


    fun onFocusChange(view: View, hasFocus: Boolean) {
        Log.v(TAG, "hasFocus change: $hasFocus")
        if (hasFocus) newValue.set("")
    }

    fun close() {
        disposes.dispose()
        disposes.clear()
        dataRepository.close()
    }
}