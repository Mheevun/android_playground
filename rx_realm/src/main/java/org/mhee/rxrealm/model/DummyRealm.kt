package org.mhee.rxrealm.model

import io.realm.RealmObject

@AllOpen
open class DummyRealm(var value:String? = null) :RealmObject()