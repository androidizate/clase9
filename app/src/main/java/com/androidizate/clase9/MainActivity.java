package com.androidizate.clase9;

import android.content.SharedPreferences;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.androidizate.clase9.model.DaoSession;
import com.androidizate.clase9.model.Tag;
import com.androidizate.clase9.model.Todo;
import com.androidizate.clase9.model.TodoTag;

import org.greenrobot.greendao.AbstractDao;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    DaoSession daoSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        daoSession = ((MyApplication) getApplication()).getDaoSession();
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
        Tag tag1 = new Tag();
        tag1.setTagName("Shopping");
        ((TextView) findViewById(R.id.tv_tag_1)).setText(tag1.getTagName());
        Tag tag2 = new Tag();
        tag2.setTagName("Importante");
        ((TextView) findViewById(R.id.tv_tag_2)).setText(tag2.getTagName());
        Tag tag3 = new Tag();
        tag3.setTagName("Peliculas");
        ((TextView) findViewById(R.id.tv_tag_3)).setText(tag3.getTagName());
        Tag tag4 = new Tag();
        tag4.setTagName("Androidizate");
        ((TextView) findViewById(R.id.tv_tag_4)).setText(tag4.getTagName());

        try {
            daoSession.insert(tag1);
            daoSession.insert(tag2);
            daoSession.insert(tag3);
            daoSession.insert(tag4);
        } catch (SQLiteConstraintException exception) {
            exception.printStackTrace();
        }

        ((TextView) findViewById(R.id.tv_tags_quantity)).setText("Cantidad de Tags: " + daoSession.getTagDao().count());

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

        ((TextView) findViewById(R.id.tv_todos_quantity)).setText("Cantidad de Todos: " + daoSession.getTodoDao().count());

        // "Crear una nueva app" - la asignamos como "Importante"
        // Ahora va a tener 2 tags - "Androidizate" e "Importante"
        TodoTag todoTag12 = new TodoTag();
        todoTag12.setTodoId(todo10.getId());
        todoTag12.setTagId(tag2.getId());

        daoSession.getTodoTagDao().insert(todoTag12);

        // Buscamos todos los nombres de tags
        Log.d("Buscar Tags", "Buscamos todos los Tags");
        List<Tag> allTags = daoSession.getTagDao().loadAll();
        for (Tag tag : allTags) {
            Log.d("Nombre del Tag", tag.getTagName());
        }

        // Buscamos todos los Todos
        Log.d("Buscar Todos", "Buscamos todos los ToDos");
        List<Todo> allToDos = daoSession.getTodoDao().loadAll();
        for (Todo todo : allToDos) {
            Log.d("ToDo", todo.getNote());
        }

        // Buscamos todos los todos bajo el tag "Peliculas"
        Log.d("ToDo", "Buscamos todos los ToDos bajo un Tag");
        List<Todo> moviesList = daoSession.getTodoDao()._queryTag_Todos(tag3.getId());
        for (Todo todo : moviesList) {
            Log.d("ToDo Peliculas", todo.getNote());
        }

        fillTodo(R.id.tv_todo_1, daoSession.getTodoDao().load(todo1.getId()), daoSession.getTodoTagDao().loadAll());
        fillTodo(R.id.tv_todo_2, daoSession.getTodoDao().load(todo2.getId()), daoSession.getTodoTagDao().loadAll());
        fillTodo(R.id.tv_todo_3, daoSession.getTodoDao().load(todo3.getId()), daoSession.getTodoTagDao().loadAll());
        fillTodo(R.id.tv_todo_4, daoSession.getTodoDao().load(todo4.getId()), daoSession.getTodoTagDao().loadAll());
        fillTodo(R.id.tv_todo_5, daoSession.getTodoDao().load(todo5.getId()), daoSession.getTodoTagDao().loadAll());
        fillTodo(R.id.tv_todo_6, daoSession.getTodoDao().load(todo6.getId()), daoSession.getTodoTagDao().loadAll());
        fillTodo(R.id.tv_todo_7, daoSession.getTodoDao().load(todo7.getId()), daoSession.getTodoTagDao().loadAll());
        fillTodo(R.id.tv_todo_8, daoSession.getTodoDao().load(todo8.getId()), daoSession.getTodoTagDao().loadAll());
        fillTodo(R.id.tv_todo_9, daoSession.getTodoDao().load(todo9.getId()), daoSession.getTodoTagDao().loadAll());
        fillTodo(R.id.tv_todo_10, daoSession.getTodoDao().load(todo10.getId()), daoSession.getTodoTagDao().loadAll());
        fillTodo(R.id.tv_todo_11, daoSession.getTodoDao().load(todo11.getId()), daoSession.getTodoTagDao().loadAll());

        // Borramos un ToDo
        Log.d("Borrar ToDo", "Borramos un Todo");
        Log.d("Cantidad de ToDos", "Cantidad de ToDos antes de elimnar el ToDo: " + daoSession.getTodoDao().count());
        daoSession.getTodoDao().delete(todo8);
        Log.d("Cantidad de ToDos", "Cantidad de ToDos despues de elimnar el ToDo: " + daoSession.getTodoDao().count());

        // Eliminamos todos los ToDos del tag "Shopping"
        Log.d("Cantidad de ToDos", "Cantidad de ToDos antes de eliminar el tag 'Shopping': " + daoSession.getTagDao().count());
        daoSession.getTagDao().delete(tag1);
        Log.d("Cantidad de ToDos", "Cantidad de ToDos despues de elimnar el tag 'Shopping': " + daoSession.getTodoDao().count());

        // Actualizamos el nombre del tag
        tag3.setTagName("Peliculas a mirar");
        daoSession.getTagDao().update(tag3);
    }

    private void fillTodo(int resource, Todo todo, List<TodoTag> todoTags) {
        String newText = null;
        int i = 0;
        for (TodoTag todoTag : todoTags) {
            if (todo.getId() == todoTag.getTodoId()) {
                Tag tag = daoSession.getTagDao().load(todoTag.getTagId());
                if (i == 0) {
                    newText = todo.getNote() + " - " + tag.getTagName();
                } else {
                    newText = newText + ", " + tag.getTagName();
                }
                i++;
            }
        }
        ((TextView) findViewById(resource)).setText(newText);
    }

    @Override
    protected void onStop() {
        super.onStop();
        for (AbstractDao dao : daoSession.getAllDaos()) {
            dao.deleteAll();
        }
    }
}
