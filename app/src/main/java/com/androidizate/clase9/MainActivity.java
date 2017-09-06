package com.androidizate.clase9;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.androidizate.clase9.helper.DatabaseHelper;
import com.androidizate.clase9.model.Tag;
import com.androidizate.clase9.model.Todo;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

import static com.androidizate.clase9.helper.DatabaseHelper.DATABASE_NAME;

public class MainActivity extends AppCompatActivity {

    // Database Helper
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        db = new DatabaseHelper(getApplicationContext());
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
        Tag tag1 = new Tag("Shopping");
        ((TextView) findViewById(R.id.tv_tag_1)).setText(tag1.getTagName());
        Tag tag2 = new Tag("Importante");
        ((TextView) findViewById(R.id.tv_tag_2)).setText(tag2.getTagName());
        Tag tag3 = new Tag("Peliculas");
        ((TextView) findViewById(R.id.tv_tag_3)).setText(tag3.getTagName());
        Tag tag4 = new Tag("Androidizate");
        ((TextView) findViewById(R.id.tv_tag_4)).setText(tag4.getTagName());

        // Insertamos los tags en la base de datos
        long tag1_id = db.createTag(tag1);
        long tag2_id = db.createTag(tag2);
        long tag3_id = db.createTag(tag3);
        long tag4_id = db.createTag(tag4);

        ((TextView) findViewById(R.id.tv_tags_quantity)).setText("Cantidad de Tags: " + db.getAllTags().size());

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
        // Insertamos todos bajo el tag "Shopping"
        long todo1_id = db.createToDo(todo1, new long[]{tag1_id});
        long todo2_id = db.createToDo(todo2, new long[]{tag1_id});
        long todo3_id = db.createToDo(todo3, new long[]{tag1_id});

        // Insertamos todos bajo el tag "Peliculas" Tag
        long todo4_id = db.createToDo(todo4, new long[]{tag3_id});
        long todo5_id = db.createToDo(todo5, new long[]{tag3_id});
        long todo6_id = db.createToDo(todo6, new long[]{tag3_id});
        long todo7_id = db.createToDo(todo7, new long[]{tag3_id});

        // Insertamos todos bajo el tag "Importante" Tag
        long todo8_id = db.createToDo(todo8, new long[]{tag2_id});
        long todo9_id = db.createToDo(todo9, new long[]{tag2_id});

        // Insertamos todos bajo el tag "Androidizate"
        long todo10_id = db.createToDo(todo10, new long[]{tag4_id});
        long todo11_id = db.createToDo(todo11, new long[]{tag4_id});

        fillTodos();

        ((TextView) findViewById(R.id.tv_todos_quantity)).setText("Cantidad de Todos: " + db.getToDoCount());

        // "Crear una nueva app" - la asignamos como "Importante"
        // Ahora va a tener 2 tags - "Androidizate" e "Importante"
        db.createTodoTag(todo10_id, tag2_id);

        // Buscamos todos los nombres de tags
        Log.d("Buscar Tags", "Buscamos todos los Tags");
        List<Tag> allTags = db.getAllTags();
        for (Tag tag : allTags) {
            Log.d("Nombre del Tag", tag.getTagName());
        }

        // Buscamos todos los Todos
        Log.d("Buscar Todos", "Buscamos todos los ToDos");
        List<Todo> allToDos = db.getAllToDos();
        for (Todo todo : allToDos) {
            Log.d("ToDo", todo.getNote());
        }

        // Buscamos todos los todos bajo el tag "Peliculas"
        Log.d("ToDo", "Buscamos todos los ToDos bajo un Tag");
        List<Todo> moviesList = db.getAllToDosByTag(tag3.getTagName());
        for (Todo todo : moviesList) {
            Log.d("ToDo Peliculas", todo.getNote());
        }

        // Borramos un ToDo
        Log.d("Borrar ToDo", "Borramos un Todo");
        Log.d("Cantidad de ToDos", "Cantidad de ToDos antes de elimnar el ToDo: " + db.getToDoCount());
        db.deleteToDo(todo8_id);
        Log.d("Cantidad de ToDos", "Cantidad de ToDos despues de elimnar el ToDo: " + db.getToDoCount());

        // Eliminamos todos los ToDos del tag "Shopping"
        Log.d("Cantidad de ToDos", "Cantidad de ToDos antes de eliminar el tag 'Shopping': " + db.getToDoCount());
        db.deleteTag(tag1, true);
        Log.d("Cantidad de ToDos", "Cantidad de ToDos despues de elimnar el tag 'Shopping': " + db.getToDoCount());

        // Actualizamos el nombre del tag
        tag3.setTagName("Peliculas a mirar");
        db.updateTag(tag3);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // borramos la base de datos y cerramos la conexion
        getApplicationContext().deleteDatabase(DATABASE_NAME);
        db.closeDB();
    }

    private void fillTodos() {
        fillTodo(R.id.tv_todo_1, db.getTodo(1), db.getAllTagsForTodo(1));
        fillTodo(R.id.tv_todo_2, db.getTodo(2), db.getAllTagsForTodo(2));
        fillTodo(R.id.tv_todo_3, db.getTodo(3), db.getAllTagsForTodo(3));
        fillTodo(R.id.tv_todo_4, db.getTodo(4), db.getAllTagsForTodo(4));
        fillTodo(R.id.tv_todo_5, db.getTodo(5), db.getAllTagsForTodo(5));
        fillTodo(R.id.tv_todo_6, db.getTodo(6), db.getAllTagsForTodo(6));
        fillTodo(R.id.tv_todo_7, db.getTodo(7), db.getAllTagsForTodo(7));
        fillTodo(R.id.tv_todo_8, db.getTodo(8), db.getAllTagsForTodo(8));
        fillTodo(R.id.tv_todo_9, db.getTodo(9), db.getAllTagsForTodo(9));
        fillTodo(R.id.tv_todo_10, db.getTodo(10), db.getAllTagsForTodo(10));
        fillTodo(R.id.tv_todo_11, db.getTodo(11), db.getAllTagsForTodo(11));
    }

    private void fillTodo(int resource, Todo todo, List<Tag> tags) {
        String newText = null;
        for (int i = 0; i < tags.size(); i++) {
            if (i == 0) {
                newText = todo.getNote() + " - " + tags.get(i).getTagName();
            } else {
                newText = newText + ", " + tags.get(i).getTagName();
            }
        }
        ((TextView) findViewById(resource)).setText(newText);
    }
}
