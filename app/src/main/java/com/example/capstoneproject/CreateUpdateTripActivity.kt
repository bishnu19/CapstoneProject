package com.example.capstoneproject

import android.annotation.SuppressLint
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import java.lang.Exception

class CreateUpdateTripActivity : AppCompatActivity(), View.OnClickListener
{
    private var op = CREATE_OP
    private lateinit var db: SQLiteDatabase
    private lateinit var edtTripName: EditText
    private lateinit var edtBikeName: EditText
    private lateinit var edtDate: EditText
    private lateinit var edtLocation: EditText
    
    companion object
    {
        const val CREATE_OP = 0
        const val UPDATE_OP = 1
    } // End companion object
    
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_update_trip)
        
        // Get references to view objects
        edtTripName = findViewById(R.id.etTripName)
        edtBikeName = findViewById(R.id.etBikeUsed)
        edtDate = findViewById(R.id.etTripDate)
        edtLocation = findViewById(R.id.etLocation)
        
        // Get reference to the "CREATE/UPDATE" button and sets its listener
        val btnCreateUpdate: Button = findViewById(R.id.btnCreateUpdate)
        btnCreateUpdate.setOnClickListener(this)
        
        // Gets a "writable" db connection
        val dbHelper = DBHelperTrip(this)
        db = dbHelper.writableDatabase
        
        // Gets the operation and updates the view accordingly
        op = intent.getIntExtra("op", CREATE_OP)
        if (op == CREATE_OP) btnCreateUpdate.text = "CREATE"
        
        // Update Operation
        else
        {
            btnCreateUpdate.text = "UPDATE"
            
            // Find previous entry
            val name = intent.getStringExtra("name") ?: ""
            val item = retrieveItem(name)
            
            // Update fields
            edtTripName.setText(name)
            edtBikeName.setText(item.bikeName)
            edtDate.setText(item.date)
            edtLocation.setText(item.location)
        } // End else
    } // End onCreate
    
    // returns the item based on the given name
    @SuppressLint("Recycle")
    private fun retrieveItem(name: String): TripItem
    {
        val cursor =
            db.query("trips", null, "tripName = \"${name}\"", null, null, null,
                null)
        
        cursor.moveToNext()
        val bikeName = cursor.getString(1)
        val date = cursor.getString(2)
        val location = cursor.getString(3)
        return TripItem(name, bikeName, date, location)
    } // End retrieveItem
    
    override fun onClick(p0: View?)
    {
        val tripName1 = findViewById<EditText>(R.id.etTripName).text.toString()
        val bikeName1 = findViewById<EditText>(R.id.etBikeUsed).text.toString()
        val tripDate = findViewById<EditText>(R.id.etTripDate).text.toString()
        val tripLocation =
            findViewById<EditText>(R.id.etLocation).text.toString()
        
        // Create new entry...
        if (op == CREATE_OP)
        {
            // Create new entry
            try
            {
                db.execSQL("""
                        INSERT INTO trips VALUES
                            ("$tripName1", "$bikeName1", "$tripDate", "$tripLocation")
                    """)
                Toast.makeText(this, "New trip successfully created.",
                    Toast.LENGTH_SHORT).show()
            } // End try
            
            // Couldn't create new trip
            catch (ex: Exception)
            {
                print(ex.toString())
                Toast.makeText(this, "Unable to create new trip.",
                    Toast.LENGTH_SHORT).show()
            } // End catch
        } // End if
        
        // Update operation
        else
        {
            try
            {
                db.execSQL("""
                        UPDATE trips SET
                            bikeName = "$bikeName1",
                            date = "$tripDate",
                            location = "$tripLocation"
                        WHERE tripName = "$tripName1"
                    """)
                Toast.makeText(this, "Entry successfully updated!",
                    Toast.LENGTH_SHORT).show()
            } // End try
            
            // Unable to update trip
            catch (ex: Exception)
            {
                print(ex.toString())
                Toast.makeText(this, "Update entry exception.",
                    Toast.LENGTH_SHORT).show()
            } // End catch
        } // End else
        finish()
    } // End onClick
} // End CreateUpdateTripActivity class