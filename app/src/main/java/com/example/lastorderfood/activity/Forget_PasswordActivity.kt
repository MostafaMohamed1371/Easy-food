package com.example.lastorderfood.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.util.Patterns
import android.view.View
import com.example.lastorderfood.R
import com.example.lastorderfood.databinding.ActivityForgetPasswordBinding

import com.google.firebase.auth.FirebaseAuth
import io.github.muddz.styleabletoast.StyleableToast


class Forget_PasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityForgetPasswordBinding
    lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityForgetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mAuth= FirebaseAuth.getInstance()
        binding.ResetPassword.setOnClickListener {
            ResetPassword()
        }
    }

    private fun ResetPassword() {
        val email = binding.editTextTextEmailAddress2.text.toString().trim()
        if (email.isEmpty()) {
          //  StyleableToast.makeText(this, "Email is empty", R.style.exampleToast).show()
            binding.editTextTextEmailAddress2.setError("Email is empty")
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            StyleableToast.makeText(this, "Please provide valid email!", R.style.exampleToast).show()
        } else {
            binding.progressBar.visibility = View.VISIBLE
            mAuth.sendPasswordResetEmail(email).addOnCompleteListener {
                if (it.isSuccessful) {
                    binding.progressBar.visibility = View.GONE
                    StyleableToast.makeText(this,
                        "Check your email to reset your password",
                        R.style.exampleToast).show()
                } else {
                    binding.progressBar.visibility = View.GONE
                    StyleableToast.makeText(this,
                        "Try again something wrong happen",
                        R.style.exampleToast).show()
                }
            }
        }
    }
}