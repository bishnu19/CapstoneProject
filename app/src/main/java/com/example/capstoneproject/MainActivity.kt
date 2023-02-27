//****************************************************************************
/**@methodName:
 * @param:
 * @return:
 * @description:
 * */


package com.example.capstoneproject

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.*
import com.google.android.material.button.MaterialButton


class MainActivity : AppCompatActivity()
{
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        // User Sign-Up
        val signUpBtn = findViewById<View>(R.id.signUpBtn) as MaterialButton
        signUpBtn.setOnClickListener { v ->
            val intent1 = Intent()
            intent1.setClass(v.context, SignUP::class.java)
            startActivity(intent1)
        } // End signUpBtn.setOnClickListener
        
        // Entry field stuff
        val username = findViewById<View>(R.id.username) as TextView
        val password = findViewById<View>(R.id.password) as TextView
        val loginBtn = findViewById<View>(R.id.loginbtn) as MaterialButton
        
        // Method call for login
        passwordLogIn(username, password, loginBtn)
        
    } // End onCreate
    
    //************************************************************************
    /**@methodName:passwordLogIn
     * @param: username: TextView
     * @param: password: TextView
     * @param: loginBtn: MaterialButton
     * @description: This method uses pre-provided credentials to sign a user
     * into the app. After taking in arguments, the method uses sqlite queries
     * to see if a particular user name and password are contained in the
     * database; the method then takes the appropriate steps.
     * */
    private fun passwordLogIn(username: TextView, password: TextView,
                              loginBtn: MaterialButton)
    {
        // Login if user enters correct information
        loginBtn.setOnClickListener {
            
            // Database Query
            val db = DataBaseHelper(this)
            
            // Field(s) null
            if (username.text.toString() == "" || password.text.toString() == "")
            {
                Toast.makeText(this@MainActivity,
                    "Please enter information into all fields.",
                    Toast.LENGTH_SHORT).show()
            } // End if
            
            // Admin login
            else if (username.text.toString() == "admin" && password.text.toString() == "admin")
            {
                val directoryIntent = Intent()
                Toast.makeText(this@MainActivity, "ADMIN LOGIN",
                    Toast.LENGTH_SHORT).show()
                directoryIntent.setClass(this, DirectoryActivity::class.java)
                startActivity(directoryIntent)
            } // End if
            
            // Correct username/ email address and password provided
            else if (db.checkUsernamePassword(username.text.toString(),
                    password.text.toString()) || db.checkEmailPassword(
                    username.text.toString(), password.text.toString()))
            {
                val directoryIntent = Intent()
                Toast.makeText(this@MainActivity, "Welcome", Toast.LENGTH_SHORT)
                    .show()
                directoryIntent.setClass(this, DirectoryActivity::class.java)
                startActivity(directoryIntent)
            } // End if
            
            // Wrong login
            else
            {
                Toast.makeText(this@MainActivity, "LOGIN FAILED",
                    Toast.LENGTH_SHORT).show()
            } // End else
        } // End loginBtn.setOnClickListener
    } // End passwordLogIn
} // End MainActivity Class