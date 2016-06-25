package practice.wardrobe;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import practice.wardrobe.model.DaoMaster;
import practice.wardrobe.model.DaoSession;

public class DatabaseManager {

    private static DaoSession daoSessionMain;
    public static final String DB_NAME = "database";

    /**
     * Private constructor throwing exception to ensure that DatabaseManager is
     * not re-initialized
     *
     * @throws Exception
     */
    private DatabaseManager() {
    }

    public static DaoSession getDaoSessionMain() {
        if (daoSessionMain == null) {
            throw new Error("DatabaseManager.init(context) not called");
        } else
            return daoSessionMain;
    }

    private static DaoMaster.DevOpenHelper helperInstance;

    public static void init(Context context) {
        // init db
        if (helperInstance == null)
            helperInstance = new DaoMaster.DevOpenHelper(context.getApplicationContext(), DB_NAME, null);
        SQLiteDatabase db = helperInstance.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSessionMain = daoMaster.newSession();
    }

    /**
     * Closes any open DB Instance.
     */
    public static void closeDataBase() {
        if (helperInstance != null) {
            helperInstance.close();
        }
    }
}
