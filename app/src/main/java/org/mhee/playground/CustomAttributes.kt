package org.mhee.playground

import android.databinding.BindingAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView


/**
 * Created by cnr on 3/26/2017.
 */
@BindingAdapter("listBinder")
fun <E> bindItems(recyclerView: RecyclerView, listBinder: ListBinder<E>) {
    val adapter = recyclerView.adapter
    if (adapter != null) {
        listBinder.setOnDataChangeListener(object :ListBinder.OnDataChangeListener {
            override fun onChange(diffResult: DiffUtil.DiffResult) = diffResult.dispatchUpdatesTo(adapter)
        })
    }
}