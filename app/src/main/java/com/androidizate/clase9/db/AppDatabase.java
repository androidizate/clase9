package com.androidizate.clase9.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.androidizate.clase9.dao.TagDao;
import com.androidizate.clase9.dao.TodoDao;
import com.androidizate.clase9.dao.TodoTagDao;
import com.androidizate.clase9.model.Tag;
import com.androidizate.clase9.model.Todo;
import com.androidizate.clase9.model.TodoTag;

/**
 * @author Andres Oller
 */
@Database(entities = {Todo.class, Tag.class, TodoTag.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract TodoDao todoDao();

    public abstract TagDao tagDao();

    public abstract TodoTagDao todoTagDao();

}
