//****************************************************************************
/**@methodName:
 * @param:
 * @return:
 * @description:
 * */

package com.example.capstoneproject

import android.annotation.SuppressLint
import android.content.*
import android.database.Cursor
import android.database.sqlite.*
import java.io.Serializable
import java.security.MessageDigest
import kotlin.random.Random

class DataBaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION),
    Serializable
{
    
    
    companion object
    {
        const val DATABASE_NAME = "User.db"
        const val DATABASE_VERSION = 2
        const val SYMBOLS = "abcdefghijklmnopqrstuvwxyz0123456789$%&@!?_-"
        
        
        fun getSalt(): String
        {
            var salt = ""
            while (salt.length < 16) salt += SYMBOLS[Random.nextInt(
                SYMBOLS.length)]
            return salt
        } // End getSalt
        
        fun getHash(input: String): String
        {
            return MessageDigest.getInstance("SHA256")
                .digest(input.toByteArray()).decodeToString()
        } // End getHash
    } // End companion object
    
    var companion = Companion
    
    //************************************************************************
    /**@methodName: onCreate
     * @param: db (a sqlite database)
     * @return: void
     * @description: This is called the first time a data base is created (there should be
     * code here to create a new database).
     * */
    
    override fun onCreate(db: SQLiteDatabase?)
    {
        val createTableStatement =
            """CREATE TABLE USER_DATABASE (column_Email TEXT PRIMARY KEY, column_Username TEXT NOT NULL, column_Password TEXT NOT NULL)"""
        
        db?.execSQL(createTableStatement)
    } // End onCreate
    
    //************************************************************************
    /**@methodName: onUpgrade
     * @param: oldDB (a sqlite database)
     * @param: oldVersion (an initial version number for a database)
     * @param: newVersion ""
     * @return: null
     * @description: This is called when the version number changes. It prevents
     * previous user apps from breaking when you change the data base design.
     * */
    override fun onUpgrade(oldDB: SQLiteDatabase?, oldVersion: Int,
                           newVersion: Int)
    {
        // Drop table...
        oldDB?.execSQL("""
            DROP TABLE IF EXISTS USER_DATABASE
        """)
        
        // ...then call "onCreate" again
        onCreate(oldDB)
    } // End onUpgrade
    
    //************************************************************************
    /**@methodName: addOne
     * @return: bool
     * @description: This method adds one user to the database
     * */
    fun addOne(userModel: UserModel): Boolean
    {
        // Variables
        val db: SQLiteDatabase = this.writableDatabase
        val cv = ContentValues()
        
        // Enter data into data base
        cv.put("column_Email", userModel.email)
        cv.put("column_Username", userModel.username)
        cv.put("column_Password", userModel.password)
        val insert = db.insert("USER_DATABASE", null, cv)
    
        // Close variable
        db.close()
        
        // Return stuff
        if (insert.equals(-1)) return false
        return true
    } // End addOne
    
    //************************************************************************
    /**@methodName: getEveryone
     * @return: List<UserModel>
     * @description: This method returns all database users and their
     * respective information.
     * @status: works as expected.
     * */
    fun getEveryone(): List<UserModel>
    {
        // List to be returned at the end of the method
        val returnList = mutableListOf<UserModel>()
        
        // Get data from data base
        val queryString = "SELECT * FROM USER_DATABASE"
        val db: SQLiteDatabase = this.writableDatabase
        val cursor = db.rawQuery(queryString, null)
        
        // If the cursor has an element in it...
        if (cursor.moveToFirst())
        {
            do
            {
                val email = cursor.getString(0).toString()
                val userName = cursor.getString(1).toString()
                val password = cursor.getString(2).toString()
                val newUser = UserModel(email, userName, password)
                
                returnList.add(newUser)
            } // End do
            while (cursor.moveToNext()) // End do while
        } // End if
        
        
        // Close variables
        cursor.close()
        db.close()
        
        // Return list
        return returnList
    } // End getEveryone
    
    
    //************************************************************************
    /**@methodName: viewEntry
     * @return: ArrayList<UserModel>
     * @description: This method returns all database users and their
     * respective information.
     * */
    fun viewEntry(): ArrayList<UserModel>
    {
        val userList: ArrayList<UserModel> = ArrayList()
        val selectQuery = "SELECT * FROM USER_DATABASE"
        
        val db = this.readableDatabase
        var cursor: Cursor? = null
        
        try
        {
            cursor = db.rawQuery(selectQuery, null)
        } // End try
        catch (e: SQLiteException)
        {
            db.execSQL(selectQuery)
            return ArrayList()
        } // End catch
        
        // Necessary variables
        var email: String
        var userName: String
        var password: String
        
        if (cursor.moveToFirst())
        {
            do
            {
                email = cursor.getString(0)
                userName = cursor.getString(1)
                password = cursor.getString(2)
                val user = UserModel(email, userName, password)
                userList.add(user)
            } // End do
            while (cursor.moveToNext()) // End do while
        } // End if
        
        // Close variables
        db.close()
        cursor.close()
        
        // Return statement
        return userList
    } // End viewEntry
    
    //****************************************************************************
    /**@methodName: checkUsername
     * @param: userName: String
     * @return: Boolean
     * @description: This method returns a boolean based on whether a user name
     * is found in the database.
     * */
    @SuppressLint("Recycle")
    fun checkUsername(userName: String): Boolean
    {
        // Variable initialization and query statement
        val myDB = this.writableDatabase
        val selectQuery =
            "SELECT * FROM USER_DATABASE WHERE column_Username = ?"
        val cursor = myDB.rawQuery(selectQuery, arrayOf(userName))
        
        // Return Statement
        return (cursor.count > 0)
    } // End checkUsername
    
    //****************************************************************************
    /**@methodName: checkUsernamePassword
     * @param: userName: String
     * @param: password: String
     * @return: Boolean
     * @description:This method returns a boolean based on whether a user name
     * and password are jointly found in the database.
     * */
    @SuppressLint("Recycle")
    fun checkUsernamePassword(userName: String, password: String): Boolean
    {
        // Variable initialization and query statement
        val myDB = this.writableDatabase
        val selectQuery =
            "SELECT * FROM USER_DATABASE WHERE column_Username = ? and column_Password = ?"
        val cursor = myDB.rawQuery(selectQuery, arrayOf(userName, password))
        
        // Return statement
        return (cursor.count > 0)
    } // End checkUsernamePassword
    
    //****************************************************************************
    /**@methodName: checkEmailPassword
     * @param: email: String
     * @param: password: String
     * @return: Boolean
     * @description:This method returns a boolean based on whether an email
     * address and password are jointly found in the database.
     * */
    @SuppressLint("Recycle")
    fun checkEmailPassword(email: String, password: String): Boolean
    {
        // Variable initialization and query statement
        val myDB = this.writableDatabase
        val selectQuery =
            "SELECT * FROM USER_DATABASE WHERE column_Email = ? and column_Password = ?"
        val cursor = myDB.rawQuery(selectQuery, arrayOf(email, password))
        
        // Return statement
        return (cursor.count > 0)
    } // End checkEmailPassword
} // End DataBaseHelper class