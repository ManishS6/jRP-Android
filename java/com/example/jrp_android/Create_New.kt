package com.example.jrp_android

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.jrp_android.databinding.ActivityCreateNewBinding
import com.google.firebase.firestore.FirebaseFirestore

class Create_New : AppCompatActivity() {
    private lateinit var binding: ActivityCreateNewBinding
    lateinit var db: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateNewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = FirebaseFirestore.getInstance()
        create()
    }
    private  fun create(){
        var name = binding.accountsNewNameEditText.text

        var gst = binding.accountsNewGstEditText.text
        var address = binding.accountsNewAddrEditText.text
        var balance = binding.accountsNewBalEditText.text

        binding.accountsCreateButtonLocal.setOnClickListener(){
            var STRname = name.toString()
            var STRaddr = address.toString()
            var STRgst = gst.toString()
            var INTbalance = Integer.parseInt(balance.toString())

            val CNDocRef = db.collection("/users/naresh/connections").document(STRname)

            val CNObject = hashMapOf(
                "ADDRESS" to "default address ok"
            )
            CNDocRef.set(CNObject as Map<*, *>)
                .addOnSuccessListener {
                    Toast.makeText(this@Create_New,"DocumentSnapshot successfully written!", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e -> Log.w(ContentValues.TAG, "Error writing document", e) }
            Toast.makeText(this@Create_New,address, Toast.LENGTH_SHORT).show()
            CNDocRef.update("ADDRESS",STRaddr)
            CNDocRef.update("BALANCE",INTbalance)
            CNDocRef.update("GSTIN",STRgst)
            CNDocRef.update("NAME",STRname)


//            val connection = hashMapOf(
//                "ADDRESS" to address,
//                "BALANCE" to INTbalance,
//                "GSTIN" to gst,
//                "NAME" to STRname
//            )
//            docRef.set(connection)
//                .addOnSuccessListener { Log.d(ContentValues.TAG, "DocumentSnapshot successfully written!") }
//                .addOnFailureListener { e -> Log.w(ContentValues.TAG, "Error writing document", e) }
        }
    }
}