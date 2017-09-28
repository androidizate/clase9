package com.androidizate.clase9;

import android.app.Application;
import android.content.Context;

import com.androidizate.clase9.model.DaoMaster;
import com.androidizate.clase9.model.DaoSession;

import org.greenrobot.greendao.database.Database;

/**
 * @author Andres Oller
 */

public class MyApplication extends Application {

    private static Context context;
    private DaoSession daoSession;
    private DaoMaster daoMaster;

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "todo");
        Database db = helper.getWritableDb();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        context = getApplicationContext();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    public DaoMaster getDaoMaster() {
        return daoMaster;
    }
}
