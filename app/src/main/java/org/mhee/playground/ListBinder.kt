package org.mhee.playground

import android.support.v7.util.DiffUtil
import io.reactivex.android.MainThreadDisposable.verifyMainThread
import org.mhee.playground.diffcallback.DiffCallBack



/**
 * Created by cnr on 3/26/2017.
 */
class ListBinder<E>(private val diffCallBack: DiffCallBack<E>) {
    private var current: List<E> = ArrayList()
    private var onDataChangeListener: OnDataChangeListener? = null

    interface OnDataChangeListener {
        fun onChange(diffResult: DiffUtil.DiffResult)
    }

    fun setOnDataChangeListener(onDataChangeListener: OnDataChangeListener) {
        this.onDataChangeListener = onDataChangeListener
    }

    fun notifyDataChange(data: List<E>) {
        verifyMainThread()
        val diffResult = calculateDiff(data)
        if (onDataChangeListener != null) {
            onDataChangeListener!!.onChange(diffResult)
        }
    }

    private fun calculateDiff(data: List<E>): DiffUtil.DiffResult {
        val newList = ArrayList(data)
        diffCallBack.setData(current, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallBack)
        current = newList
        return diffResult
    }
}