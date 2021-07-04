package com.example.jrp_android

import android.content.Intent
import android.opengl.GLES30
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.example.jrp_android.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        if(currentUser!=null){
            startActivity(Intent(this@MainActivity,mainMenu::class.java))
            finish()
        }
        login()
    }

    private fun login(){
        binding.loginButton.setOnClickListener {
                        Toast.makeText(this@MainActivity, "Login Button was pressed ", Toast.LENGTH_LONG).show()
            if(TextUtils.isEmpty(binding.username.text.toString())) {
                this.username.error = "Please enter username"
                return@setOnClickListener
            }
            else if(TextUtils.isEmpty(binding.password.text.toString())){
                this.password.error = "Please enter password"
                return@setOnClickListener
            }
            auth.signInWithEmailAndPassword(binding.username.text.toString(), binding.password.text.toString())
                .addOnCompleteListener {
                    if(it.isSuccessful) {
                        startActivity(Intent(this@MainActivity, mainMenu::class.java))
                        Toast.makeText(this@MainActivity, "Authentication successful", Toast.LENGTH_LONG).show()
//                        finish()
                    } else {
                        Toast.makeText(this@MainActivity, "Login failed, please try again! ", Toast.LENGTH_LONG).show()
                    }
                }
        }

    }
}