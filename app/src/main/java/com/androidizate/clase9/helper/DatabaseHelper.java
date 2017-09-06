package com.androidizate.clase9.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.androidizate.clase9.model.Tag;
import com.androidizate.clase9.model.Todo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @author Andres Oller
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // Tag del log
    private static final String LOG = "DatabaseHelper";

    // Version de la Base de Datos
    private static final int DATABASE_VERSION = 1;

    // Nombre de la Base de Datos
    public static final String DATABASE_NAME = "contactsManager";

    // Nombre de las tablas
    private static final String TABLE_TODO = "todos";
    private static final String TABLE_TAG = "tags";
    private static final String TABLE_TODO_TAG = "todo_tags";

    // Columnas comunes
    private static final String KEY_ID = "id";
    private static final String KEY_CREATED_AT = "created_at";

    // Tabla TODO - nombre de columnas
    private static final String KEY_TODO = "todo";
    private static final String KEY_STATUS = "status";

    // Tabla TAGS - nombre de columnas
    private static final String KEY_TAG_NAME = "tag_name";

    // Tabla NOTE_TAGS - nombre de columnas
    private static final String KEY_TODO_ID = "todo_id";
    private static final String KEY_TAG_ID = "tag_id";

    // Sentencias de creacion de tablas
    // Creacion de la tabla TODO
    private static final String CREATE_TABLE_TODO = "CREATE TABLE "
            + TABLE_TODO + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TODO
            + " TEXT," + KEY_STATUS + " INTEGER," + KEY_CREATED_AT
            + " DATETIME" + ")";

    // Creacion de la tabla TAG
    private static final String CREATE_TABLE_TAG = "CREATE TABLE " + TABLE_TAG
            + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TAG_NAME + " TEXT,"
            + KEY_CREATED_AT + " DATETIME" + ")";

    // Creacion de la tabla TODO_TAG
    private static final String CREATE_TABLE_TODO_TAG = "CREATE TABLE "
            + TABLE_TODO_TAG + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_TODO_ID + " INTEGER," + KEY_TAG_ID + " INTEGER,"
            + KEY_CREATED_AT + " DATETIME" + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        db.execSQL(CREATE_TABLE_TODO);
        db.execSQL(CREATE_TABLE_TAG);
        db.execSQL(CREATE_TABLE_TODO_TAG);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // en la actualización eliminamos las tablas viejas
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TAG);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO_TAG);

        // creamos las nuevas tablas
        onCreate(db);
    }

    // ------------------------ metodos de la tabla "todos" ----------------//

    /*
     * Creando un todo
     */
    public long createToDo(Todo todo, long[] tag_ids) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TODO, todo.getNote());
        values.put(KEY_STATUS, todo.getStatus());
        values.put(KEY_CREATED_AT, getDateTime());

        // insertamos una fila
        long todo_id = db.insert(TABLE_TODO, null, values);

        // insertamos tag_ids
        for (long tag_id : tag_ids) {
            createTodoTag(todo_id, tag_id);
        }

        return todo_id;
    }

    /**
     * buscamos solo un todo
     */
    public Todo getTodo(long todo_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_TODO + " WHERE "
                + KEY_ID + " = " + todo_id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);
        Todo td = new Todo();

        if (c.moveToFirst()) {
            td.setId(c.getInt(c.getColumnIndex(KEY_ID)));
            td.setNote((c.getString(c.getColumnIndex(KEY_TODO))));
            td.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
        }

        return td;
    }

    /**
     * buscamos todos los todos
     */
    public List<Todo> getAllToDos() {
        List<Todo> todos = new ArrayList<Todo>();
        String selectQuery = "SELECT  * FROM " + TABLE_TODO;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Todo td = new Todo();
                td.setId(c.getInt((c.getColumnIndex(KEY_ID))));
                td.setNote((c.getString(c.getColumnIndex(KEY_TODO))));
                td.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

                // adding to todo list
                todos.add(td);
            } while (c.moveToNext());
        }

        return todos;
    }

    /**
     * buscamos los todos bajo un mismo tag
     */
    public List<Todo> getAllToDosByTag(String tag_name) {
        List<Todo> todos = new ArrayList<Todo>();

        String selectQuery = "SELECT  * FROM " + TABLE_TODO + " td, "
                + TABLE_TAG + " tg, " + TABLE_TODO_TAG + " tt WHERE tg."
                + KEY_TAG_NAME + " = '" + tag_name + "'" + " AND tg." + KEY_ID
                + " = " + "tt." + KEY_TAG_ID + " AND td." + KEY_ID + " = "
                + "tt." + KEY_TODO_ID;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // loopeamos todas las filas y las agregamos a la lista
        if (c.moveToFirst()) {
            do {
                Todo td = new Todo();
                td.setId(c.getInt((c.getColumnIndex(KEY_ID))));
                td.setNote((c.getString(c.getColumnIndex(KEY_TODO))));
                td.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

                // agregamos un todo a la lista
                todos.add(td);
            } while (c.moveToNext());
        }

        return todos;
    }

    /*
     * contamos la cantidad de todos
     */
    public int getToDoCount() {
        String countQuery = "SELECT * FROM " + TABLE_TODO;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // devolvemos la cantidad
        return count;
    }

    /*
     * actualizamos un todo
     */
    public int updateToDo(Todo todo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TODO, todo.getNote());
        values.put(KEY_STATUS, todo.getStatus());

        // updating row
        return db.update(TABLE_TODO, values, KEY_ID + " = ?",
                new String[]{String.valueOf(todo.getId())});
    }

    /*
     * eliminamos un todo
     */
    public void deleteToDo(long tado_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TODO, KEY_ID + " = ?",
                new String[]{String.valueOf(tado_id)});
    }

    // ------------------------ métodos de la tabla "tags" ----------------//

    /*
     * Creamos un tag
     */
    public long createTag(Tag tag) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TAG_NAME, tag.getTagName());
        values.put(KEY_CREATED_AT, getDateTime());

        // insertamos una fila
        long tag_id = db.insert(TABLE_TAG, null, values);

        return tag_id;
    }

    /**
     * buscamos solo un tag
     */
    public Tag getTag(long tag_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_TAG + " WHERE "
                + KEY_ID + " = " + tag_id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        Tag tg = new Tag();

        if (c.moveToFirst()) {
            tg.setId(c.getColumnIndex(KEY_ID));
            tg.setTagName((c.getString(c.getColumnIndex(KEY_TAG_NAME))));
        }

        return tg;
    }

    /**
     * buscamos todos los tags
     */
    public List<Tag> getAllTags() {
        List<Tag> tags = new ArrayList<Tag>();
        String selectQuery = "SELECT  * FROM " + TABLE_TAG;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // loopeamos todas los tags y los agregamos a la lista
        if (c.moveToFirst()) {
            do {
                Tag t = new Tag();
                t.setId(c.getInt((c.getColumnIndex(KEY_ID))));
                t.setTagName(c.getString(c.getColumnIndex(KEY_TAG_NAME)));

                // agregamos el tag a la lista
                tags.add(t);
            } while (c.moveToNext());
        }
        return tags;
    }

    /*
     * actualizamos un tag
     */
    public int updateTag(Tag tag) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TAG_NAME, tag.getTagName());

        // actualizamos una fila
        return db.update(TABLE_TAG, values, KEY_ID + " = ?",
                new String[]{String.valueOf(tag.getId())});
    }

    /*
     * eliminamos un tag
     */
    public void deleteTag(Tag tag, boolean should_delete_all_tag_todos) {
        SQLiteDatabase db = this.getWritableDatabase();

        // antes de eliminar un tag
        // verificamos si los todos de este tag tambien deben ser eliminados
        if (should_delete_all_tag_todos) {
            // buscamos todos los todos bajo este tag
            List<Todo> allTagToDos = getAllToDosByTag(tag.getTagName());

            // eliminamos los todos
            for (Todo todo : allTagToDos) {
                // elminamos un todo
                deleteToDo(todo.getId());
            }
        }

        // ahora eliminamos el tag
        db.delete(TABLE_TAG, KEY_ID + " = ?",
                new String[]{String.valueOf(tag.getId())});
    }

    // ------------------------ métodos de la tabla "todo_tags" ----------------//

    /*
     * Creamos un todo_tag
     */
    public long createTodoTag(long todo_id, long tag_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TODO_ID, todo_id);
        values.put(KEY_TAG_ID, tag_id);
        values.put(KEY_CREATED_AT, getDateTime());

        long id = db.insert(TABLE_TODO_TAG, null, values);

        return id;
    }

    /**
     * buscamos todos los tags de un todo
     */
    public List<Tag> getAllTagsForTodo(long todo_id) {
        List<Tag> tags = new ArrayList<Tag>();
        String selectQuery = "SELECT * FROM " + TABLE_TODO_TAG + " WHERE " + KEY_TODO_ID + " = " + todo_id;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // loopeamos todas los tags y los agregamos a la lista
        if (c.moveToFirst()) {
            do {
                Tag t = getTag(c.getInt((c.getColumnIndex(KEY_TAG_ID))));

                // agregamos el tag a la lista
                tags.add(t);
            } while (c.moveToNext());
        }
        return tags;
    }

    /*
     * actualizamos un todo tag
     */
    public int updateNoteTag(long id, long tag_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TAG_ID, tag_id);

        // updating row
        return db.update(TABLE_TODO, values, KEY_ID + " = ?",
                new String[]{String.valueOf(id)});
    }

    /*
     * eliminamos un todo tag
     */
    public void deleteToDoTag(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TODO, KEY_ID + " = ?",
                new String[]{String.valueOf(id)});
    }

    // cerramos la base de datos
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }

    /**
     * tomamos el date
     */
    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
}