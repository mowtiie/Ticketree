package com.mowtiie.ticketree.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

    public DbHelper(Context context) {
        super(context, "ticketree.db", null, 6);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE users (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, "
                + "email TEXT UNIQUE, password TEXT, organizer INTEGER, phone TEXT DEFAULT '', about TEXT DEFAULT '', "
                + "birthdate TEXT DEFAULT '', address TEXT DEFAULT '')");
        insertDemoUsers(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("DROP TABLE IF EXISTS users");
            onCreate(db);
            return;
        }
        if (oldVersion < 4) {
            db.execSQL("ALTER TABLE users ADD COLUMN address TEXT DEFAULT ''");
        }
        if (oldVersion < 6) {
            db.execSQL("UPDATE users SET phone='09171234567' WHERE email='" + Data.DEMO_CUSTOMER_EMAIL + "'");
        }
        if (oldVersion < 5) {
            db.execSQL("ALTER TABLE users ADD COLUMN birthdate TEXT DEFAULT ''");
            db.execSQL("UPDATE users SET birthdate='Jan 15, 2000', address='123 Rizal Street, Quezon City' "
                    + "WHERE email IN ('" + Data.DEMO_CUSTOMER_EMAIL + "', '" + Data.DEMO_ORGANIZER_EMAIL + "')");
        }
        insertDemoUsers(db);
    }

    void insertDemoUsers(SQLiteDatabase db) {
        db.execSQL("INSERT OR IGNORE INTO users (name, email, password, organizer, phone, birthdate, address) VALUES "
                + "('Demo Customer', '" + Data.DEMO_CUSTOMER_EMAIL + "', '" + Data.DEMO_PASSWORD + "', 0, "
                + "'09171234567', 'Jan 15, 2000', '123 Rizal Street, Quezon City')");
        db.execSQL("INSERT OR IGNORE INTO users (name, email, password, organizer, phone, about, birthdate, address) VALUES "
                + "('Demo Organizer', '" + Data.DEMO_ORGANIZER_EMAIL + "', '" + Data.DEMO_PASSWORD + "', 1, "
                + "'0917 555 0100', 'Demo organization running sample events for the Code Fest pitch.', "
                + "'Jan 15, 2000', '123 Rizal Street, Quezon City')");
    }
}
