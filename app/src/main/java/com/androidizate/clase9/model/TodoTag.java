package com.androidizate.clase9.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;

/**
 * @author Andres Oller
 */
@Entity(foreignKeys = {
        @ForeignKey(entity = Todo.class, parentColumns = "id", childColumns = "todo_id"),
        @ForeignKey(entity = Tag.class, parentColumns = "id", childColumns = "tag_id")
})
public class TodoTag {

    @ColumnInfo(name = "todo_id")
    int todoId;

    @ColumnInfo(name = "tag_id")
    int tagId;

    public TodoTag(int todoId, int tagId) {
        this.todoId = todoId;
        this.tagId = tagId;
    }

    public int getTodoId() {
        return todoId;
    }

    public void setTodoId(int todoId) {
        this.todoId = todoId;
    }

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }
}
