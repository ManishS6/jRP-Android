package com.example.jrp_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_recycler_view.*

class RecyclerViewActivity : AppCompatActivity() {
    private var db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val cf : CollectionReference = db.collection("/users/naresh/Reciept_Records")

    var usersAdapter : UsersAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view)
//        db = FirebaseFirestore.getInstance()

        setUpRV()
    }

    private fun setUpRV() {
        val query : Query = cf
        val firestoreRecyclerOptions: FirestoreRecyclerOptions<UsersModel> = FirestoreRecyclerOptions.Builder<UsersModel>()
            .setQuery(query, UsersModel::class.java)
            .build()

        usersAdapter = UsersAdapter(firestoreRecyclerOptions)

        rcv.layoutManager = LinearLayoutManager(this)
        rcv.adapter = usersAdapter

    }

    override fun onStart() {
        super.onStart()
        usersAdapter!!.startListening()
    }

    override fun onStop() {
        super.onStop()
        usersAdapter!!.stopListening()
    }
}