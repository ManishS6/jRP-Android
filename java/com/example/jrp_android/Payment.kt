package com.example.jrp_android

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.google.firebase.firestore.FirebaseFirestore

class Payment : AppCompatActivity() {
    lateinit var db: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)
        db = FirebaseFirestore.getInstance()
        payment()
    }

    private fun Pfetch( party: String){
        val docRef = db.collection("users/naresh/connections/").document(party)
        docRef.get()
            .addOnSuccessListener { document ->
                Log.d(ContentValues.TAG, "Balance is data: ${document.data?.get("BALANCE")}")
                findViewById<TextView>(R.id.payment_party_balance).text=  "${document.data?.get("BALANCE")}"
            }
            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "got failed with ", exception) // logging the error message if failure
            }
    }

    private fun Wfetch(wallet: String){
        val docRef = db.collection("users").document("naresh")
        docRef.get()
            .addOnSuccessListener { document ->
                Log.d(ContentValues.TAG, "Balance is data: ${document.data?.get(wallet)}")
//                findViewById<TextView>(R.id.payment_wallet_balance).text=  "Balance: ₹${document.data?.get(wallet)}"
                findViewById<TextView>(R.id.payment_wallet_balance).text=  "${document.data?.get(wallet)}"
            }
            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "got failed with ", exception) // logging the error message if failure
            }
    }

    private fun PGenerate(partyChoosen: String, walletChoosen: String, textView1: String, textView2: String){
//        take →spinner1, spinner2, textView1, textView2 values
        Log.d(ContentValues.TAG, partyChoosen)
        Log.d(ContentValues.TAG, walletChoosen)
        Log.d(ContentValues.TAG, textView1)
        Log.d(ContentValues.TAG, textView2)

//        find the current payment number → Payment_Series
        val docRef = db.collection("users/naresh/Data").document("Series")
        docRef.get()
            .addOnSuccessListener { document ->
//                getting the current payment number
                var currentPaymentNumber =  document.data?.get("Payment_Series")
                Log.d(ContentValues.TAG, "Current Payment number is "+currentPaymentNumber.toString())
//                create a object of the current data
                val payment = hashMapOf(
                    "AMOUNT" to textView1,
                    "TO" to walletChoosen,
                    "Payment_Number" to currentPaymentNumber,
                    "FROM" to partyChoosen,
                    "NARRATION" to textView2
                )
//                create new payment
                db.collection("/users/naresh/Payment_Records").document("$currentPaymentNumber")
                    .set(payment)
                    .addOnSuccessListener { Log.d(ContentValues.TAG, "DocumentSnapshot successfully written!") }
                    .addOnFailureListener { e -> Log.w(ContentValues.TAG, "Error writing document", e) }
//                Increment the payment number series data
                val OPpn = Integer.parseInt(currentPaymentNumber.toString())+1
                val update = hashMapOf(
                    "Payment_Series" to OPpn
                )
                db.document("/users/naresh/Data/Series")
                    .update(update as Map<String, Any>)
                    .addOnSuccessListener { Log.d(ContentValues.TAG, "Payment_Series value incremented success!") }
                    .addOnFailureListener { e -> Log.w(ContentValues.TAG, "Payment_Series value not incremented", e) }
//                Update the balance of party
                val PincrementedPB = Integer.parseInt(findViewById<TextView>(R.id.payment_party_balance).text.toString())+Integer.parseInt(textView1)
                val PpartyMap = hashMapOf(
                    "BALANCE" to PincrementedPB
                )
                db.document("/users/naresh/connections/"+partyChoosen)
                    .update(PpartyMap as Map<String, Any>)
                    .addOnSuccessListener {
                        Log.d(ContentValues.TAG, "Party balance value incremented success!")
//                        Update the textView Party Balance
                        findViewById<TextView>(R.id.payment_party_balance).text = PincrementedPB.toString()
                    }
                    .addOnFailureListener { e -> Log.w(ContentValues.TAG, "Party balance value not incremented", e) }


//                Update the balance of wallet
                val PincrementedWB = Integer.parseInt(findViewById<TextView>(R.id.payment_wallet_balance).text.toString())-Integer.parseInt(textView1)
                val PwalletMap = hashMapOf(
                    walletChoosen to PincrementedWB
                )
                db.document("/users/naresh/")
                    .update(PwalletMap as Map<String, Any>)
                    .addOnSuccessListener {
                        Log.d(ContentValues.TAG, "Wallet balance value decremented success!")
//                        Update the textView Wallet Balance
                        findViewById<TextView>(R.id.payment_wallet_balance).text = PincrementedWB.toString()
                    }
                    .addOnFailureListener { e -> Log.w(ContentValues.TAG, "Wallet balance value not incremented", e) }

            }
            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "got failed with ", exception) // logging the error message if failure
            }

    }

    private fun payment(){
        var al = ArrayList<String>()
        al.add("Please choose the required user")
        val docRef = db.collection("users/naresh/connections")
        docRef.get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    al.add(document["NAME"] as String)
                    Log.d(ContentValues.TAG, "DocumentSnapshot data: ${document["NAME"]}")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "get failed with ", exception)
            }
        val spinner = findViewById<Spinner>(R.id.payment_parties) as Spinner
        val adapter= ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,al)
        var partyChoosen = ""
        var walletChoosen = ""
        spinner.adapter = adapter
        spinner.onItemSelectedListener=object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                Log.d(ContentValues.TAG,"You have Selected "+al[p2])
                Pfetch(al[p2])
                partyChoosen = al[p2]
            }
        }

        val spinner2 = findViewById<Spinner>(R.id.from) as Spinner
        val wallets = arrayOf("BOB","CASH")
        val adapter2= ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,wallets)
        spinner2.adapter = adapter2
        spinner2.onItemSelectedListener=object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
//                Toast.makeText(this@Payment,"You have Selected "+wallets[p2], Toast.LENGTH_SHORT).show()
                Log.d(ContentValues.TAG,"You have Selected "+wallets[p2])
                Wfetch(wallets[p2])
                walletChoosen= wallets[p2]
            }

        }


        val generateBtn = findViewById<Button>(R.id.payment_generate_button) as Button
        generateBtn.setOnClickListener {
            val textView1 = findViewById<EditText>(R.id.payment_amount_edit_text).text.toString()
            val textView2 = findViewById<EditText>(R.id.payment_narration_edit_text).text.toString()
            PGenerate(partyChoosen,walletChoosen,textView1,textView2)
        }
    }
}