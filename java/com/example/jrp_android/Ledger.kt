package com.example.jrp_android

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.example.jrp_android.databinding.ActivityLedgerBinding
import com.google.firebase.firestore.FirebaseFirestore

class Ledger : AppCompatActivity() {
    private lateinit var binding: ActivityLedgerBinding
    lateinit var db: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLedgerBinding.inflate(layoutInflater)
        db = FirebaseFirestore.getInstance()
        setContentView(binding.root)
        ledge()
    }
    private fun ledge(){
        var al = ArrayList<String>()
        al.add("Please choose the required user")
//        Fetching data from Firebase form the location users/naresh/connections
        val docRef = db.collection("users/naresh/connections")
        docRef.get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    al.add(document["NAME"] as String) // adding the fetched document to the array list
                    Log.d(ContentValues.TAG, "DocumentSnapshot data: ${document["NAME"]}")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "get failed with ", exception) // logging the error message if failure
            }
        // creating the spinner ( dropdown menu ) for all the values in the array list
        val spinner = findViewById<Spinner>(R.id.ledger_parties) as Spinner
        val adapter= ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,al)
        var partyChoosen = ""
        spinner.adapter = adapter
        spinner.onItemSelectedListener=object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                Log.d(ContentValues.TAG,"You have Selected "+al[p2])
                partyChoosen = al[p2]
            }
        }

        binding.viewLedger.setOnClickListener() {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }
}