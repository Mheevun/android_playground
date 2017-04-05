package org.mhee.playground.viewmodel

import io.reactivex.subjects.PublishSubject
import org.mhee.playground.ListBinder
import org.mhee.playground.model.Todo
import org.mhee.playground.model.TodoRepo


/**
 * Created by cnr on 3/26/2017.
 */
class TodoListViewModel internal constructor(val todoListBinder: ListBinder<TodoViewModel>, private val todoRepo: TodoRepo) {
    val todoViewModels = ArrayList<TodoViewModel>()
    private val scrollTo = PublishSubject.create<Int>()


    internal fun initialize() {
        todoViewModels.addAll(toViewModels(todoRepo.todos))
        todoListBinder.notifyDataChange(todoViewModels)
    }

    internal fun setCompleted(position: Int, completed: Boolean) {
        var viewModel = todoViewModels[position]
        if (viewModel.completed != completed) {
            viewModel = viewModel.setCompleted(completed)
            todoRepo.updateTodo(viewModel.toModel())
            todoViewModels.set(position, viewModel)
            todoListBinder.notifyDataChange(todoViewModels)
        }
    }

    private fun toViewModels(todos: List<Todo>): List<TodoViewModel> {

        val viewModels = ArrayList<TodoViewModel>()

        for (todo in todos) {
            viewModels.add(TodoViewModel(todo))
        }

        return viewModels

    }
}