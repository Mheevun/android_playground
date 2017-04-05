package org.mhee.playground.diffcallback

import org.mhee.playground.viewmodel.TodoViewModel
import javax.inject.Inject


/**
 * Created by cnr on 3/26/2017.
 */
class TodoDiffCallback @Inject constructor() : DiffCallBack<TodoViewModel>() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList.get(newItemPosition).id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].title == newList[newItemPosition].title
                && oldList[oldItemPosition].dueDate == newList[newItemPosition].dueDate
                && oldList[oldItemPosition].completed == newList.get(newItemPosition).completed
    }
}