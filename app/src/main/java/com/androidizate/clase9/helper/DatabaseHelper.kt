package com.androidizate.clase9.helper

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.androidizate.clase9.model.Tag
import com.androidizate.clase9.model.Todo
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author Andres Oller
 */
class DatabaseHelper(context: Context?) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        // creating required tables
        db.execSQL(CREATE_TABLE_TODO)
        db.execSQL(CREATE_TABLE_TAG)
        db.execSQL(CREATE_TABLE_TODO_TAG)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // en la actualización eliminamos las tablas viejas
        db.execSQL("DROP TABLE IF EXISTS $TABLE_TODO")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_TAG")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_TODO_TAG")

        // creamos las nuevas tablas
        onCreate(db)
    }

    // ------------------------ metodos de la tabla "todos" ----------------//
    /*
     * Creando un todo
     */
    fun createToDo(todo: Todo, tag_ids: LongArray): Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(KEY_TODO, todo.note)
        values.put(KEY_STATUS, todo.status)
        values.put(KEY_CREATED_AT, getDateTime())

        // insertamos una fila
        val todo_id = db.insert(TABLE_TODO, null, values)

        // insertamos tag_ids
        for (tag_id in tag_ids) {
            createTodoTag(todo_id, tag_id)
        }
        return todo_id
    }

    /**
     * buscamos solo un todo
     */
    fun getTodo(todo_id: Long): Todo {
        val db = this.readableDatabase
        val selectQuery = ("SELECT  * FROM " + TABLE_TODO + " WHERE "
                + KEY_ID + " = " + todo_id)
        Log.e(LOG, selectQuery)
        val c = db.rawQuery(selectQuery, null)
        val td = Todo()
        if (c.moveToFirst()) {
            td.id = c.getLong(c.getColumnIndex(KEY_ID))
            td.note = c.getString(c.getColumnIndex(KEY_TODO))
            td.created_at = c.getString(c.getColumnIndex(KEY_CREATED_AT))
        }
        c.close()
        return td
    }// adding to todo list// looping through all rows and adding to list

    /**
     * buscamos todos los todos
     */
    fun getAllToDos(): List<Todo> {
        val todos: MutableList<Todo> = ArrayList()
        val selectQuery = "SELECT  * FROM $TABLE_TODO"
        Log.e(LOG, selectQuery)
        val db = this.readableDatabase
        val c = db.rawQuery(selectQuery, null)

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                val td = Todo()
                td.id = c.getLong(c.getColumnIndex(KEY_ID))
                td.note = c.getString(c.getColumnIndex(KEY_TODO))
                td.created_at = c.getString(c.getColumnIndex(KEY_CREATED_AT))

                // adding to todo list
                todos.add(td)
            } while (c.moveToNext())
        }
        c.close()
        return todos
    }

    /**
     * buscamos los todos bajo un mismo tag
     */
    fun getAllToDosByTag(tag_name: String?): List<Todo> {
        val todos: MutableList<Todo> = ArrayList()
        val selectQuery = ("SELECT  * FROM " + TABLE_TODO + " td, "
                + TABLE_TAG + " tg, " + TABLE_TODO_TAG + " tt WHERE tg."
                + KEY_TAG_NAME + " = '" + tag_name + "'" + " AND tg." + KEY_ID
                + " = " + "tt." + KEY_TAG_ID + " AND td." + KEY_ID + " = "
                + "tt." + KEY_TODO_ID)
        Log.e(LOG, selectQuery)
        val db = this.readableDatabase
        val c = db.rawQuery(selectQuery, null)

        // loopeamos todas las filas y las agregamos a la lista
        if (c.moveToFirst()) {
            do {
                val td = Todo()
                td.id = c.getLong(c.getColumnIndex(KEY_ID))
                td.note = c.getString(c.getColumnIndex(KEY_TODO))
                td.created_at = c.getString(c.getColumnIndex(KEY_CREATED_AT))

                // agregamos un todo a la lista
                todos.add(td)
            } while (c.moveToNext())
        }
        c.close()
        return todos
    }

    // devolvemos la cantidad

    /*
     * contamos la cantidad de todos
     */
    fun getToDoCount(): Int {
        val countQuery = "SELECT * FROM $TABLE_TODO"
        val db = this.readableDatabase
        val cursor = db.rawQuery(countQuery, null)
        val count = cursor.count
        cursor.close()

        // devolvemos la cantidad
        return count
    }

    /*
     * actualizamos un todo
     */
    fun updateToDo(todo: Todo): Int {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(KEY_TODO, todo.note)
        values.put(KEY_STATUS, todo.status)

        // updating row
        return db.update(TABLE_TODO, values, "$KEY_ID = ?", arrayOf(todo.id.toString()))
    }

    /*
     * eliminamos un todo
     */
    fun deleteToDo(tado_id: Long) {
        val db = this.writableDatabase
        db.delete(TABLE_TODO, "$KEY_ID = ?", arrayOf(tado_id.toString()))
    }

    // ------------------------ métodos de la tabla "tags" ----------------//
    /*
     * Creamos un tag
     */
    fun createTag(tag: Tag): Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(KEY_TAG_NAME, tag.tagName)
        values.put(KEY_CREATED_AT, getDateTime())

        // insertamos una fila
        return db.insert(TABLE_TAG, null, values)
    }

    /**
     * buscamos solo un tag
     */
    fun getTag(tag_id: Long): Tag {
        val db = this.readableDatabase
        val selectQuery = ("SELECT  * FROM " + TABLE_TAG + " WHERE "
                + KEY_ID + " = " + tag_id)
        Log.e(LOG, selectQuery)
        val c = db.rawQuery(selectQuery, null)
        val tg = Tag()
        if (c.moveToFirst()) {
            tg.id = c.getColumnIndex(KEY_ID)
            tg.tagName = c.getString(c.getColumnIndex(KEY_TAG_NAME))
        }
        c.close()
        return tg
    }

    // agregamos el tag a la lista// loopeamos todas los tags y los agregamos a la lista

    /**
     * buscamos todos los tags
     */
    fun getAllTags(): List<Tag> {
        val tags: MutableList<Tag> = ArrayList()
        val selectQuery = "SELECT  * FROM $TABLE_TAG"
        Log.e(LOG, selectQuery)
        val db = this.readableDatabase
        val c = db.rawQuery(selectQuery, null)

        // loopeamos todas los tags y los agregamos a la lista
        if (c.moveToFirst()) {
            do {
                val t = Tag()
                t.id = c.getInt(c.getColumnIndex(KEY_ID))
                t.tagName = c.getString(c.getColumnIndex(KEY_TAG_NAME))

                // agregamos el tag a la lista
                tags.add(t)
            } while (c.moveToNext())
        }
        c.close()
        return tags
    }

    /*
     * actualizamos un tag
     */
    fun updateTag(tag: Tag): Int {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(KEY_TAG_NAME, tag.tagName)

        // actualizamos una fila
        return db.update(TABLE_TAG, values, "$KEY_ID = ?", arrayOf(tag.id.toString()))
    }

    /*
     * eliminamos un tag
     */
    fun deleteTag(tag: Tag, should_delete_all_tag_todos: Boolean) {
        val db = this.writableDatabase

        // antes de eliminar un tag
        // verificamos si los todos de este tag tambien deben ser eliminados
        if (should_delete_all_tag_todos) {
            // buscamos todos los todos bajo este tag
            val allTagToDos = getAllToDosByTag(tag.tagName)

            // eliminamos los todos
            for (todo in allTagToDos) {
                // elminamos un todo
                deleteToDo(todo.id)
            }
        }

        // ahora eliminamos el tag
        db.delete(TABLE_TAG, "$KEY_ID = ?", arrayOf(tag.id.toString()))
    }

    // ------------------------ métodos de la tabla "todo_tags" ----------------//
    /*
     * Creamos un todo_tag
     */
    fun createTodoTag(todo_id: Long, tag_id: Long): Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(KEY_TODO_ID, todo_id)
        values.put(KEY_TAG_ID, tag_id)
        values.put(KEY_CREATED_AT, getDateTime())
        return db.insert(TABLE_TODO_TAG, null, values)
    }

    /**
     * buscamos todos los tags de un todo
     */
    fun getAllTagsForTodo(todo_id: Long): List<Tag> {
        val tags: MutableList<Tag> = ArrayList()
        val selectQuery = "SELECT * FROM $TABLE_TODO_TAG WHERE $KEY_TODO_ID = $todo_id"
        Log.e(LOG, selectQuery)
        val db = this.readableDatabase
        val c = db.rawQuery(selectQuery, null)

        // loopeamos todas los tags y los agregamos a la lista
        if (c.moveToFirst()) {
            do {
                val t = getTag(c.getInt(c.getColumnIndex(KEY_TAG_ID)).toLong())

                // agregamos el tag a la lista
                tags.add(t)
            } while (c.moveToNext())
        }
        c.close()
        return tags
    }

    /*
     * actualizamos un todo tag
     */
    fun updateNoteTag(id: Long, tag_id: Long): Int {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(KEY_TAG_ID, tag_id)

        // updating row
        return db.update(TABLE_TODO, values, "$KEY_ID = ?", arrayOf(id.toString()))
    }

    /*
     * eliminamos un todo tag
     */
    fun deleteToDoTag(id: Long) {
        val db = this.writableDatabase
        db.delete(TABLE_TODO, "$KEY_ID = ?", arrayOf(id.toString()))
    }

    // cerramos la base de datos
    fun closeDB() {
        val db = this.readableDatabase
        if (db != null && db.isOpen) db.close()
    }

    /**
     * tomamos el date
     */
    private fun getDateTime(): String {
        val dateFormat = SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val date = Date()
        return dateFormat.format(date)
    }

    companion object {
        // Tag del log
        private const val LOG = "DatabaseHelper"

        // Version de la Base de Datos
        private const val DATABASE_VERSION = 1

        // Nombre de la Base de Datos
        const val DATABASE_NAME = "contactsManager"

        // Nombre de las tablas
        private const val TABLE_TODO = "todos"
        private const val TABLE_TAG = "tags"
        private const val TABLE_TODO_TAG = "todo_tags"

        // Columnas comunes
        private const val KEY_ID = "id"
        private const val KEY_CREATED_AT = "created_at"

        // Tabla TODO - nombre de columnas
        private const val KEY_TODO = "todo"
        private const val KEY_STATUS = "status"

        // Tabla TAGS - nombre de columnas
        private const val KEY_TAG_NAME = "tag_name"

        // Tabla NOTE_TAGS - nombre de columnas
        private const val KEY_TODO_ID = "todo_id"
        private const val KEY_TAG_ID = "tag_id"

        // Sentencias de creacion de tablas
        // Creacion de la tabla TODO
        private const val CREATE_TABLE_TODO = ("CREATE TABLE "
                + TABLE_TODO + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TODO
                + " TEXT," + KEY_STATUS + " INTEGER," + KEY_CREATED_AT
                + " DATETIME" + ")")

        // Creacion de la tabla TAG
        private const val CREATE_TABLE_TAG = ("CREATE TABLE " + TABLE_TAG
                + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TAG_NAME + " TEXT,"
                + KEY_CREATED_AT + " DATETIME" + ")")

        // Creacion de la tabla TODO_TAG
        private const val CREATE_TABLE_TODO_TAG = ("CREATE TABLE "
                + TABLE_TODO_TAG + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_TODO_ID + " INTEGER," + KEY_TAG_ID + " INTEGER,"
                + KEY_CREATED_AT + " DATETIME" + ")")
    }
}