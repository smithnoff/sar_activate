package logica;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by RSMAPP on 16/12/2015.
 */
public class SqliteDBHelper extends SQLiteOpenHelper {
    public static final int version=1;
    public static final String name="SoyActivistaDB_test";

    public SqliteDBHelper(Context context) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
