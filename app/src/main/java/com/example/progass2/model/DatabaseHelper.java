package com.example.progass2.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "profiles_db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_PROFILE = "Profile";
    private static final String TABLE_ACCESS = "Access";

    private static final String KEY_PROFILE_ID = "profileId";
    private static final String KEY_SURNAME = "surname";
    private static final String KEY_NAME = "name";
    private static final String KEY_GPA = "gpa";
    private static final String KEY_CREATION_DATE = "creationDate";

    private static final String KEY_ACCESS_ID = "accessId";
    private static final String KEY_ACCESS_PROFILE_ID = "profileId";
    private static final String KEY_ACCESS_TYPE = "accessType";
    private static final String KEY_TIMESTAMP = "timeStamp";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_PROFILE_TABLE = "CREATE TABLE " + TABLE_PROFILE + "("
                + KEY_PROFILE_ID + " INTEGER PRIMARY KEY,"
                + KEY_SURNAME + " TEXT,"
                + KEY_NAME + " TEXT,"
                + KEY_GPA + " REAL,"
                + KEY_CREATION_DATE + " TEXT" + ")";

        String CREATE_ACCESS_TABLE = "CREATE TABLE " + TABLE_ACCESS + " (" +
                KEY_ACCESS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KEY_ACCESS_PROFILE_ID + " INTEGER, " +
                KEY_ACCESS_TYPE + " TEXT, " +
                KEY_TIMESTAMP + " TEXT " + ")";

        db.execSQL(CREATE_PROFILE_TABLE);
        db.execSQL(CREATE_ACCESS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROFILE);
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCESS);
//        onCreate(db);
    }

    public void addProfile(Profile profile) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_PROFILE_ID, profile.getStudentId());
        values.put(KEY_NAME, profile.getName());
        values.put(KEY_SURNAME, profile.getSurname());
        values.put(KEY_GPA, profile.getGpa());
        values.put(KEY_CREATION_DATE, profile.getCreationDate());

        db.insert(TABLE_PROFILE, null, values);
        db.close();

        addAccess(new Access(profile.getStudentId(), "created"));
    }

    public void addAccess(Access access) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_ACCESS_PROFILE_ID, access.getStudentId());
        values.put(KEY_ACCESS_TYPE, access.getAccessType());
        values.put(KEY_TIMESTAMP, access.getTimeStamp());

        db.insert(TABLE_ACCESS, null, values);
        db.close();
    }

    public List<Profile> getAllProfilesByName() {
        List<Profile> profileList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PROFILE +
                " ORDER BY " + KEY_SURNAME + " ASC", null);

        if (cursor.moveToFirst()) {
            do {
                Profile profile = new Profile(
                        cursor.getInt(cursor.getColumnIndexOrThrow(KEY_PROFILE_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_SURNAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME)),
                        cursor.getFloat(cursor.getColumnIndexOrThrow(KEY_GPA)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_CREATION_DATE))
                );
                profileList.add(profile);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return profileList;
    }

    public List<Profile> getAllProfilesById() {
        List<Profile> profileList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PROFILE +
                " ORDER BY " + KEY_PROFILE_ID + " ASC", null);

        if (cursor.moveToFirst()) {
            do {
                Profile profile = new Profile(
                        cursor.getInt(cursor.getColumnIndexOrThrow(KEY_PROFILE_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_SURNAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME)),
                        cursor.getFloat(cursor.getColumnIndexOrThrow(KEY_GPA)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_CREATION_DATE))
                );
                profileList.add(profile);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return profileList;
    }

    public List<Access> getAccessHistoryForProfile(int profileId) {
        List<Access> accessList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_ACCESS +
                        " WHERE " + KEY_ACCESS_PROFILE_ID + " = ? ORDER BY " + KEY_ACCESS_ID + " ASC",
                new String[]{String.valueOf(profileId)});

        if (cursor.moveToFirst()) {
            do {
                Access access = new Access(
                        cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ACCESS_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ACCESS_PROFILE_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_ACCESS_TYPE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_TIMESTAMP))
                );
                accessList.add(access);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return accessList;
    }

    public void deleteProfile(int profileId) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.delete(TABLE_PROFILE, KEY_PROFILE_ID + " = ?", new String[]{String.valueOf(profileId)});

            addAccess(new Access(profileId, "deleted"));
        } finally {
            db.close();
        }
    }

    public int getProfileCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_PROFILE, null);
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return count;
    }

    public boolean profileExists(int profileId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + KEY_PROFILE_ID + " FROM " + TABLE_PROFILE + " WHERE " + KEY_PROFILE_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(profileId)});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    public Profile getProfileById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.query(TABLE_PROFILE,
                    new String[]{KEY_PROFILE_ID, KEY_SURNAME, KEY_NAME, KEY_GPA, KEY_CREATION_DATE},
                    KEY_PROFILE_ID + "=?",
                    new String[]{String.valueOf(id)}, null, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                Profile profile = new Profile(
                        cursor.getInt(cursor.getColumnIndexOrThrow(KEY_PROFILE_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_SURNAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME)),
                        cursor.getFloat(cursor.getColumnIndexOrThrow(KEY_GPA))
                );
                profile.setCreationDate(cursor.getString(cursor.getColumnIndexOrThrow(KEY_CREATION_DATE)));
                return profile;
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return null;
    }


}
