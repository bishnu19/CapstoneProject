package com.example.capstoneproject

class TripItem(var tripName: String, var bikeName: String, var date: String,
               var location: String)
{
    override fun toString(): String
    {
        return "TripItem(tripName='$tripName', bikeName='$bikeName', date='$date', location='$location')"
    }
}