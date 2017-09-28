package com.androidizate.clase9.mvp;

import android.database.sqlite.SQLiteConstraintException;

import com.androidizate.clase9.model.DaoSession;
import com.androidizate.clase9.model.Tag;
import com.androidizate.clase9.model.TagDao;
import com.androidizate.clase9.model.Todo;
import com.androidizate.clase9.model.TodoDao;
import com.androidizate.clase9.model.TodoTag;
import com.androidizate.clase9.model.TodoTagDao;
import com.androidizate.clase9.utils.FileUtils;
import com.androidizate.clase9.utils.PreferencesUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Andres Oller
 */
public class MainModel {

    private PreferencesUtil preferencesUtil;
    private FileUtils fileUtils;
    private DaoSession daoSession;

    MainModel(DaoSession daoSession, PreferencesUtil preferencesUtil, FileUtils fileUtils) {
        this.daoSession = daoSession;
        this.preferencesUtil = preferencesUtil;
        this.fileUtils = fileUtils;
    }


    public void registerUser() {
        preferencesUtil.setStringPreference("username", "billy");
        preferencesUtil.setStringPreference("user_id", "65");
    }

    public void createFiles() {
        fileUtils.createTxtFile("androidizate.txt", "Hola!! Estoy en Androidizate");
    }

    public String getRegisteredUser() {
        return preferencesUtil.getStringPreference("username");
    }

    public String getRegisteredUserId() {
        return preferencesUtil.getStringPreference("user_id");
    }

    public void populateDb() {
        // Creamos tags
        Tag tag1 = new Tag();
        tag1.setTagName("Shopping");
        Tag tag2 = new Tag();
        tag2.setTagName("Importante");
        Tag tag3 = new Tag();
        tag3.setTagName("Peliculas");
        Tag tag4 = new Tag();
        tag4.setTagName("Androidizate");

        try {
            daoSession.insert(tag1);
            daoSession.insert(tag2);
            daoSession.insert(tag3);
            daoSession.insert(tag4);
        } catch (SQLiteConstraintException exception) {
            exception.printStackTrace();
        }

        List<Todo> todos = new ArrayList<>();
        // Creamos los ToDos
        Todo todo1 = new Todo();
        todo1.setCreatedAt(new Date(System.currentTimeMillis()));
        todo1.setNote("Asado");

        Todo todo2 = new Todo();
        todo2.setCreatedAt(new Date(System.currentTimeMillis()));
        todo2.setNote("Samsung S8");

        Todo todo3 = new Todo();
        todo3.setCreatedAt(new Date(System.currentTimeMillis()));
        todo3.setNote("Auto");

        Todo todo4 = new Todo();
        todo4.setCreatedAt(new Date(System.currentTimeMillis()));
        todo4.setNote("Bourne");

        Todo todo5 = new Todo();
        todo5.setCreatedAt(new Date(System.currentTimeMillis()));
        todo5.setNote("Batman vs Superman");

        Todo todo6 = new Todo();
        todo6.setCreatedAt(new Date(System.currentTimeMillis()));
        todo6.setNote("Guardianes de la Galaxia 2");

        Todo todo7 = new Todo();
        todo7.setCreatedAt(new Date(System.currentTimeMillis()));
        todo7.setNote("Star Wars: Episode IV");

        Todo todo8 = new Todo();
        todo8.setCreatedAt(new Date(System.currentTimeMillis()));
        todo8.setNote("Llamar a reservar el after");

        Todo todo9 = new Todo();
        todo9.setCreatedAt(new Date(System.currentTimeMillis()));
        todo9.setNote("Sacar plata del cajero");

        Todo todo10 = new Todo();
        todo10.setCreatedAt(new Date(System.currentTimeMillis()));
        todo10.setNote("Crear una nueva app");

        Todo todo11 = new Todo();
        todo11.setCreatedAt(new Date(System.currentTimeMillis()));
        todo11.setNote("Estudiar mucho");

        todos.add(todo1);
        todos.add(todo2);
        todos.add(todo3);
        todos.add(todo4);
        todos.add(todo5);
        todos.add(todo6);
        todos.add(todo7);
        todos.add(todo8);
        todos.add(todo9);
        todos.add(todo10);
        todos.add(todo11);

        // Insertamos todos en la base de datos
        for (Todo todo : todos) {
            try {
                daoSession.insert(todo);
            } catch (SQLiteConstraintException exception) {
                exception.printStackTrace();
            }
        }

        // Insertamos todos bajo el tag "Shopping"
        TodoTag todoTag1 = new TodoTag();
        todoTag1.setTodoId(todo1.getId());
        todoTag1.setTagId(tag1.getId());

        TodoTag todoTag2 = new TodoTag();
        todoTag2.setTodoId(todo2.getId());
        todoTag2.setTagId(tag1.getId());

        TodoTag todoTag3 = new TodoTag();
        todoTag3.setTodoId(todo3.getId());
        todoTag3.setTagId(tag1.getId());

        daoSession.getTodoTagDao().insert(todoTag1);
        daoSession.getTodoTagDao().insert(todoTag2);
        daoSession.getTodoTagDao().insert(todoTag3);

        // Insertamos todos bajo el tag "Peliculas" Tag
        TodoTag todoTag4 = new TodoTag();
        todoTag4.setTodoId(todo4.getId());
        todoTag4.setTagId(tag3.getId());

        TodoTag todoTag5 = new TodoTag();
        todoTag5.setTodoId(todo5.getId());
        todoTag5.setTagId(tag3.getId());

        TodoTag todoTag6 = new TodoTag();
        todoTag6.setTodoId(todo6.getId());
        todoTag6.setTagId(tag3.getId());

        TodoTag todoTag7 = new TodoTag();
        todoTag7.setTodoId(todo7.getId());
        todoTag7.setTagId(tag3.getId());

        daoSession.getTodoTagDao().insert(todoTag4);
        daoSession.getTodoTagDao().insert(todoTag5);
        daoSession.getTodoTagDao().insert(todoTag6);
        daoSession.getTodoTagDao().insert(todoTag7);

        // Insertamos todos bajo el tag "Importante" Tag
        TodoTag todoTag8 = new TodoTag();
        todoTag8.setTodoId(todo8.getId());
        todoTag8.setTagId(tag2.getId());

        TodoTag todoTag9 = new TodoTag();
        todoTag9.setTodoId(todo9.getId());
        todoTag9.setTagId(tag2.getId());

        daoSession.getTodoTagDao().insert(todoTag8);
        daoSession.getTodoTagDao().insert(todoTag9);

        // Insertamos todos bajo el tag "Androidizate"
        TodoTag todoTag10 = new TodoTag();
        todoTag10.setTodoId(todo10.getId());
        todoTag10.setTagId(tag4.getId());

        TodoTag todoTag11 = new TodoTag();
        todoTag11.setTodoId(todo11.getId());
        todoTag11.setTagId(tag4.getId());

        daoSession.getTodoTagDao().insert(todoTag10);
        daoSession.getTodoTagDao().insert(todoTag11);
    }

    public List<Tag> loadAllTags() {
        return daoSession.getTagDao().loadAll();
    }

    public List<Todo> loadAllTodos() {
        return daoSession.getTodoDao().loadAll();
    }

    public Todo getTodo(String note) {
        return daoSession.getTodoDao().queryBuilder().where(TodoDao.Properties.Note.eq(note)).build().unique();
    }

    public Tag getTag(String name) {
        return daoSession.getTagDao().queryBuilder().where(TodoDao.Properties.Note.eq(name)).build().unique();
    }

    public List<Todo> getAllMovies() {
        long moviesTagId = daoSession.getTagDao().queryBuilder().where(TagDao.Properties.TagName.eq("Peliculas")).build().unique().getId();
        return daoSession.getTodoDao().queryBuilder().where(TodoTagDao.Properties.TagId.eq(moviesTagId)).build().list();
    }

    public void deleteTodo(String note) {
        daoSession.getTodoDao().queryBuilder().where(TodoDao.Properties.Note.eq(note)).buildDelete().executeDeleteWithoutDetachingEntities();
    }

    public void deleteTag(String tagName) {
        daoSession.getTagDao().queryBuilder().where(TodoDao.Properties.Note.eq(tagName)).buildDelete().executeDeleteWithoutDetachingEntities();
    }

    public void addNewTagToTodo(String note, String tagName) {
        Todo todo = getTodo(note);
        Tag tag = getTag(tagName);
        TodoTag todoTag = new TodoTag();
        todoTag.setTodoId(todo.getId());
        todoTag.setTagId(tag.getId());

        daoSession.getTodoTagDao().insert(todoTag);
    }

    public void updateTagName(String oldName, String newName) {
        Tag tag = getTag(oldName);
        tag.setTagName(newName);
        daoSession.getTagDao().update(tag);
    }

    public void deleteAll() {
        daoSession.getTodoDao().deleteAll();
        daoSession.getTagDao().deleteAll();
        daoSession.getTodoTagDao().deleteAll();
    }

    public List<Todo> getAllTodos() {
        return daoSession.getTodoDao().loadAll();
    }

    public List<TodoTag> getAllTodoTag() {
        return daoSession.getTodoTagDao().loadAll();
    }

    public List<Tag> getAllTags() {
        return daoSession.getTagDao().loadAll();
    }
}
