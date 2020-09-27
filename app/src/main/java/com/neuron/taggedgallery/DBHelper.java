package com.neuron.taggedgallery;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    public static final int DB_VERSION = 4;

    public static final String TABLE_IMAGES = "images";
    public static final String TABLE_TAGS = "tags";
    public static final String TABLE_TAGMAP = "tagmap";

    public static final String KEY_IMAGE_ID = "imgId";
    public static final String KEY_TAG_ID = "tagId";
    public static final String KEY_TAGMAP_ID = "tagmapId";

    public static final String KEY_NAME = "name";
    public static final String KEY_URI = "uri";

    public DBHelper(@Nullable Context context, @Nullable String name) {
        super(context, name, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_IMAGES + " (" +
                KEY_IMAGE_ID + " INTEGER PRIMARY KEY," +
                KEY_NAME + " TEXT," +
                KEY_URI + " TEXT)");

        db.execSQL("create table " + TABLE_TAGS + " (" +
                KEY_TAG_ID + " INTEGER PRIMARY KEY," +
                KEY_IMAGE_ID + " INTEGER," +
                KEY_NAME + " TEXT)");

        db.execSQL("create table " + TABLE_TAGMAP + " (" +
                KEY_TAGMAP_ID + " INTEGER PRIMARY KEY," +
                KEY_TAG_ID + " INTEGER," +
                KEY_IMAGE_ID + " INTEGER)");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.
//        db.execSQL("DELETE FROM " + TABLE_IMAGES);
//        db.execSQL("DELETE FROM " + TABLE_TAGS);
//        db.execSQL("DELETE FROM " + TABLE_TAGMAP);
    }
}
