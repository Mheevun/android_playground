package org.mhee.playground.diffcallback

import android.support.v7.util.DiffUtil

abstract class DiffCallBack<T>: DiffUtil.Callback() {
    protected var oldList: List<T> = ArrayList()
    protected var newList: List<T> = ArrayList()

    fun setData(oldList: List<T>, newList: List<T>) {
        this.oldList = oldList
        this.newList = newList
    }

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

}