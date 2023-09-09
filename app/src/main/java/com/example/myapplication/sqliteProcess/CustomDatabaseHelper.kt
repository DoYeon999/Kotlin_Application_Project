package com.example.myapplication.sqliteProcess

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.myapplication.weather_imgfind.model.FirstDayWeatherDB
import com.example.myapplication.weather_imgfind.model.OtherDayWeatherDB
import com.github.mikephil.charting.data.Entry
import kotlin.math.min

class CustomDatabaseHelper(context : Context) : SQLiteOpenHelper(context, "tidedatabase.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        Log.d("sqlite", "success1")
        db?.execSQL(
            "CREATE TABLE IF NOT EXISTS todaytide (ttid LONG PRIMARY KEY, obscode TEXT, tidelevel TEXT)"
        )
        Log.d("sqlite", "success2")
        db?.execSQL(
            """
                CREATE TABLE IF NOT EXISTS firstdayweather (fdwid LONG PRIMARY KEY, nowtemp TEXT, obscode TEXT,
                tidetypeone TEXT, tidetimeone TEXT, tidelevelone TEXT, tidetypetwo TEXT, tidetimetwo TEXT, tideleveltwo TEXT,
                tidetypethree TEXT, tidetimethree TEXT, tidelevelthree TEXT, tidetypefour TEXT, tidetimefour TEXT, tidelevelfour TEXT)
            """.trimIndent()
        )
        Log.d("sqlite", "success3")
        db?.execSQL(
            """
                CREATE TABLE IF NOT EXISTS otherdayweather (fdwid LONG PRIMARY KEY, daytype INTEGER, maxtemp TEXT, mintemp TEXT, obscode TEXT,
                tidetypeone TEXT, tidetimeone TEXT, tidelevelone TEXT, tidetypetwo TEXT, tidetimetwo TEXT, tideleveltwo TEXT,
                tidetypethree TEXT, tidetimethree TEXT, tidelevelthree TEXT, tidetypefour TEXT, tidetimefour TEXT, tidelevelfour TEXT)
            """.trimIndent()
        )
        Log.d("sqlite", "success4")
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }

    fun clearTide() {
        val database = writableDatabase
        database.execSQL("DELETE FROM todaytide")
        database.close()
    }

    fun insertTide(obscode : String, tidelevel : String) {
        val db = writableDatabase
        val values = ContentValues()
        values.put("obscode", obscode)
        values.put("tidelevel", tidelevel)
        db.insert("todaytide", null, values)
        db.close()
    }

    fun getAllTides(obscode : String) : List<Entry> {
        Log.d("today::::", obscode)
        val todayTide = mutableListOf<Entry>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM todaytide WHERE obscode = ?", arrayOf(obscode))
        var i = 0
        if (cursor.moveToFirst()) {
            do {
                val colIdx = cursor.getColumnIndex("tidelevel")
                if(colIdx >= 0) {
                    val level = cursor.getString(colIdx)
                    todayTide.add(Entry(i.toFloat(), level.toFloat()))
                    i++
                }
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return todayTide
    }

    fun clearFirstDayWeather() {
        val db = writableDatabase
        db.execSQL("DELETE FROM firstdayweather")
        db.close()
    }

    fun insertFirstDayWeather(nowtemp : String, obscode : String, typeone : String?, timeone : String?, levelone : String?,
                              typetwo : String?, timetwo : String?, leveltwo : String?, typethree : String?, timethree : String?, levelthree : String?,
                              typefour : String?, timefour : String?, levelfour : String?) {
        val db = writableDatabase
        val values = ContentValues()
        values.put("nowtemp", nowtemp)
        values.put("obscode", obscode)
        values.put("tidetypeone", typeone)
        values.put("tidetimeone", timeone)
        values.put("tidelevelone", levelone)
        values.put("tidetypetwo", typetwo)
        values.put("tidetimetwo", timetwo)
        values.put("tideleveltwo", leveltwo)
        values.put("tidetypethree", typethree)
        values.put("tidetimethree", timethree)
        values.put("tidelevelthree", levelthree)
        values.put("tidetypefour", typefour)
        values.put("tidetimefour", timefour)
        values.put("tidelevelfour", levelfour)
        db.insert("firstdayweather", null, values)
        db.close()
    }

    fun getFirstDayWeather(obscode : String) : FirstDayWeatherDB {
        Log.d("today::::", obscode)
        lateinit var todayWeather : FirstDayWeatherDB
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM firstdayweather WHERE obscode = ?", arrayOf(obscode))
        var i = 0
        if (cursor.moveToFirst()) {
            do {
                val colIdx1 = cursor.getColumnIndex("nowtemp")
                val colIdx2 = cursor.getColumnIndex("tidetypeone")
                val colIdx3 = cursor.getColumnIndex("tidetimeone")
                val colIdx4 = cursor.getColumnIndex("tidelevelone")
                val colIdx5 = cursor.getColumnIndex("tidetypetwo")
                val colIdx6 = cursor.getColumnIndex("tidetimetwo")
                val colIdx7 = cursor.getColumnIndex("tideleveltwo")
                val colIdx8 = cursor.getColumnIndex("tidetypethree")
                val colIdx9 = cursor.getColumnIndex("tidetimethree")
                val colIdx10 = cursor.getColumnIndex("tidelevelthree")
                val colIdx11 = cursor.getColumnIndex("tidetypefour")
                val colIdx12 = cursor.getColumnIndex("tidetimefour")
                val colIdx13 = cursor.getColumnIndex("tidelevelfour")
                if(colIdx1 >= 0) {
                    val nowtemp = cursor.getString(colIdx1)
                    val typeone = cursor.getString(colIdx2)
                    val timeone = cursor.getString(colIdx3)
                    val levelone = cursor.getString(colIdx4)
                    val typetwo = cursor.getString(colIdx5)
                    val timetwo = cursor.getString(colIdx6)
                    val leveltwo = cursor.getString(colIdx7)
                    val typethree = cursor.getString(colIdx8)
                    val timethree = cursor.getString(colIdx9)
                    val levelthree = cursor.getString(colIdx10)
                    val typefour = cursor.getString(colIdx11)
                    val timefour = cursor.getString(colIdx12)
                    val levelfour = cursor.getString(colIdx13)
                    todayWeather = FirstDayWeatherDB(nowtemp, levelone, timeone, typeone, leveltwo, timetwo, typetwo, levelthree, timethree, typethree, levelfour, timefour, typefour)
                }
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return todayWeather
    }

    fun clearOtherDayWeather() {
        val database = writableDatabase
        database.execSQL("DELETE FROM otherdayweather")
        database.close()
    }

    fun insertOtherDayWeather(daytype : Int, mintemp : String, maxtemp : String, obscode : String, typeone : String?, timeone : String?, levelone : String?,
                              typetwo : String?, timetwo : String?, leveltwo : String?, typethree : String?, timethree : String?, levelthree : String?,
                              typefour : String?, timefour : String?, levelfour : String?) {
        val db = writableDatabase
        val values = ContentValues()
        values.put("daytype", daytype)
        values.put("mintemp", mintemp)
        values.put("maxtemp", maxtemp)
        values.put("obscode", obscode)
        values.put("tidetypeone", typeone)
        values.put("tidetimeone", timeone)
        values.put("tidelevelone", levelone)
        values.put("tidetypetwo", typetwo)
        values.put("tidetimetwo", timetwo)
        values.put("tideleveltwo", leveltwo)
        values.put("tidetypethree", typethree)
        values.put("tidetimethree", timethree)
        values.put("tidelevelthree", levelthree)
        values.put("tidetypefour", typefour)
        values.put("tidetimefour", timefour)
        values.put("tidelevelfour", levelfour)
        db.insert("otherdayweather", null, values)
        db.close()
    }

    fun getOtherDayWeather(obscode : String) : List<OtherDayWeatherDB> {
        Log.d("today::::", obscode)
        var otherDayWeather = mutableListOf<OtherDayWeatherDB>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM otherdayweather WHERE obscode = ? ORDER BY daytype", arrayOf(obscode))
        if (cursor.moveToFirst()) {
            do {
                val colIdx0 = cursor.getColumnIndex("mintemp")
                val colIdx1 = cursor.getColumnIndex("maxtemp")
                val colIdx2 = cursor.getColumnIndex("tidetypeone")
                val colIdx3 = cursor.getColumnIndex("tidetimeone")
                val colIdx4 = cursor.getColumnIndex("tidelevelone")
                val colIdx5 = cursor.getColumnIndex("tidetypetwo")
                val colIdx6 = cursor.getColumnIndex("tidetimetwo")
                val colIdx7 = cursor.getColumnIndex("tideleveltwo")
                val colIdx8 = cursor.getColumnIndex("tidetypethree")
                val colIdx9 = cursor.getColumnIndex("tidetimethree")
                val colIdx10 = cursor.getColumnIndex("tidelevelthree")
                val colIdx11 = cursor.getColumnIndex("tidetypefour")
                val colIdx12 = cursor.getColumnIndex("tidetimefour")
                val colIdx13 = cursor.getColumnIndex("tidelevelfour")
                if(colIdx0 >= 0) {
                    val mintemp = cursor.getString(colIdx0)
                    val maxtemp = cursor.getString(colIdx1)
                    val typeone = cursor.getString(colIdx2)
                    val timeone = cursor.getString(colIdx3)
                    val levelone = cursor.getString(colIdx4)
                    val typetwo = cursor.getString(colIdx5)
                    val timetwo = cursor.getString(colIdx6)
                    val leveltwo = cursor.getString(colIdx7)
                    val typethree = cursor.getString(colIdx8)
                    val timethree = cursor.getString(colIdx9)
                    val levelthree = cursor.getString(colIdx10)
                    val typefour = cursor.getString(colIdx11)
                    val timefour = cursor.getString(colIdx12)
                    val levelfour = cursor.getString(colIdx13)
                    otherDayWeather.add(OtherDayWeatherDB(mintemp, maxtemp, levelone, timeone, typeone, leveltwo, timetwo, typetwo, levelthree, timethree, typethree, levelfour, timefour, typefour))
                }
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return otherDayWeather
    }
}