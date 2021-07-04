package com.example.jrp_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.jrp_android.databinding.ActivityMainMenuBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main_menu.*

class mainMenu : AppCompatActivity() {
    private lateinit var binding: ActivityMainMenuBinding
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        gallery()
    }
    private fun gallery(){
        binding.logout.setOnClickListener(){
            Toast.makeText(this@mainMenu, "Logout was clicked ", Toast.LENGTH_LONG).show()
            auth.signOut()
            startActivity(Intent(this,MainActivity::class.java))
        }
        binding.reciept.setOnClickListener(){
            Toast.makeText(this@mainMenu,"Receipt text was clicked",Toast.LENGTH_SHORT).show()
            startActivity(Intent(this,Receipt::class.java))
//            finish()
        }
        binding.payment.setOnClickListener(){
            Toast.makeText(this@mainMenu,"Payment text was clicked",Toast.LENGTH_SHORT).show()
            startActivity(Intent(this,Payment::class.java))
//            finish()
        }
        binding.sale.setOnClickListener(){
            Toast.makeText(this@mainMenu,"Sale text was clicked",Toast.LENGTH_SHORT).show()
            startActivity(Intent(this,Sale::class.java))
//            finish()
        }
        binding.ledger.setOnClickListener(){
            Toast.makeText(this@mainMenu,"Ledger text was clicked",Toast.LENGTH_SHORT).show()
//            startActivity(Intent(this,Ledger::class.java))
            startActivity(Intent(this,RecyclerViewActivity::class.java))
//            finish()
        }
        binding.accounts.setOnClickListener(){
            Toast.makeText(this@mainMenu,"Accounts text was clicked",Toast.LENGTH_SHORT).show()
            startActivity(Intent(this,Accounts::class.java))
//            finish()
        }
    }
}