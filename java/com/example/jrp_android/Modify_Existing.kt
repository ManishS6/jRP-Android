package com.example.jrp_android

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.example.jrp_android.databinding.ActivityModifyExistingBinding
import com.google.firebase.firestore.FirebaseFirestore

class Modify_Existing : AppCompatActivity() {
    private lateinit var binding: ActivityModifyExistingBinding
    lateinit var db: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityModifyExistingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = FirebaseFirestore.getInstance()
        modify()
    }

    private fun DFetch(partyChoosen:String){
        var fetched_addr : Any? = null
        var fetched_bal : Any? = null
        var fetched_gst : Any? = null
        val docRef = db.collection("users/naresh/connections").document(partyChoosen)
        docRef.get()
            .addOnSuccessListener { document ->
                fetched_addr = document.data?.get("ADDRESS")
                fetched_bal = document.data?.get("BALANCE")
                fetched_gst = document.data?.get("GSTIN")
                findViewById<TextView>(R.id.accounts_gst_edit_text).text = fetched_gst.toString()
                findViewById<TextView>(R.id.accounts_addr_edit_text).text = fetched_addr.toString()
                findViewById<TextView>(R.id.accounts_bal_edit_text).text = fetched_bal.toString()

                binding.accountsModifyButton.setOnClickListener(){
                    val gstET = findViewById<EditText>(R.id.accounts_gst_edit_text) as EditText
                    val one = gstET.text
                    val two = findViewById<EditText>(R.id.accounts_addr_edit_text).text
                    val three = findViewById<EditText>(R.id.accounts_bal_edit_text).text
//                    val modifiedP = hashMapOf(
//                        "ADDRESS" to two,
//                        "BALANCE" to three,
//                        "GSTIN" to one
//                    )
                    val MEdocRef = db.collection("users/naresh/connections").document(partyChoosen)
                    MEdocRef.update("ADDRESS",two.toString())
                    MEdocRef.update("BALANCE",three.toString())
                    MEdocRef.update("GSTIN",one.toString())
                    Toast.makeText(this@Modify_Existing, " Details Modified Successfully ", Toast.LENGTH_LONG).show()
                }


            }
            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "get failed with ", exception) // logging the error message if failure
            }
    }

    private fun modify(){
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
        val spinner = findViewById<Spinner>(R.id.accounts_modify_parties) as Spinner
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
                DFetch(partyChoosen)
            }
        }
    }
}