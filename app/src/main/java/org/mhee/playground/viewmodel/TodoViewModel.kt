package org.mhee.playground.viewmodel

import android.view.View
import org.mhee.playground.model.Todo



class TodoViewModel(val id: Long, val title: String, val dueDate: String, val completed: Boolean) {
    constructor(todo: Todo) : this(todo.id, todo.title, todo.dueDate, todo.completed)
    constructor(todoViewModel: TodoViewModel, completed: Boolean) :
            this(todoViewModel.id, todoViewModel.title, todoViewModel.dueDate, completed)

    fun setCompleted(completed: Boolean): TodoViewModel {
        return TodoViewModel(this, completed)
    }

    fun removeVisibility(): Int {
        return if (completed) View.VISIBLE else View.INVISIBLE
    }

    fun toModel(): Todo {
        return Todo(id, title, dueDate, completed)
    }

    override fun equals(other: Any?): Boolean {
        if (other is TodoViewModel) {
            return id == other.id
        }
        return super.equals(other)
    }
}