package com.androidizate.clase9

import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.androidizate.clase9.helper.DatabaseHelper
import com.androidizate.clase9.model.Tag
import com.androidizate.clase9.model.Todo
import kotlinx.android.synthetic.main.activity_main.*
import java.io.FileOutputStream
import java.io.OutputStreamWriter

class MainActivity : AppCompatActivity() {
    // Database Helper
    lateinit var db: DatabaseHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
        db = DatabaseHelper(this)
    }

    override fun onResume() {
        super.onResume()
        populateDb()
        populateSharedPreferences()
        populateFiles()
        registrarUsuario()
    }

    private fun registrarUsuario() {
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        val edit = pref.edit()
        edit.putString("username", "billy")
        edit.putString("user_id", "65")
        edit.apply()
    }

    private fun populateFiles() {
        try {
            val fileout: FileOutputStream = openFileOutput("holamundo.txt", MODE_PRIVATE)
            val outputWriter = OutputStreamWriter(fileout)
            outputWriter.write("Estoy en Androidizate!")
            outputWriter.close()
            Toast.makeText(this, "Archivo Guardado Satifactoriamente!", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun populateSharedPreferences() {
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        val username = pref.getString("username", "n/a")
    }

    private fun populateDb() {
        // Creamos tags
        val tag1 = Tag("Shopping")
        tv_tag_1.text = tag1.tagName
        val tag2 = Tag("Importante")
        tv_tag_2.text = tag2.tagName
        val tag3 = Tag("Peliculas")
        tv_tag_3.text = tag3.tagName
        val tag4 = Tag("Androidizate")
        tv_tag_4.text = tag4.tagName

        // Insertamos los tags en la base de datos
        val tag1_id = db.createTag(tag1)
        val tag2_id = db.createTag(tag2)
        val tag3_id = db.createTag(tag3)
        val tag4_id = db.createTag(tag4)
        tv_tags_quantity.text = "Cantidad de Tags: " + db.getAllTags().size

        // Creamos los ToDos
        val todo1 = Todo("Asado", 0)
        val todo2 = Todo("Samsung S8", 0)
        val todo3 = Todo("Auto", 0)
        val todo4 = Todo("Bourne", 0)
        val todo5 = Todo("Batman vs Superman", 0)
        val todo6 = Todo("Guardianes de la Galaxia 2", 0)
        val todo7 = Todo("Star Wars: Episode IV", 0)
        val todo8 = Todo("Llamar a reservar el after", 0)
        val todo9 = Todo("Sacar plata del cajero", 0)
        val todo10 = Todo("Crear una nueva app", 0)
        val todo11 = Todo("Estudiar mucho", 0)

        // Insertamos todos en la base de datos
        // Insertamos todos bajo el tag "Shopping"
        val todo1_id = db.createToDo(todo1, longArrayOf(tag1_id))
        val todo2_id = db.createToDo(todo2, longArrayOf(tag1_id))
        val todo3_id = db.createToDo(todo3, longArrayOf(tag1_id))

        // Insertamos todos bajo el tag "Peliculas" Tag
        val todo4_id = db.createToDo(todo4, longArrayOf(tag3_id))
        val todo5_id = db.createToDo(todo5, longArrayOf(tag3_id))
        val todo6_id = db.createToDo(todo6, longArrayOf(tag3_id))
        val todo7_id = db.createToDo(todo7, longArrayOf(tag3_id))

        // Insertamos todos bajo el tag "Importante" Tag
        val todo8_id = db.createToDo(todo8, longArrayOf(tag2_id))
        val todo9_id = db.createToDo(todo9, longArrayOf(tag2_id))

        // Insertamos todos bajo el tag "Androidizate"
        val todo10_id = db.createToDo(todo10, longArrayOf(tag4_id))
        val todo11_id = db.createToDo(todo11, longArrayOf(tag4_id))
        fillTodos()
        tv_todos_quantity.text = "Cantidad de Todos: " + db.getToDoCount()

        // "Crear una nueva app" - la asignamos como "Importante"
        // Ahora va a tener 2 tags - "Androidizate" e "Importante"
        db.createTodoTag(todo10_id, tag2_id)

        // Buscamos todos los nombres de tags
        Log.d("Buscar Tags", "Buscamos todos los Tags")
        val allTags = db.getAllTags()
        for (tag in allTags) {
            Log.d("Nombre del Tag", tag.tagName)
        }

        // Buscamos todos los Todos
        Log.d("Buscar Todos", "Buscamos todos los ToDos")
        val allToDos = db.getAllToDos()
        for (todo in allToDos) {
            Log.d("ToDo", todo.note)
        }

        // Buscamos todos los todos bajo el tag "Peliculas"
        Log.d("ToDo", "Buscamos todos los ToDos bajo un Tag")
        val moviesList = db.getAllToDosByTag(tag3.tagName)
        for (todo in moviesList) {
            Log.d("ToDo Peliculas", todo.note)
        }

        // Borramos un ToDo
        Log.d("Borrar ToDo", "Borramos un Todo")
        Log.d("Cantidad de ToDos", "Cantidad de ToDos antes de elimnar el ToDo: " + db.getToDoCount())
        db.deleteToDo(todo8_id)
        Log.d("Cantidad de ToDos", "Cantidad de ToDos despues de elimnar el ToDo: " + db.getToDoCount())

        // Eliminamos todos los ToDos del tag "Shopping"
        Log.d("Cantidad de ToDos", "Cantidad de ToDos antes de eliminar el tag 'Shopping': " + db.getToDoCount())
        db.deleteTag(tag1, true)
        Log.d("Cantidad de ToDos", "Cantidad de ToDos despues de elimnar el tag 'Shopping': " + db.getToDoCount())

        // Actualizamos el nombre del tag
        tag3.tagName = "Peliculas a mirar"
        db.updateTag(tag3)
    }

    override fun onStop() {
        super.onStop()
        // borramos la base de datos y cerramos la conexion
        getApplicationContext().deleteDatabase(DatabaseHelper.Companion.DATABASE_NAME)
        db.closeDB()
    }

    private fun fillTodos() {
        fillTodo(R.id.tv_todo_1, db.getTodo(1), db.getAllTagsForTodo(1))
        fillTodo(R.id.tv_todo_2, db.getTodo(2), db.getAllTagsForTodo(2))
        fillTodo(R.id.tv_todo_3, db.getTodo(3), db.getAllTagsForTodo(3))
        fillTodo(R.id.tv_todo_4, db.getTodo(4), db.getAllTagsForTodo(4))
        fillTodo(R.id.tv_todo_5, db.getTodo(5), db.getAllTagsForTodo(5))
        fillTodo(R.id.tv_todo_6, db.getTodo(6), db.getAllTagsForTodo(6))
        fillTodo(R.id.tv_todo_7, db.getTodo(7), db.getAllTagsForTodo(7))
        fillTodo(R.id.tv_todo_8, db.getTodo(8), db.getAllTagsForTodo(8))
        fillTodo(R.id.tv_todo_9, db.getTodo(9), db.getAllTagsForTodo(9))
        fillTodo(R.id.tv_todo_10, db.getTodo(10), db.getAllTagsForTodo(10))
        fillTodo(R.id.tv_todo_11, db.getTodo(11), db.getAllTagsForTodo(11))
    }

    private fun fillTodo(resource: Int, todo: Todo, tags: List<Tag>) {
        var newText: String? = null
        for (i in tags.indices) {
            newText = if (i == 0) {
                todo.note + " - " + tags[i].tagName
            } else {
                newText + ", " + tags[i].tagName
            }
        }
        (findViewById(resource) as TextView).text = newText
    }
}