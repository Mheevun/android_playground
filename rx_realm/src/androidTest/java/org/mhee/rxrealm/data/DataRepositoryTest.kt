package org.mhee.rxrealm.data

import android.content.Context
import android.support.test.rule.ActivityTestRule
import io.realm.Realm
import io.realm.RealmConfiguration
import org.assertj.core.api.Assertions
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mhee.rxrealm.main.MainActivity
import org.mhee.rxrealm.model.DummyRealm

/**
 * Created by cnr on 4/7/2017.
 */
class DataRepositoryTest {
//    private val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
//    @Rule @JvmField
//    val uiThreadTestRule = UiThreadTestRule()

    @Rule @JvmField
    val mActivityRule = ActivityTestRule(MainActivity::class.java)
    lateinit var dataRepository: DataRepository
    lateinit var config:RealmConfiguration


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
        config = getConfiguration(mActivityRule.activity)
        println("config: "+config)
        dataRepository = DataRepository(RealmFactory(config))
    }

    @After
    fun after() {
        dataRepository.close()
        Realm.deleteRealm(config)
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
    }

    @Test
    fun could_observe_insert() {
        val insertSubscriber = dataRepository
                .realm
                .where(DummyRealm::class.java).findAll()
                .observeInsert()
                .test()
        insertSubscriber.assertNoErrors()
        insertSubscriber.assertNoValues()
        println("validate that repository is empty complete")

        val insertObserver = dataRepository
                .insert<DummyRealm> { it.value = "12345" }
                .test()
        insertObserver.awaitTerminalEvent()
        insertObserver.assertComplete()
        insertObserver.dispose()
        Assertions.assertThat(insertObserver.isDisposed).isTrue()
        println("insert data complete")

        insertSubscriber.assertValueCount(1)
    }

}