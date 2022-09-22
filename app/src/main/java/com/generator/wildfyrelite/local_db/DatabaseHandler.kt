package com.generator.wildfyrelite.local_db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.generator.wildfyrelite.enum.Table
import com.generator.wildfyrelite.enum.WebOpenerDB
import com.generator.wildfyrelite.model.RangeData
import com.generator.wildfyrelite.model.Wordpress

class DatabaseHandler(val context : Context) : SQLiteOpenHelper(context, WebOpenerDB.DATABASE_NAME.getValue(), null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE " + WebOpenerDB.TABLE_URL.getValue() + " (" +
                Table.Table_Url.URL.getValue() + " VARCHAR(200))"
        )

        db?.execSQL("CREATE TABLE " + WebOpenerDB.TABLE_FACTOR.getValue() + " (" +
                Table.Table_Factor.FACTOR.getValue() + " VARCHAR(200))"
        )

        db?.execSQL("CREATE TABLE " + WebOpenerDB.TABLE_RANGE.getValue() + " (" +
                Table.Table_Range.RANGE_OF_POST.getValue() + " VARCHAR(200)," +
                Table.Table_Range.RANGE_TO_LOAD.getValue() + " VARCHAR(200))"
        )

        db?.execSQL("CREATE TABLE " + WebOpenerDB.TABLE_WORDPRESS.getValue() + " (" +
                Table.Table_Wordpress.TITLE.getValue() + " VARCHAR(200)," +
                Table.Table_Wordpress.DATE.getValue() + " VARCHAR(200), " +
                Table.Table_Wordpress.GROUP.getValue() + " VARCHAR(200), " +
                Table.Table_Wordpress.LINK.getValue() + " VARCHAR(200))"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    fun insertURL(data : String) : Boolean {
        val db = this.writableDatabase
        var url = ContentValues()

        url.put(Table.Table_Url.URL.getValue(), data)
        var result = db.insert(WebOpenerDB.TABLE_URL.getValue(), null , url)
        db.close()
        return result != (-1).toLong()
    }

    fun insertRange(toLoad : String, ofPost: String) : Boolean {
        deleteDatabase(WebOpenerDB.TABLE_RANGE.getValue())
        val db = this.writableDatabase
        var range = ContentValues()

        range.put(Table.Table_Range.RANGE_OF_POST.getValue(), ofPost)
        range.put(Table.Table_Range.RANGE_TO_LOAD.getValue(), toLoad)
        var result = db.insert(WebOpenerDB.TABLE_RANGE.getValue(), null , range)
        db.close()
        return result != (-1).toLong()
    }

    fun insertWordpress(data : Wordpress.Result, group : String) : Boolean {
        val db = this.writableDatabase
        var wordpress = ContentValues()

        wordpress.put(Table.Table_Wordpress.TITLE.getValue(), data.title.rendered)
        wordpress.put(Table.Table_Wordpress.LINK.getValue(), data.link)
        wordpress.put(Table.Table_Wordpress.DATE.getValue(), data.date)
        wordpress.put(Table.Table_Wordpress.GROUP.getValue(), group)
        var result = db.insert(WebOpenerDB.TABLE_WORDPRESS.getValue(), null , wordpress)
        db.close()
        return result != (-1).toLong()
    }

    fun insertFactor(data : String) : Boolean {
        val db = this.writableDatabase
        var interval = ContentValues()

        interval.put(Table.Table_Factor.FACTOR.getValue(), data)
        var result = db.insert(WebOpenerDB.TABLE_FACTOR.getValue(), null , interval)
        db.close()
        return result != (-1).toLong()
    }

    fun getURL() : MutableList<String>{
        val list : MutableList<String> = ArrayList()
        val db = this.readableDatabase
        val result = db.rawQuery("SELECT * from " + WebOpenerDB.TABLE_URL.getValue(), null)
        if (result.moveToFirst()) {
            do {
                list.add(result.getString(result.getColumnIndex(Table.Table_Url.URL.getValue())))
            }while (result.moveToNext() )
        }
        db.close()
        return list
    }

    fun getWordpress() : MutableList<Wordpress.Result>{
        val list : MutableList<Wordpress.Result> = ArrayList()
        val db = this.readableDatabase
        val result = db.rawQuery("SELECT * from " + WebOpenerDB.TABLE_WORDPRESS.getValue(), null)
        if (result.moveToFirst()) {
            do {
                var data = Wordpress.Result(
                        result.getString(result.getColumnIndex(Table.Table_Wordpress.GROUP.getValue())),
                        result.getString(result.getColumnIndex(Table.Table_Wordpress.LINK.getValue())),
                        result.getString(result.getColumnIndex(Table.Table_Wordpress.DATE.getValue())),
                Wordpress.Title(result.getString(result.getColumnIndex(Table.Table_Wordpress.TITLE.getValue()))))
                list.add(data)
            }while (result.moveToNext() )
        }
        db.close()
        return list
    }

    fun getRange() : RangeData.Result? {
        var data : RangeData.Result? = null
        val db = this.readableDatabase
        val result = db.rawQuery("SELECT * from " + WebOpenerDB.TABLE_RANGE.getValue(), null)
        if (result.moveToFirst()) {
            do {
                data = RangeData.Result(
                        result.getString(result.getColumnIndex(Table.Table_Range.RANGE_TO_LOAD.getValue())),
                        result.getString(result.getColumnIndex(Table.Table_Range.RANGE_OF_POST.getValue()))
                )
            }while (result.moveToNext() )
        }
        db.close()
        return data
    }

    fun getFactor() : Int {
        var data : Int = 0
        val db = this.readableDatabase
        val result = db.rawQuery("SELECT * from " + WebOpenerDB.TABLE_FACTOR.getValue(), null)
        if (result.moveToFirst()) {
            do {
                data = result.getString(result.getColumnIndex(Table.Table_Factor.FACTOR.getValue())).toInt()
            }while (result.moveToNext() )
        }
        db.close()
        return data
    }

    fun deleteDatabase(data : String) {
        val db = this.writableDatabase
        db.delete(data,null,null)
        db.close()
    }

}