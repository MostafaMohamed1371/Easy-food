package com.example.lastorderfood.activity

import android.app.ProgressDialog.show
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.lastorderfood.R
import com.example.lastorderfood.databinding.ActivityLoginBinding
import com.example.lastorderfood.ui.fragment.HomeFragment
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import io.github.muddz.styleabletoast.StyleableToast


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
       mAuth= FirebaseAuth.getInstance()

        binding.login.setOnClickListener{_->

            signIN()
        }
        binding.Register.setOnClickListener {
           val intent=Intent(this,RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.forgetPassword.setOnClickListener {
            val i=Intent(this,Forget_PasswordActivity::class.java)
            startActivity(i)
        }
    }
    fun signIN(){
        val email=binding.editTextTextEmailAddress.text.toString().trim()
        val pass=binding.editTextTextPassword.text.toString().trim()

        if (email.isNotEmpty()&&pass.isNotEmpty()) {
            binding.progressBarLogin.visibility=View.VISIBLE
            mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                if (it.isSuccessful) {
                    binding.progressBarLogin.visibility=View.GONE
                // val t= Toast.makeText(this, "sucess login", Toast.LENGTH_LONG)
                  //  t.view!!.setBackgroundColor(Color.parseColor("#FF9800"))
                    //t.show()
                    StyleableToast.makeText(this,"sucess login",R.style.exampleToast).show()
                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                } else {
                    binding.progressBarLogin.visibility=View.GONE
//                    val t= Toast.makeText(this, it.exception!!.localizedMessage.toString(), Toast.LENGTH_LONG)
//                    t.view!!.setBackgroundColor(Color.parseColor("#FF9800"))
//                    t.show()
                    StyleableToast.makeText(this,it.exception!!.localizedMessage.toString(),R.style.exampleToast).show()

                }
            }
        }else{
            binding.progressBarLogin.visibility=View.GONE
            if (email.isEmpty()){
//                val t= Toast.makeText(this, "emial is empty", Toast.LENGTH_LONG)
//             t.view!!.setBackgroundColor(Color.parseColor("#FF9800"))
//                t.show()
              //  StyleableToast.makeText(this,"emial is empty",R.style.exampleToast).show()
                binding.editTextTextEmailAddress.setError("emial is empty")
              }
                if (pass.isEmpty()) {
//                    val t=   Toast.makeText(this, "password is empty", Toast.LENGTH_LONG)
//                  t.view!!.setBackgroundColor(Color.parseColor("#FF9800"))
//                    t.show()
                   // StyleableToast.makeText(this,"password is empty",R.style.exampleToast).show()
                    binding.editTextTextPassword.setError("password is empty")
                 }


//            if (email.isEmpty()){
//                binding.userValidition.visibility=View.VISIBLE
//            }else{
//                binding.userValidition.visibility=View.GONE
//            }
//            binding.userValidition.visibility=View.GONE
//            if (pass.isEmpty()) {
//                binding.passValidition.visibility = View.VISIBLE
//            }else{
//                binding.passValidition.visibility=View.GONE
//            }
//            binding.passValidition.visibility=View.GONE
//            if (pass< 6.toString()){
//                binding.passValidition.text="this is less than 6 character"
//                binding.passValidition.visibility = View.VISIBLE
//            }else{
//                binding.passValidition.visibility=View.GONE
//            }
//            binding.passValidition.visibility=View.GONE
        }
    }

    override fun onBackPressed() {
         finishActivity(R.layout.activity_login)
    }

}