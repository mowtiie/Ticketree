package com.mowtiie.ticketree.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

    public DbHelper(Context context) {
        super(context, "ticketree.db", null, 3);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE users (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, "
                + "email TEXT UNIQUE, password TEXT, organizer INTEGER, phone TEXT DEFAULT '', about TEXT DEFAULT '')");
        insertDemoUsers(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("DROP TABLE IF EXISTS users");
            onCreate(db);
        } else {
            insertDemoUsers(db);
        }
    }

    void insertDemoUsers(SQLiteDatabase db) {
        db.execSQL("INSERT OR IGNORE INTO users (name, email, password, organizer) VALUES "
                + "('Demo Customer', '" + Data.DEMO_CUSTOMER_EMAIL + "', '" + Data.DEMO_PASSWORD + "', 0)");
        db.execSQL("INSERT OR IGNORE INTO users (name, email, password, organizer, phone, about) VALUES "
                + "('Demo Organizer', '" + Data.DEMO_ORGANIZER_EMAIL + "', '" + Data.DEMO_PASSWORD + "', 1, "
                + "'0917 555 0100', 'Demo organization running sample events for the Code Fest pitch.')");
    }
}
