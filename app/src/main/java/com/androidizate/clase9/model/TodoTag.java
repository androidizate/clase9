package com.androidizate.clase9.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;

/**
 * @author Andres Oller
 */
@Entity
public class TodoTag {

    @Id(autoincrement = true)
    private Long id;
    @NotNull
    @Property(nameInDb = "todo_id")
    private Long todoId;
    @NotNull
    @Property(nameInDb = "tag_id")
    private Long tagId;

    @Generated(hash = 1043261838)
    public TodoTag(Long id, @NotNull Long todoId, @NotNull Long tagId) {
        this.id = id;
        this.todoId = todoId;
        this.tagId = tagId;
    }

    @Generated(hash = 1516555268)
    public TodoTag() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTodoId() {
        return todoId;
    }

    public void setTodoId(Long todoId) {
        this.todoId = todoId;
    }

    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }
}
