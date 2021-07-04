package com.example.jrp_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.viewpager.widget.ViewPager
import com.example.jrp_android.databinding.ActivityAccountsBinding
import com.google.android.material.tabs.TabLayout

class Accounts : AppCompatActivity() {
    private lateinit var binding: ActivityAccountsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.accountsCreateButton.setOnClickListener{creater()}
        binding.accountsModifyButton.setOnClickListener{modifier()}
    }
    private fun creater(){
        Toast.makeText(this@Accounts,"Create New Button was clicked", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this@Accounts,Create_New::class.java))
    }
    private fun modifier(){
        Toast.makeText(this@Accounts,"Modify Existing Button was clicked",Toast.LENGTH_SHORT).show()
        startActivity(Intent(this@Accounts,Modify_Existing::class.java))
    }
}