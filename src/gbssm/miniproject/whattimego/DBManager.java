package gbssm.miniproject.whattimego;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;

public class DBManager extends SQLiteOpenHelper {

	public static int DB_VERSION = 4;
	
	public DBManager(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		// 새 테이블 생성
		db.execSQL("CREATE TABLE SCHEDULE_LIST( s_id INTEGER PRIMARY KEY AUTOINCREMENT, s_name TEXT, s_dest TEXT, s_addr TEXT, s_time TEXT, s_day TEXT, repeat TEXT, onoff TEXT, lati TEXT, longi TEXT);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		Log.d("DB version up", oldVersion + " -> " + newVersion);

		switch (oldVersion) {
		case 1:
			try {
				db.beginTransaction();
				db.execSQL("ALTER TABLE SCHEDULE_LIST ADD COLUMN onoff TEXT DEFAULT 'on'");
				db.setTransactionSuccessful();
			} catch (IllegalStateException e) {
				Log.e("DB", "err");
			} finally {
				db.endTransaction();
			}
			break;

		case 2:
			try {
				db.beginTransaction();
				db.execSQL("ALTER TABLE SCHEDULE_LIST ADD COLUMN lati TEXT DEFAULT '0'");
				db.execSQL("ALTER TABLE SCHEDULE_LIST ADD COLUMN longi TEXT DEFAULT '0'");
				db.setTransactionSuccessful();
			} catch (IllegalStateException e) {
				Log.e("DB", "err");
			} finally {
				db.endTransaction();
			}
			break;
			
		case 3:
			try {
				db.beginTransaction();
				db.execSQL("ALTER TABLE SCHEDULE_LIST ADD COLUMN lati TEXT DEFAULT '0'");
				db.execSQL("ALTER TABLE SCHEDULE_LIST ADD COLUMN longi TEXT DEFAULT '0'");
				db.setTransactionSuccessful();
			} catch (IllegalStateException e) {
				Log.e("DB", "err");
			} finally {
				db.endTransaction();
			}
			break;
		}
	}

	public void insert(String _query) {
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(_query);
		db.close();
	}

	public void update(String _query) {
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(_query);
		db.close();
	}

	public void delete(String _query) {
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(_query);
		db.close();
	}

	public Cursor select(String _query) {
		SQLiteDatabase db = getWritableDatabase();

		Cursor cursor = db.rawQuery(_query, null);

		return cursor;
	}

	public String PrintData() {
		SQLiteDatabase db = getReadableDatabase();
		String str = "";

		Cursor cursor = db.rawQuery("select * from SCHEDULE_LIST", null);
		while (cursor.moveToNext()) {
			str += cursor.getInt(0) + " : s_name = " + cursor.getString(1)
					+ ", s_dest = " + cursor.getString(2) + ", s_addr = "
					+ cursor.getString(3) + ", s_time = " + cursor.getString(4)
					+ ", s_day = " + cursor.getString(5) + ", repeat = "
					+ cursor.getString(6) + "\n";
		}

		return str;
	}
}
