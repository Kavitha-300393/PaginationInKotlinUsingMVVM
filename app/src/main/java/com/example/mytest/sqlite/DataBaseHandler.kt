package com.example.mytest.sqlite

import android.database.sqlite.SQLiteOpenHelper
import com.example.mytest.sqlite.DataBaseHandler
import android.database.sqlite.SQLiteDatabase
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.util.Log

class DataBaseHandler(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    // Creating Tables
    override fun onCreate(db: SQLiteDatabase) {
        val SQL_CREATE_TABLE_MASTER_DATA = "CREATE TABLE IF NOT EXISTS " +
                TABLE_ROOM + " (" +
                KEY_NAME + TEXT_TYPE + COMMA_SEP +
                KEY_islive + TEXT_TYPE + COMMA_SEP +
                KEY_DATE + TEXT_TYPE + ")"
        db.execSQL(SQL_CREATE_TABLE_MASTER_DATA)
    }

    // Upgrading database
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ROOM)

        // Create tables again
        onCreate(db)
    }

    fun insert_rooms(name: String, islive: String, date: String): Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(KEY_NAME, name)
        values.put(KEY_islive, islive)
        values.put(KEY_DATE, date)
        Log.v("dd>>", "$name $islive $date")
        return db.insert(TABLE_ROOM, null, values)
    }

    fun updateRoom(name: String, islive: String?) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(KEY_islive, islive)
        db.update(TABLE_ROOM, values, KEY_NAME + " = ?", arrayOf(name))
    }

    fun select_MDP(): Cursor {
        val db = this.writableDatabase
        return db.rawQuery(" SELECT * FROM " + TABLE_ROOM, null)
    }

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "RoomManager"
        private const val TABLE_ROOM = "rooms"
        private const val KEY_NAME = "name"
        private const val KEY_islive = "islive"
        private const val KEY_DATE = "date"
        private const val TEXT_TYPE = " TEXT"
        private const val COMMA_SEP = ","
    }
}