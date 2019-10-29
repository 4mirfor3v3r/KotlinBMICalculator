package com.amier.bmicalculator.sqlite

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class SQLiteHelper(context: Context):SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = "CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_UID INTEGER PRIMARY KEY," +
                "$COLUMN_DAY TEXT," +
                "$COLUMN_DATE TEXT," +
                "$COLUMN_WEIGHT TEXT," +
                "$COLUMN_BMI TEXT," +
                "$COLUMN_ICON TEXT," +
                "$COLUMN_DIV TEXT);"
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        val dropTable = "DROP TABLE IF EXISTS $TABLE_NAME;"
        db.execSQL(dropTable)
        onCreate(db)
    }

    fun addData(data: Data):Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_DAY, data.day)
        values.put(COLUMN_DATE, data.date)
        values.put(COLUMN_WEIGHT, data.weight)
        values.put(COLUMN_BMI, data.bmi)
        values.put(COLUMN_ICON, data.icon)
        values.put(COLUMN_DIV, data.div)

        val success = db.insert(TABLE_NAME, null, values)
        db.close()
        Log.d("INSERTED ID", "$success")
        return (Integer.parseInt("$success") != -1)
    }

    fun fetchAll():ArrayList<Data> {
        val dataList = ArrayList<Data>()
        val db = writableDatabase
        val query = "SELECT * FROM $TABLE_NAME ORDER BY $COLUMN_UID DESC LIMIT 10;"
        val cursor = db.rawQuery(query, null)
        if(cursor != null){
            if(cursor.moveToFirst()){
                do {
                    val data = Data()
                    data.id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_UID)))
                    data.day = cursor.getString(cursor.getColumnIndex(COLUMN_DAY))
                    data.date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE))
                    data.weight = cursor.getString(cursor.getColumnIndex(COLUMN_WEIGHT))
                    data.bmi = cursor.getString(cursor.getColumnIndex(COLUMN_BMI))
                    data.icon = cursor.getString(cursor.getColumnIndex(COLUMN_ICON))
                    data.div = cursor.getString(cursor.getColumnIndex(COLUMN_DIV))
                    dataList.add(data)
                }
                while (cursor.moveToNext())
            }
        }
        cursor.close()
        return dataList
    }

    fun fetchLast(): Data {
        val data = Data()
        val db = writableDatabase
        val query = "SELECT * FROM $TABLE_NAME ORDER BY $COLUMN_UID DESC LIMIT 1;"
        val cursor = db.rawQuery(query, null)
        if(cursor != null && cursor.moveToFirst()){
            cursor.moveToFirst()
            data.id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_UID)))
            data.day = cursor.getString(cursor.getColumnIndex(COLUMN_DAY))
            data.date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE))
            data.weight = cursor.getString(cursor.getColumnIndex(COLUMN_WEIGHT))
            data.bmi = cursor.getString(cursor.getColumnIndex(COLUMN_BMI))
            data.icon = cursor.getString(cursor.getColumnIndex(COLUMN_ICON))
            data.div = cursor.getString(cursor.getColumnIndex(COLUMN_DIV))
        }
        cursor.close()
        return data
    }

    companion object {
        private const val DB_VERSION = 1
        private const val DB_NAME = "BMI"
        private const val TABLE_NAME = "data"

        const val COLUMN_UID = "uid"
        const val COLUMN_DAY = "day"
        const val COLUMN_DATE = "date"
        const val COLUMN_WEIGHT = "weight"
        const val COLUMN_BMI = "bmi"
        const val COLUMN_ICON = "icon"
        const val COLUMN_DIV = "div"
    }
}
