package com.example.capstoneproject

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.*
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import java.lang.Exception


class DirectoryActivity : AppCompatActivity(), View.OnClickListener, View.OnLongClickListener
{
    // Drawer toggle variables
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    lateinit var recyclerView: RecyclerView
    lateinit var dbHelper: BikeDatabaseHelper

    // creating bikeHolder inner class
    private inner class BikeHolder(view: View): RecyclerView.ViewHolder(view) {
        val bikeId: TextView = view.findViewById(R.id.bikeId)
        val bikeYear: TextView = view.findViewById(R.id.bikeYear)
        val bikeMake: TextView = view.findViewById(R.id.bikeMake)
        val bikeModel: TextView = view.findViewById(R.id.bikeModel)
        val bikeSize: TextView = view.findViewById(R.id.bikeSize)
    }
    // creating adapter inner class
    private inner class BikeAdapter(var bikes: List<Bike>, var onClickListener: View.OnClickListener
                                    , var onLongClickListener: View.OnLongClickListener): RecyclerView.Adapter<BikeHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BikeHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.bikedetail, parent, false)
            return BikeHolder(view)
        }

        override fun getItemCount(): Int {
            return bikes.size
        }

        override fun onBindViewHolder(holder: BikeHolder, position: Int) {
            val bike = bikes[position]
            //holder.bikeId.text = bike.id.toString()
            holder.bikeId.text = bike.id.toString()
            holder.bikeYear.text = bike.year
            holder.bikeMake.text = bike.make
            holder.bikeModel.text = bike.model
            holder.bikeSize.text = bike.size

            // sets the holder's listener
            holder.itemView.setOnClickListener(onClickListener)

            // set the holder's (long click) listener
            holder.itemView.setOnLongClickListener(onLongClickListener)
        }
    }

    // populates the recycler view
    fun populateRecyclerView() {
        val db = dbHelper.readableDatabase
        val bikes = mutableListOf<Bike>()
        val cursor = db.query(
            "bikes",
            null,
            null,
            null,
            null,
            null,
            null
        )
        with (cursor) {
            while (moveToNext()) {
                val id    = getInt(0)
                val year = getString(1)
                val make = getString(2)
                val model = getString(3)
                val size     = getString(4)
                val bike = Bike(id,year, make, model, size)
                bikes.add(bike)
            }
        }
        recyclerView.adapter = BikeAdapter(bikes, this, this)
    }
    
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_directory)
        // instance of BikeDatabaseHelper
        dbHelper = BikeDatabaseHelper(this)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        populateRecyclerView()
        // initializes the button
        val fabCreate: Button = findViewById(R.id.add_button)
        // set onClick listener
        fabCreate.setOnClickListener {
            // calls the BikeActivity for create
            val intent = Intent(this, BikeActivity::class.java)
            intent.putExtra("op", BikeActivity.CREATE_OP)
            startActivity(intent)
        }
        
        // Intents
        val intentLogOut = Intent(this, MainActivity::class.java)
        val intentHomeScreen = Intent(this, DirectoryActivity::class.java)
        val intentTripList = Intent(this, TripListActivity::class.java)
        
        // Variables to locate and toggle drawer
        drawerLayout = findViewById(R.id.my_drawer_layout)
        actionBarDrawerToggle =
            ActionBarDrawerToggle(this, drawerLayout, R.string.open,
                R.string.close)
        
        // Pass Open and Close toggle for drawer layout listener
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        
        // Make Navigation drawer icon always appear on action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        
        // Sidebar
        val navView: NavigationView = findViewById(R.id.nav_view)
        navView.setNavigationItemSelectedListener {
            when (it.itemId)
            {
                // Home Screen
                R.id.nav_home ->
                {
                    startActivity(intentHomeScreen)
                } // End R.id.nav_home
                
                // Trip List
                R.id.nav_trips ->
                {
                    startActivity(intentTripList)
                    Toast.makeText(this, "Trips", Toast.LENGTH_SHORT).show()
                } // End R.id.nav_trips
                
                // Account Page
                R.id.nav_account ->
                {
                    Toast.makeText(this, "Account", Toast.LENGTH_SHORT).show()
                } // End R.id.nav_account
                
                // Settings
                R.id.nav_settings ->
                {
                    Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show()
                } // End R.id.nav_settings
                
                // Log out
                R.id.nav_logout ->
                {
                    startActivity(intentLogOut)
                    Toast.makeText(this, "Logged Out", Toast.LENGTH_SHORT)
                        .show()
                } // End R.id.nav_logout
                
            } // End when
            true
        } // End navView.setNavigationItemSelectedListener
    } // End onCreate

    // this method is called when BikeActivity finishes
    override fun onResume() {
        super.onResume()
        populateRecyclerView()
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable("dbHelper", dbHelper)
    }
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        dbHelper = if (savedInstanceState.containsKey("dbHelper")) savedInstanceState.getSerializable("dbHelper") as BikeDatabaseHelper else BikeDatabaseHelper(this)
    }

    // calls BikeActivity for update
    override fun onClick(view: View?) {
        if (view != null) {
            val id = view.findViewById<TextView>(R.id.bikeId).text.toString().toInt()
            val intent = Intent(this, BikeActivity::class.java)
            intent.putExtra("op", BikeActivity.UPDATE_OP)
            intent.putExtra("id", id)
            startActivity(intent)
        }
    }
    
    // Override function to implement item click listener callback to open
    // and close the navigation drawer when the icon is clicked
    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        return if (actionBarDrawerToggle.onOptionsItemSelected(item))
        {
            true
        } // End return if
        else super.onOptionsItemSelected(item)
    } // End onOptionsItemSelected

    // implements the onLongClick method which is used to delete a bike
    override fun onLongClick(view: View?): Boolean {

        class MyDialogInterfaceListener(val id: Int): DialogInterface.OnClickListener {
            override fun onClick(dialogInterface: DialogInterface?, which: Int) {
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    try {
                        val db = dbHelper.writableDatabase
                        db.execSQL("""
                            DELETE FROM bikes
                            WHERE id = $id
                        """)
                        populateRecyclerView()
                    } catch (ex: Exception) {

                    }
                }
            }
        }

        if (view != null) {
            val id = view.findViewById<TextView>(R.id.bikeId).text.toString().toInt()
            val alertDialogBuilder = AlertDialog.Builder(this)
            alertDialogBuilder.setMessage("Are you sure you want to delete item named $id ?")
            alertDialogBuilder.setPositiveButton("Yes", MyDialogInterfaceListener(id))
            alertDialogBuilder.setNegativeButton("No", MyDialogInterfaceListener(id))
            alertDialogBuilder.show()
            return true
        }
        return false
    }
}