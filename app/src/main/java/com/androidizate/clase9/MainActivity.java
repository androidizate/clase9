package com.androidizate.clase9;

import android.arch.persistence.room.Room;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.androidizate.clase9.db.AppDatabase;
import com.androidizate.clase9.model.Tag;
import com.androidizate.clase9.model.Todo;
import com.androidizate.clase9.model.TodoTag;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "todo_app").build();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        populateDb();
        populateSharedPreferences();
        populateFiles();
        registrarUsuario();
    }

    private void registrarUsuario() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor edit = pref.edit();
        edit.putString("username", "billy");
        edit.putString("user_id", "65");
        edit.apply();
    }

    private void populateFiles() {
        try {
            FileOutputStream fileout = openFileOutput("holamundo.txt", MODE_PRIVATE);
            OutputStreamWriter outputWriter = new OutputStreamWriter(fileout);
            outputWriter.write("Estoy en Androidizate!");
            outputWriter.close();

            Toast.makeText(getBaseContext(), "Archivo Guardado Satifactoriamente!", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void populateSharedPreferences() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        String username = pref.getString("username", "n/a");
    }

    private void populateDb() {
        // Creamos tags
        Tag tag1 = new Tag(0, "Shopping");
        ((TextView) findViewById(R.id.tv_tag_1)).setText(tag1.getTagName());
        Tag tag2 = new Tag(1, "Importante");
        ((TextView) findViewById(R.id.tv_tag_2)).setText(tag2.getTagName());
        Tag tag3 = new Tag(2, "Peliculas");
        ((TextView) findViewById(R.id.tv_tag_3)).setText(tag3.getTagName());
        Tag tag4 = new Tag(3, "Androidizate");
        ((TextView) findViewById(R.id.tv_tag_4)).setText(tag4.getTagName());

        ((TextView) findViewById(R.id.tv_tags_quantity)).setText("Cantidad de Tags: " + db.tagDao().getAll().size());

        db.tagDao().insertAll(tag1, tag2, tag3, tag4);

        // Creamos los ToDos
        Todo todo1 = new Todo("Asado", 0);
        Todo todo2 = new Todo("Samsung S8", 0);
        Todo todo3 = new Todo("Auto", 0);

        Todo todo4 = new Todo("Bourne", 0);
        Todo todo5 = new Todo("Batman vs Superman", 0);
        Todo todo6 = new Todo("Guardianes de la Galaxia 2", 0);
        Todo todo7 = new Todo("Star Wars: Episode IV", 0);

        Todo todo8 = new Todo("Llamar a reservar el after", 0);
        Todo todo9 = new Todo("Sacar plata del cajero", 0);

        Todo todo10 = new Todo("Crear una nueva app", 0);
        Todo todo11 = new Todo("Estudiar mucho", 0);

        // Insertamos todos en la base de datos
        db.todoDao().insertAll(todo1, todo2, todo3, todo4, todo5, todo6, todo7, todo8, todo9, todo10, todo11);

        // Insertamos todos bajo el tag "Shopping"
        db.todoTagDao().insertTodoTags(new TodoTag(todo1.getId(), 1), new TodoTag(todo2.getId(), 1), new TodoTag(todo3.getId(), 1));

        // Insertamos todos bajo el tag "Peliculas" Tag
        db.todoTagDao().insertTodoTags(new TodoTag(todo4.getId(), 3), new TodoTag(todo5.getId(), 3), new TodoTag(todo6.getId(), 3), new TodoTag(todo7.getId(), 3));

        // Insertamos todos bajo el tag "Importante" Tag
        db.todoTagDao().insertTodoTags(new TodoTag(todo8.getId(), 2), new TodoTag(todo9.getId(), 2));

        // Insertamos todos bajo el tag "Androidizate"
        db.todoTagDao().insertTodoTags(new TodoTag(todo10.getId(), 4), new TodoTag(todo11.getId(), 4));

        fillTodos();

        ((TextView) findViewById(R.id.tv_todos_quantity)).setText("Cantidad de Todos: " + db.todoDao().getAll().size());

        // "Crear una nueva app" - la asignamos como "Importante"
        // Ahora va a tener 2 tags - "Androidizate" e "Importante"
        db.todoTagDao().insertTodoTags(new TodoTag(todo10.getId(), 2));

        // Buscamos todos los nombres de tags
        Log.d("Buscar Tags", "Buscamos todos los Tags");
        List<Tag> allTags = db.tagDao().getAll();
        for (Tag tag : allTags) {
            Log.d("Nombre del Tag", tag.getTagName());
        }

        // Buscamos todos los Todos
        Log.d("Buscar Todos", "Buscamos todos los ToDos");
        List<Todo> allToDos = db.todoDao().getAll();
        for (Todo todo : allToDos) {
            Log.d("ToDo", todo.getNote());
        }

        // Buscamos todos los todos bajo el tag "Peliculas"
        Log.d("ToDo", "Buscamos todos los ToDos bajo un Tag");
        List<Todo> moviesList = db.todoDao().getTodosByTag(tag3.getTagName());
        for (Todo todo : moviesList) {
            Log.d("ToDo Peliculas", todo.getNote());
        }

        // Borramos un ToDo
        Log.d("Borrar ToDo", "Borramos un Todo");
        Log.d("Cantidad de ToDos", "Cantidad de ToDos antes de elimnar el ToDo: " + db.todoDao().getAll().size());
        db.todoDao().delete(db.todoDao().findById(8));
        Log.d("Cantidad de ToDos", "Cantidad de ToDos despues de elimnar el ToDo: " + db.todoDao().getAll().size());

        // Eliminamos todos los ToDos del tag "Shopping"
        Log.d("Cantidad de ToDos", "Cantidad de ToDos antes de eliminar el tag 'Shopping': " + db.todoDao().getAll().size());
        db.tagDao().delete(db.tagDao().findById(1));
        Log.d("Cantidad de ToDos", "Cantidad de ToDos despues de elimnar el tag 'Shopping': " + db.todoDao().getAll().size());

        // Actualizamos el nombre del tag
        tag3.setTagName("Peliculas a mirar");
        db.tagDao().updateTags(tag3);
    }

    private void fillTodos() {
        fillTodo(R.id.tv_todo_1, db.todoDao().findById(1), db.todoTagDao().findById(1));
        fillTodo(R.id.tv_todo_2, db.todoDao().findById(2), db.todoTagDao().findById(2));
        fillTodo(R.id.tv_todo_3, db.todoDao().findById(3), db.todoTagDao().findById(3));
        fillTodo(R.id.tv_todo_4, db.todoDao().findById(4), db.todoTagDao().findById(4));
        fillTodo(R.id.tv_todo_5, db.todoDao().findById(5), db.todoTagDao().findById(5));
        fillTodo(R.id.tv_todo_6, db.todoDao().findById(6), db.todoTagDao().findById(6));
        fillTodo(R.id.tv_todo_7, db.todoDao().findById(7), db.todoTagDao().findById(7));
        fillTodo(R.id.tv_todo_8, db.todoDao().findById(8), db.todoTagDao().findById(8));
        fillTodo(R.id.tv_todo_9, db.todoDao().findById(9), db.todoTagDao().findById(9));
        fillTodo(R.id.tv_todo_10, db.todoDao().findById(10), db.todoTagDao().findById(10));
        fillTodo(R.id.tv_todo_11, db.todoDao().findById(11), db.todoTagDao().findById(11));
    }

    private void fillTodo(int resource, Todo todo, List<TodoTag> todoTags) {
        String newText = null;
        for (int i = 0; i < todoTags.size(); i++) {
            Tag tag = db.tagDao().findById(todoTags.get(i).getTagId());
            if (i == 0) {
                newText = todo.getNote() + " - " + tag.getId();
            } else {
                newText = newText + ", " + tag.getTagName();
            }
        }
        ((TextView) findViewById(resource)).setText(newText);
    }
}
