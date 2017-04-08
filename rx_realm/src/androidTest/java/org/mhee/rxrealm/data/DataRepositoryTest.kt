package org.mhee.rxrealm.data

import android.content.Context
import android.support.test.InstrumentationRegistry
import io.reactivex.subscribers.TestSubscriber
import io.realm.Realm
import io.realm.RealmConfiguration
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.mhee.rxrealm.model.DummyRealm

/**
 * Created by cnr on 4/7/2017.
 */
class DataRepositoryTest {
    private val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
    lateinit var dataRepository: DataRepository
    lateinit var config: RealmConfiguration

    fun getConfiguration(context: Context): RealmConfiguration {
        Realm.init(context)
        val realmConfiguration = RealmConfiguration.Builder()
                .name("test2.realm")
                .build()
        Realm.deleteRealm(realmConfiguration)
        return realmConfiguration
    }

    @Before
    fun setup() {
        config = getConfiguration(context)
        dataRepository = DataRepository(RealmFactory(config))
    }


    @Test
    fun could_insert() {
        val emptySubscriber = dataRepository
                .realm
                .where(DummyRealm::class.java).findAll()
                .asFlowable()
                .test()
        emptySubscriber.assertNoErrors()
        emptySubscriber.assertNoValues()
        emptySubscriber.dispose()
        emptySubscriber.awaitTerminalEvent()
        println("validate that repository is empty complete")

        val insertObserver = dataRepository
                .insert<DummyRealm> { it.value = "12345" }
                .test()
        insertObserver.awaitTerminalEvent()
        insertObserver.assertComplete()
        insertObserver.dispose()
        Assertions.assertThat(insertObserver.isDisposed).isTrue()
        println("insert data complete")

        val validateSubscriber = dataRepository
                .realm
                .where(DummyRealm::class.java).findAll()
                .asFlowable()
                .test()
        validateSubscriber.assertValueCount(1)
        validateSubscriber.dispose()
        validateSubscriber.awaitTerminalEvent()
        dataRepository.close()
    }

    @Test
    fun could_observe_insert() {
        var insertSubscriber:TestSubscriber<DummyRealm>? = null
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
             insertSubscriber =
                    dataRepository
                            .realm
                            .where(DummyRealm::class.java).findAll()
                            .observeInsert()
                            .test()
            insertSubscriber!!.assertNoErrors()
            insertSubscriber!!.assertNoValues()

            val insertObserver = dataRepository
                    .insert<DummyRealm> { it.value = "12345" }
                    .test()
            insertObserver.awaitTerminalEvent()
            insertObserver.assertComplete()
            insertObserver.dispose()
            Assertions.assertThat(insertObserver.isDisposed).isTrue()
        }

        //must wait till realm emit data on its listener
        InstrumentationRegistry.getInstrumentation().waitForIdleSync()

        //observer must be in main sync (UI thread)
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            insertSubscriber!!.assertNotTerminated()
            insertSubscriber!!.assertValueCount(1)
            insertSubscriber!!.dispose()
            insertSubscriber!!.cancel()
            insertSubscriber!!.onComplete()
            assertThat(insertSubscriber!!.isDisposed).isTrue()
            dataRepository.close()
        }
    }
}