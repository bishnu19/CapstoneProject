//****************************************************************************
/**@methodName:
 * @param:
 * @return:
 * @description:
 * */

package com.example.capstoneproject

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton


class SignUP : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sign_up)
        
        // Variables
        val username = findViewById<View>(R.id.username) as EditText
        val email = findViewById<View>(R.id.email) as EditText
        val password = findViewById<View>(R.id.password) as EditText
        val password2 = findViewById<View>(R.id.repassword) as EditText
        val regBtn = findViewById<View>(R.id.signupbtn) as MaterialButton
        
        // When user clicks the register button...
        regBtn.setOnClickListener { v ->
            
            // Screen holding variables
            val username1 = username.text.toString()
            val passwordString1 = password.text.toString()
            val passwordString2 = password2.text.toString()
            val emailAddress = email.text.toString()
            
            // Intent
            val mainScreenIntent = Intent()
            mainScreenIntent.setClass(v.context, MainActivity::class.java)
            
            // Go back to login screen after accepting user credentials.
            if (passwordString1 == passwordString2)
            {
                // Add user to database
                val dbHelper = DataBaseHelper(this)
                dbHelper.companion.getHash(passwordString1)
                val user1 = UserModel(emailAddress, username1, passwordString1)
                val success = dbHelper.addOne(user1)
                
                // Go back to sign in if the user was added to the database.
                if (success)
                {
                    Toast.makeText(this@SignUP, "Welcome $username1",
                        Toast.LENGTH_LONG).show()
                    startActivity(mainScreenIntent)
                } // End if
            } // End if
            
            // If passwords don't match, let the user know.
            else
            {
                Toast.makeText(this@SignUP,
                    "Passwords don't match.\nPlease try again.",
                    Toast.LENGTH_SHORT).show()
            } // End else
        } // End regbtn.setOnClickListener
    } // End onCreate
} // End SignUp
