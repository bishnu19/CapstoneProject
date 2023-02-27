package com.example.capstoneproject

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.io.Serializable

class DBHelperTrip(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION),
    Serializable
{
    companion object
    {
        const val DATABASE_NAME = "BikeTrip.db"
        const val DATABASE_VERSION = 1
    } // End companion object
    
    override fun onCreate(db: SQLiteDatabase?)
    {
        // create the table
        db?.execSQL("""
            CREATE TABLE trips (
                tripName        TEXT PRIMARY KEY NOT NULL,
                bikeName        TEXT NOT NULL,
                date            TEXT NOT NULL,
                location        TEXT NOT NULL)
        """)
        
        // Populate table
        db?.execSQL("""
            INSERT INTO trips VALUES
                ("Pilot", "Trail Master XXX", "June 20, 2022", "Idaho Springs"),
                ("SAMPLE TWO", "Suzuki SX2", "July 4, 2022", "Boulder")
        """)
    } // End onCreate
    
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int,
                           newVersion: Int)
    {
        // Drop old table...
        db?.execSQL("""
            DROP TABLE IF EXISTS trips
        """)
        
        // ...then call "onCreate" again
        onCreate(db)
    } // End onUpgrade
} // End DBHelperTrip class
