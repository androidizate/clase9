package com.androidizate.clase9.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.androidizate.clase9.model.Todo;

import java.util.List;

/**
 * @author Andres Oller
 */
@Dao
public interface TodoDao {

    @Query("SELECT * FROM todo")
    List<Todo> getAll();

    @Query("SELECT * FROM todo WHERE id IN (:todoIds)")
    List<Todo> loadAllByIds(int[] todoIds);

    @Query("SELECT * FROM todo WHERE id LIKE :id")
    Todo findById(int id);

    @Query("SELECT  * FROM todo td, tag tg, todo_tag tt WHERE tg.tag_name = :tagName AND tg.id = tt.tad_id AND td.id = tt.todo_id")
    List<Todo> getTodosByTag(String tagName);

    @Insert
    void insertAll(Todo... todos);

    @Delete
    void delete(Todo todos);

    @Update
    void updateTodo(Todo... todos);
}
