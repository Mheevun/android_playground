package org.mhee.playground.model

/**
 * Created by cnr on 3/26/2017.
 */
interface TodoRepo {
    val todos: List<Todo>
    fun updateTodo(todo: Todo)
    fun deleteTodo(todo: Todo)
    fun createTodo(title: String, dueDate: String): Todo
}