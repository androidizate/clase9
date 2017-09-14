package com.androidizate.clase9.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.androidizate.clase9.model.Tag;
import com.androidizate.clase9.model.TodoTag;

import java.util.List;

/**
 * @author Andres Oller
 */
@Dao
public interface TodoTagDao {

    @Query("SELECT * FROM todo_tag")
    List<TodoTag> getAll();

    @Query("SELECT * FROM todo_tag WHERE todo_id = :id")
    List<Tag> getTagsForTodo(int id);

    @Query("SELECT * FROM todo_tag WHERE id LIKE :id")
    List<TodoTag> findById(int id);

    @Insert
    void insertTodoTags(TodoTag... todoTags);

    @Delete
    void delete(TodoTag todoTag);

    @Update
    void updateTags(TodoTag... todoTags);
}
