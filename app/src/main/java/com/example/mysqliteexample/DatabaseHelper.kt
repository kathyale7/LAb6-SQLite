package com.example.mysqliteexample

import android.content.Context
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

/**
 * Let's start by creating our database CRUD helper class
 * based on the SQLiteHelper.
 */
class DatabaseHelper(context: Context) :
        SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {

    /**
     * Our onCreate() method.
     * Called when the database is created for the first time. This is
     * where the creation of tables and the initial population of the tables
     * should happen.
     */
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE $TABLE_NAME (ID TEXT PRIMARY KEY " +
                ",NAME TEXT,SURNAME TEXT,AGE TEXT)")
        db.execSQL("CREATE TABLE $TABLE_NAME2 (ID TEXT PRIMARY KEY " +
                ",DESCRIPTION TEXT,CREDIT TEXT)")
        db.execSQL("CREATE TABLE $TABLE_NAME3 (user TEXT PRIMARY KEY " +
                ",password TEXT)")

    }

    /**
     * Let's create Our onUpgrade method
     * Called when the database needs to be upgraded. The implementation should
     * use this method to drop tables, add tables, or do anything else it needs
     * to upgrade to the new schema version.
     */
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME2)
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME3)

        onCreate(db)
    }



    /**
     * Let's create our insertData() method.
     * It Will insert data to SQLIte database.
     */
    fun insertData(id: String, name: String, surname: String, marks: String) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL_1, id)
        contentValues.put(COL_2, name)
        contentValues.put(COL_3, surname)
        contentValues.put(COL_4, marks)
        db.insert(TABLE_NAME, null, contentValues)
    }

    fun insertData2(id: String, name: String, surname: String) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(CLA_1, id)
        contentValues.put(CLA_2, name)
        contentValues.put(CLA_3, surname)
        db.insert(TABLE_NAME2, null, contentValues)
    }

    fun insertData3(id: String, password: String) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(USE_1, id)
        contentValues.put(USE_2, password)
        db.insert(TABLE_NAME3, null, contentValues)
    }

    /**
     * Let's create  a method to update a row with new field values.
     */
    fun updateData(id: String, name: String, surname: String, marks: String):
            Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL_1, id)
        contentValues.put(COL_2, name)
        contentValues.put(COL_3, surname)
        contentValues.put(COL_4, marks)
        db.update(TABLE_NAME, contentValues, "ID = ?", arrayOf(id))
        return true
    }

    fun updateData2(id: String, name: String, surname: String):
            Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(CLA_1, id)
        contentValues.put(CLA_2, name)
        contentValues.put(CLA_3, surname)
        db.update(TABLE_NAME2, contentValues, "ID = ?", arrayOf(id))
        return true
    }

    /**
     * Let's create a function to delete a given row based on the id.
     */
    fun deleteData(id : String) : Int {
        val db = this.writableDatabase
        return db.delete(TABLE_NAME,"ID = ?", arrayOf(id))
    }
    fun deleteData2(id : String) : Int {
        val db = this.writableDatabase
        return db.delete(TABLE_NAME2,"ID = ?", arrayOf(id))
    }

    /**
     * The below getter property will return a Cursor containing our dataset.
     */
    val allData : Cursor
        get() {
            val db = this.writableDatabase
            val res = db.rawQuery("SELECT * FROM " + TABLE_NAME, null)
            return res
        }

    val allData2 : Cursor
        get() {
            val db = this.writableDatabase
            val res = db.rawQuery("SELECT * FROM " + TABLE_NAME2, null)
            return res
        }

    fun searchData (id: String) :Cursor
    {
        val db = this.writableDatabase
        val querySearch = "SELECT * FROM " + TABLE_NAME + " WHERE ID = '"+id+"'"
        val res = db.rawQuery(querySearch, null)
        return res
    }

    fun searchData2 (id: String) :Cursor
    {
        val db = this.writableDatabase
        val querySearch = "SELECT * FROM " + TABLE_NAME2 + " WHERE ID = '"+id+"'"
        val res = db.rawQuery(querySearch, null)
        return res
    }
    /**
     * Let's create a companion object to hold our static fields.
     * A Companion object is an object that is common to all instances of a given
     * class.
     */
    companion object {
        val DATABASE_NAME = "students2.db"
        val TABLE_NAME = "student_table"
        val COL_1 = "ID"
        val COL_2 = "NAME"
        val COL_3 = "SURNAME"
        val COL_4 = "AGE"

        val TABLE_NAME2 = "class_table"
        val CLA_1 = "ID"
        val CLA_2 = "DESCRIPTION"
        val CLA_3 = "CREDIT"

        val TABLE_NAME3= "user_table"
        val USE_1 = "user"
        val USE_2 = "password"

        val TABLE_NAME4= "course_student_table"
        val sc_1 = "STUDENT_ID"
        val sc_2 = "COURSE_ID"

    }
}
//end