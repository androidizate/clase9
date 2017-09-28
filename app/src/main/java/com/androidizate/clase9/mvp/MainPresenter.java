package com.androidizate.clase9.mvp;

import com.androidizate.clase9.model.DaoSession;
import com.androidizate.clase9.model.Tag;
import com.androidizate.clase9.model.Todo;
import com.androidizate.clase9.model.TodoTag;
import com.androidizate.clase9.utils.FileUtils;
import com.androidizate.clase9.utils.PreferencesUtil;

import java.util.List;

/**
 * @author Andres Oller
 */

public class MainPresenter {

    private MainView view;
    private MainModel model;

    public MainPresenter(MainView view, DaoSession daoSession, PreferencesUtil preferencesUtil, FileUtils fileUtils) {
        this.view = view;
        this.model = new MainModel(daoSession, preferencesUtil, fileUtils);
    }


    public void loadData() {
        model.populateDb();
        model.createFiles();
        model.registerUser();

        fillTodos(model.getAllTodos(), model.getAllTags(), model.getAllTodoTag());
    }

    private void fillTodos(List<Todo> todoList, List<Tag> tagList, List<TodoTag> todoTagList) {
        String newText = null;
        int todoIndex = 0;
        for (Todo todo : todoList) {
            int i = 0;
            for (Tag tag : tagList) {
                for (TodoTag todoTag : todoTagList)
                    if (todo.getId() == todoTag.getTodoId() && tag.getId() == todoTag.getTagId()) {
                        if (i == 0) {
                            newText = todo.getNote() + " - " + tag.getTagName();
                        } else {
                            newText = newText + ", " + tag.getTagName();
                        }
                        i++;
                    }

            }
            view.fillTodo(todoIndex, newText);
            todoIndex++;
        }
    }

    public void deleteData() {
        model.deleteAll();
    }
}
