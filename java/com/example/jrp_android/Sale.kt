package com.example.jrp_android

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.*
import com.google.firebase.firestore.FirebaseFirestore
import org.w3c.dom.Text

class Sale : AppCompatActivity() {
    lateinit var db: FirebaseFirestore
    var fetched_tag_cost : Any? = null
    var fetched_tag_goldType : Any? = null
    var fetched_tag_category : Any? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sale)
        db = FirebaseFirestore.getInstance()
        sale()
    }

    private fun Tfetch(tagChoosen: String){
        val docRef =  db.collection("/users/naresh/Products/").document(tagChoosen)
            docRef.get()
                .addOnSuccessListener { document ->
                    fetched_tag_category = document.data?.get("Category")
                    fetched_tag_cost = document.data?.get("COST")
                    fetched_tag_goldType = document.data?.get("GoldType")
                    findViewById<TextView>(R.id.sale_tag_category).text = fetched_tag_category.toString()
                    findViewById<TextView>(R.id.sale_tag_cost).text = fetched_tag_cost.toString()
                    findViewById<TextView>(R.id.sale_tag_gold_type).text = fetched_tag_goldType.toString()
                }
    }

    private fun sale(){
//    filling the spinner with values fetched from firebase
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
//         creating the spinner ( dropdown menu ) for all the values in the array list
        val spinner = findViewById<Spinner>(R.id.sale_parties) as Spinner
        val adapter= ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,al)
        var partyChoosen = ""
        spinner.adapter = adapter
        spinner.onItemSelectedListener=object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
//                Toast.makeText(this@Sale,"You have Selected "+al[p2], Toast.LENGTH_SHORT).show()
                Log.d(ContentValues.TAG,"You have Selected "+al[p2])
                partyChoosen = al[p2]
            }
        }

//    fetched tag number associated values from firebase\

        val decodeBtn = findViewById<Button>(R.id.decode_tag_button) as Button
        var tagChosen = findViewById<EditText>(R.id.sale_tag_edit_text).text
        val tc = tagChosen.toString()
        decodeBtn.setOnClickListener {
            Tfetch(tagChosen.toString())
        }

//    generate btn on click listener to create a new sale
        val generateBtn = findViewById<Button>(R.id.sale_generate_button) as Button
        generateBtn.setOnClickListener{
            //    fetch the old sale_record series number from data
            val saleDocRef = db.collection("/users/naresh/Data/").document("Series")
            saleDocRef.get()
                .addOnSuccessListener { document ->
                        var INcurrentSaleNumber = document.data?.get("Sale_Series")
                        var currentSaleNumber = Integer.parseInt(INcurrentSaleNumber.toString())
                        var OPsn = currentSaleNumber+1
//                        val newSaleObject = hashMapOf<String,Any?>(
//                            "Category" to fetched_tag_category,
//                            "Cost" to fetched_tag_cost,
//                            "GoldType" to fetched_tag_goldType,
//                            "Sale_Number" to currentSaleNumber,
//                            "SoldTo" to partyChoosen,
//                            "TagNo" to tagChosen
//                        )


                    // after to add toString etc for all
//                        Log.d(ContentValues.TAG,"The value is  "+newSaleObject)
                        Log.d(ContentValues.TAG,"Integer value for sale number currrent  is  "+currentSaleNumber)
                    //            Create a new Sale

                        val newSaleDocRef = db.collection("/users/naresh/Sale_Records").document(currentSaleNumber.toString())
                        val newSaleObject = hashMapOf(
                            "Category" to 0
                        )
                        newSaleDocRef.set(newSaleObject as Map<*, *>)
                            .addOnSuccessListener {Toast.makeText(this@Sale,"DocumentSnapshot successfully written!", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener { e -> Log.w(ContentValues.TAG, "Error writing document", e) }
                        newSaleDocRef.update("Category",fetched_tag_category)
                        newSaleDocRef.update("Cost",fetched_tag_cost)
                        newSaleDocRef.update("GoldType",fetched_tag_goldType)
                        newSaleDocRef.update("Sale_Number",currentSaleNumber)
                        newSaleDocRef.update("SoldTo",partyChoosen)
                        newSaleDocRef.update("TagNo",tc)
//                        newSaleDocRef.set(newSaleObject as Map<*, *>)
//                            .addOnSuccessListener {
////                                Log.d(ContentValues.TAG, "DocumentSnapshot successfully written!")
//                                Toast.makeText(this@Sale,"DocumentSnapshot successfully written!", Toast.LENGTH_SHORT).show()
//                            }
//                            .addOnFailureListener { e -> Log.w(ContentValues.TAG, "Error writing document", e) }

                    //            Increment sale series number
                        val DataObject = hashMapOf(
                            "Sale_Series" to OPsn
                        )
                        saleDocRef
                            .update(DataObject as Map<String, Any>)
                            .addOnSuccessListener { Log.d(ContentValues.TAG, "Sale number incremented succesfully!") }
                            .addOnFailureListener { e -> Log.w(ContentValues.TAG, "Error writing document", e) }

                                //            Update party balance
                        val PartyDocRef = db.collection("/users/naresh/connections").document(partyChoosen)
                        PartyDocRef.get()
                            .addOnSuccessListener { document ->
                                val INfetched_party_balance = document.data?.get("BALANCE")
                                var fetched_party_balance = Integer.parseInt(INfetched_party_balance.toString())

                                var newPB = fetched_party_balance+Integer.parseInt(fetched_tag_cost.toString())
                                val PartyObject = hashMapOf(
                                    "BALANCE" to newPB
                                )
                                PartyDocRef.update(PartyObject as Map<String, Any>)
                                    .addOnSuccessListener { Log.d(ContentValues.TAG, "Party balance incremented succesfully!") }
                                    .addOnFailureListener { e -> Log.w(ContentValues.TAG, "Error writing document", e) }


                            }
                            .addOnFailureListener{ e -> Log.w(ContentValues.TAG, "Error reading document", e) }

                    }
                .addOnFailureListener { e -> Log.w(ContentValues.TAG, "Error fetching sale record int document", e) }

        }
    }
}