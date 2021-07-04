package com.example.jrp_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.content.ContentValues.TAG
import android.view.View
import android.widget.*
import com.google.firebase.firestore.FirebaseFirestore


class Receipt : AppCompatActivity() {
    lateinit var db: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receipt)
        db = FirebaseFirestore.getInstance()
        receipt()
    }
    private fun Pfetch( party: String){
        val docRef = db.collection("users/naresh/connections/").document(party)
        docRef.get()
            .addOnSuccessListener { document ->
                Log.d(TAG, "Balance is data: ${document.data?.get("BALANCE")}")
                findViewById<TextView>(R.id.receipt_party_balance).text=  "${document.data?.get("BALANCE")}"
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "got failed with ", exception) // logging the error message if failure
            }
    }

    private fun Wfetch(wallet: String){
        val docRef = db.collection("users").document("naresh")
        docRef.get()
            .addOnSuccessListener { document ->
                Log.d(TAG, "Balance is data: ${document.data?.get(wallet)}")
//                findViewById<TextView>(R.id.receipt_wallet_balance).text=  "Balance: ₹${document.data?.get(wallet)}"
                findViewById<TextView>(R.id.receipt_wallet_balance).text=  "${document.data?.get(wallet)}"
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "got failed with ", exception) // logging the error message if failure
            }
    }

    private fun RGenerate(partyChoosen: String, walletChoosen: String, textView1: String, textView2: String){
//        take →spinner1, spinner2, textView1, textView2 values
        Log.d(TAG, partyChoosen)
        Log.d(TAG, walletChoosen)
        Log.d(TAG, textView1)
        Log.d(TAG, textView2)

//        find the current receipt number →Reciept_Series
        val docRef = db.collection("users/naresh/Data").document("Series")
         docRef.get()
            .addOnSuccessListener { document ->
//                getting the current receipt number
                var currentReceiptNumber =  document.data?.get("Reciept_Series")
                Log.d(TAG, "Current Receipt number is "+currentReceiptNumber.toString())
//                create a object of the current data
                val receipt = hashMapOf(
                    "AMOUNT" to textView1,
                    "FROM" to walletChoosen,
                    "Reciept_Number" to currentReceiptNumber,
                    "TO" to partyChoosen,
                    "NARRATION" to textView2
                )
//                create new receipt
                db.collection("/users/naresh/Reciept_Records").document("$currentReceiptNumber")
                    .set(receipt)
                    .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
                    .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
//                Increment the receipt number series data
                val OPrn = Integer.parseInt(currentReceiptNumber.toString())+1
                val update = hashMapOf(
                    "Reciept_Series" to OPrn
                )
                db.document("/users/naresh/Data/Series")
                    .update(update as Map<String, Any>)
                    .addOnSuccessListener { Log.d(TAG, "Recipet value incremented success!") }
                    .addOnFailureListener { e -> Log.w(TAG, "reciept value not incremented", e) }
//                Update the balance of party
                val RincrementedPB = Integer.parseInt(findViewById<TextView>(R.id.receipt_party_balance).text.toString())-Integer.parseInt(textView1)
                val RpartyMap = hashMapOf(
                    "BALANCE" to RincrementedPB
                )
                db.document("/users/naresh/connections/"+partyChoosen)
                    .update(RpartyMap as Map<String, Any>)
                    .addOnSuccessListener {
                        Log.d(TAG, "Party balance value incremented success!")
//                        Update the textView Party Balance
                        findViewById<TextView>(R.id.receipt_party_balance).text = RincrementedPB.toString()
                    }
                    .addOnFailureListener { e -> Log.w(TAG, "Party balance value not incremented", e) }


//                Update the balance of wallet
                val RincrementedWB = Integer.parseInt(findViewById<TextView>(R.id.receipt_wallet_balance).text.toString())+Integer.parseInt(textView1)
                val RwalletMap = hashMapOf(
                    walletChoosen to RincrementedWB
                )
                db.document("/users/naresh/")
                    .update(RwalletMap as Map<String, Any>)
                    .addOnSuccessListener {
                        Log.d(TAG, "Wallet balance value incremented success!")
//                        Update the textView Wallet Balance
                        findViewById<TextView>(R.id.receipt_wallet_balance).text = RincrementedWB.toString()
                    }
                    .addOnFailureListener { e -> Log.w(TAG, "Wallet balance value not incremented", e) }

            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "got failed with ", exception) // logging the error message if failure
            }

    }

    private fun receipt(){
        var al = ArrayList<String>()
        al.add("Please choose the required user")
//        Fetching data from Firebase form the location users/naresh/connections
        val docRef = db.collection("users/naresh/connections")
        docRef.get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    al.add(document["NAME"] as String) // adding the fetched document to the array list
                    Log.d(TAG, "DocumentSnapshot data: ${document["NAME"]}")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception) // logging the error message if failure
            }
        // creating the spinner ( dropdown menu ) for all the values in the array list
        val spinner = findViewById<Spinner>(R.id.parties) as Spinner
        val adapter= ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,al)
        var partyChoosen = ""
        var walletChoosen = ""
        spinner.adapter = adapter
        spinner.onItemSelectedListener=object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
               Log.d(TAG,"You have Selected "+al[p2])
                Pfetch(al[p2])
                partyChoosen = al[p2]
            }
        }

//        Creating drop down menu for Wallet types like BOB CASH DIFF.AC
        val spinner2 = findViewById<Spinner>(R.id.to) as Spinner
        val wallets = arrayOf("BOB","CASH")
        val adapter2= ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,wallets)
        spinner2.adapter = adapter2
        spinner2.onItemSelectedListener=object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
//                Toast.makeText(this@Receipt,"You have Selected "+wallets[p2], Toast.LENGTH_SHORT).show()
                Log.d(TAG,"You have Selected "+wallets[p2])
                Wfetch(wallets[p2])
                walletChoosen= wallets[p2]
            }
        }


        val generateBtn = findViewById<Button>(R.id.receipt_generate_button) as Button
        generateBtn.setOnClickListener {
            val textView1 = findViewById<EditText>(R.id.receipt_amount_edit_text).text.toString()
            val textView2 = findViewById<EditText>(R.id.receipt_narration_edit_text).text.toString()
            RGenerate(partyChoosen,walletChoosen,textView1,textView2)
        }
    }
}