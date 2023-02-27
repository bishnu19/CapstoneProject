package com.example.capstoneproject

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.*
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView


class DirectoryActivity : AppCompatActivity()
{
    // Drawer toggle variables
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_directory)
        
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
} // End DirectoryActivity class