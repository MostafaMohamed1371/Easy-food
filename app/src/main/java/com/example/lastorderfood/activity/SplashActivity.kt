package com.example.lastorderfood.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.lastorderfood.R
import com.example.lastorderfood.databinding.ActivitySplash2Binding
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplash2Binding
    lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySplash2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        mAuth= FirebaseAuth.getInstance()


        binding.buttonSplash.setOnClickListener {
            binding.progressBarSplash.visibility=View.VISIBLE
            if (mAuth.currentUser!=null){
                val intent= Intent(this,HomeActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                val intent= Intent(this,LoginActivity::class.java)
                startActivity(intent)
                finish()

            }

        }
    }
}