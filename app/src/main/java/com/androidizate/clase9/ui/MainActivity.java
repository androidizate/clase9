package com.androidizate.clase9.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.androidizate.clase9.MyApplication;
import com.androidizate.clase9.R;
import com.androidizate.clase9.mvp.MainPresenter;
import com.androidizate.clase9.mvp.MainView;
import com.androidizate.clase9.utils.FileUtils;
import com.androidizate.clase9.utils.PreferencesUtil;

public class MainActivity extends AppCompatActivity implements MainView {

    MainPresenter presenter;

    int[] todoTextViewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        presenter = new MainPresenter(this, ((MyApplication) getApplication()).getDaoSession(), new PreferencesUtil(this), new FileUtils(this));
        todoTextViewList = new int[]{R.id.tv_todo_1, R.id.tv_todo_2, R.id.tv_todo_3, R.id.tv_todo_4, R.id.tv_todo_5, R.id.tv_todo_6, R.id.tv_todo_7,
                R.id.tv_todo_8, R.id.tv_todo_9, R.id.tv_todo_10, R.id.tv_todo_11};
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.loadData();
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.deleteData();
    }

    @Override
    public void fillTodo(int index, String text) {
        ((TextView) findViewById(todoTextViewList[index])).setText(text);
    }
}
