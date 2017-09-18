package com.androidizate.clase9;

import android.app.Application;

import com.androidizate.clase9.model.DaoMaster;
import com.androidizate.clase9.model.DaoSession;

import org.greenrobot.greendao.database.Database;

/**
 * @author Andres Oller
 */

public class MyApplication extends Application {

    DaoSession daoSession;
    DaoMaster daoMaster;

    @Override
    public void onCreate() {
        super.onCreate();
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "todo");
        Database db = helper.getWritableDb();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    public DaoMaster getDaoMaster() {
        return daoMaster;
    }
}
