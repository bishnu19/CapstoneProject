package com.example.capstoneproject

import android.content.*
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.*
import androidx.recyclerview.widget.*
import com.google.android.material.floatingactionbutton.FloatingActionButton


class TripListActivity: AppCompatActivity(), View.OnClickListener,
    View.OnLongClickListener
{
    
    private lateinit var recyclerView: RecyclerView
    lateinit var dbHelperTrip: DBHelperTrip
    
    // Creates ItemHolder inner class
    private inner class ItemHolder(view: View) : RecyclerView.ViewHolder(view)
    {
        val txtTripName: TextView = view.findViewById(R.id.txtTripName)
        val txtBikeName: TextView = view.findViewById(R.id.txtBikeName)
        val txtDate: TextView = view.findViewById(R.id.txtDate)
        val txtLocation: TextView = view.findViewById(R.id.txtLocation)
    } // End ItemHolder inner class
    
    // creates the ItemAdapter inner class
    // TODOd #2: add an OnLongClickListener parameter
    private inner class ItemAdapter(var items: List<TripItem>,
                                    var onClickListener: View.OnClickListener,
                                    var onLongClickListener: View.OnLongClickListener) :
        RecyclerView.Adapter<ItemHolder>()
    {
        
        override fun onCreateViewHolder(parent: ViewGroup,
                                        viewType: Int): ItemHolder
        {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_list_trips, parent, false)
            return ItemHolder(view)
        } // End onCreateViewHolder
        
        override fun onBindViewHolder(holder: ItemHolder, position: Int)
        {
            val item = items[position]
            holder.txtTripName.text = item.tripName
            holder.txtBikeName.text = item.bikeName
            holder.txtDate.text = item.date
            holder.txtLocation.text = item.location
            
            // Sets holder's listener
            holder.itemView.setOnClickListener(onClickListener)
            
            // Set holder's long click listener
            holder.itemView.setOnLongClickListener(onLongClickListener)
        } // End onBindViewHolder
        
        override fun getItemCount(): Int
        {
            return items.size
        } // End getItemCount
    } // End ItemAdapter inner class
    
    fun populateRecyclerView()
    {
        val db = dbHelperTrip.readableDatabase
        val trips = mutableListOf<TripItem>()
        val cursor = db.query("trips", null, null, null, null, null, null)
        with(cursor) {
            while (moveToNext())
            {
                val tripName = getString(0)
                val bikeName = getString(1)
                val date = getString(2)
                val location = getString(3)
                val item = TripItem(tripName, bikeName, date, location)
                trips.add(item)
            } // End while
        } // End with
        recyclerView.adapter = ItemAdapter(trips, this, this)
    } // End populateRecyclerView
    
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trips_list)
        
        // Create and populate recycler view
        dbHelperTrip = DBHelperTrip(this)
        recyclerView = findViewById(R.id.rvTripList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        populateRecyclerView()
        
        // Initializes floating action button
        val fabCreate: FloatingActionButton = findViewById(R.id.btnAddTrip)
        fabCreate.setOnClickListener {
            // Call CreateUpdateTripActivity for create
            val intent = Intent(this, CreateUpdateTripActivity::class.java)
            intent.putExtra("op", CreateUpdateTripActivity.CREATE_OP)
            startActivity(intent)
        } // End fabCreate.setOnClickListener
    } // End onCreate
    
    // Called when CreateUpdateActivity finishes
    override fun onResume()
    {
        super.onResume()
        populateRecyclerView()
    } // End onResume
    
    
    override fun onSaveInstanceState(outState: Bundle)
    {
        super.onSaveInstanceState(outState)
        outState.putSerializable("dbHelperTrip", dbHelperTrip)
    } // End onSaveInstanceState
    
    
    override fun onRestoreInstanceState(savedInstanceState: Bundle)
    {
        super.onRestoreInstanceState(savedInstanceState)
        dbHelperTrip = if (savedInstanceState.containsKey(
                "dbHelperTrip")) savedInstanceState.getSerializable(
            "dbHelperTrip") as DBHelperTrip
        else DBHelperTrip(this)
    } // End onRestoreInstanceState
    
    
    // Call CreateUpdateActivity for update
    override fun onClick(view: View?)
    {
        if (view != null)
        {
            val name = view.findViewById<TextView>(R.id.txtTripName).text
            val intent = Intent(this, CreateUpdateTripActivity::class.java)
            intent.putExtra("op", CreateUpdateTripActivity.UPDATE_OP)
            intent.putExtra("name", name)
            startActivity(intent)
        } // End if
    } // End onClick
    
    
    override fun onLongClick(view: View?): Boolean
    {
        class MyDialogInterfaceListener(val name: String) :
            DialogInterface.OnClickListener
        {
            override fun onClick(dialogInterface: DialogInterface?, which: Int)
            {
                if (which == DialogInterface.BUTTON_POSITIVE)
                {
                    try
                    {
                        val db = dbHelperTrip.writableDatabase
                        db.execSQL("""
                            DELETE FROM trips
                            WHERE tripName = "$name"
                        """)
                        populateRecyclerView()
                    } // End try
                    catch (_: Exception)
                    {
                    
                    } // End catch
                } // End if
            } // End onClick
        } // End MyDialogInterfaceListener class
        
        if (view != null)
        {
            val name =
                view.findViewById<TextView>(R.id.txtTripName).text.toString()
            val alertDialogBuilder = AlertDialog.Builder(this)
            alertDialogBuilder.setMessage(
                "Are you sure you want to delete item named ${name}?")
            alertDialogBuilder.setPositiveButton("Yes",
                MyDialogInterfaceListener(name))
            alertDialogBuilder.setNegativeButton("No",
                MyDialogInterfaceListener(name))
            alertDialogBuilder.show()
            return true
        } // End if
        return false
    } // End onLongClick
} // End TripListActivity class