package com.example.todo

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TodoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveMyTodo(todo: Todo)
    @Query("SELECT * FROM Todo")
    fun getMyTodos(): LiveData<List<Todo>>
    @Delete
    fun deleteTodo(todo:Todo)

}



