package com.example.lastorderfood.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import com.example.lastorderfood.R
import com.example.lastorderfood.databinding.ActivityRegisterBinding
import com.example.lastorderfood.login.DataLogin
import com.example.lastorderfood.ui.fragment.HomeFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import io.github.muddz.styleabletoast.StyleableToast


class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    lateinit var mAuth: FirebaseAuth
    companion object{
        const val USERNAME="com.example.lastorderfood.userName"

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mAuth= FirebaseAuth.getInstance()
        binding.Register.setOnClickListener {_->

            createUser()
          //  transUser()
        }


        binding.returnLogin.setOnClickListener {
            val intent= Intent(this,LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

 //  private fun transUser() {
//
//
//     //   val intent=Intent(this@RegisterActivity,HomeActivity::class.java)
//      //  intent.putExtra(USERNAME,binding.editTextTextUserName.text.toString())
//      //  startActivity(intent)
//       // finish()
//
//    val fragment= HomeFragment()
//     val bundle=Bundle()
//      bundle.putString(USERNAME,binding.editTextTextUserName.text.toString())
//     fragment.arguments=bundle
//      supportFragmentManager.beginTransaction().replace(R.id.sub_fragment,fragment).commit()

  // }

    fun createUser(){
        val userName=binding.editTextTextUserName.text.toString()
        val email=binding.editTextTextEmailAddress.text.toString()
        val pass=binding.editTextTextPassword.text.toString()
        val confirmPass=binding.editTextTextConfirmPassword.text.toString()

        if (email.isNotEmpty()&&pass.isNotEmpty()&&pass.equals(confirmPass)&&userName.isNotEmpty()) {
            binding.progressBarRegister.visibility=View.VISIBLE
            mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        var Register=DataLogin(userName,email,pass)
                        val database = FirebaseDatabase.getInstance()
                        val myRef = database.getReference("Register")


                          //  myRef.push().setValue(arrayRegister)
                        myRef.child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(Register).addOnCompleteListener {
                            if (it.isSuccessful) {
                                StyleableToast.makeText(this,"user has been registered successful",R.style.exampleToast).show()
                            }else{
                                StyleableToast.makeText(this,"Failed to register! try again",R.style.exampleToast).show()
                            }
                        }
                     //   myRef.child("Register").child(userName).child("email").setValue(email)
                      //  myRef.child("Register").child(userName).child("password").setValue(pass)

                        binding.progressBarRegister.visibility = View.GONE
//                      val t= Toast.makeText(this, "sucess add", Toast.LENGTH_LONG)
//                        t.view!!.setBackgroundColor(Color.parseColor("#FF9800"))
//                        t.show()
                        StyleableToast.makeText(this,"sucess add",R.style.exampleToast).show()
                        startActivity(Intent(this, HomeActivity::class.java))
                        finish()
                    } else {
                        binding.progressBarRegister.visibility = View.GONE
//                        val t= Toast.makeText(this, it.exception!!.localizedMessage.toString(), Toast.LENGTH_LONG)
//                        t.view!!.setBackgroundColor(Color.parseColor("#FF9800"))
//                        t.show()
                        StyleableToast.makeText(this,it.exception!!.localizedMessage.toString(),R.style.exampleToast).show()
                    }
                }
        }else{
            binding.progressBarRegister.visibility = View.GONE
            if (userName.isEmpty()){
               // StyleableToast.makeText(this,"userName is empty",R.style.exampleToast).show()
                binding.editTextTextUserName.setError("userName is empty")
            }
             if (email.isEmpty()){
//                val t= Toast.makeText(this, "emial is empty", Toast.LENGTH_LONG)
//                t.view!!.setBackgroundColor(Color.parseColor("#FF9800"))
//                t.show()
               // StyleableToast.makeText(this,"emial is empty",R.style.exampleToast).show()
                binding.editTextTextEmailAddress.setError("emial is empty")
            }
            if (pass.isEmpty()) {
//                val t=   Toast.makeText(this, "password is empty", Toast.LENGTH_LONG)
//                t.view!!.setBackgroundColor(Color.parseColor("#FF9800"))
//                t.show()
               // StyleableToast.makeText(this,"password is empty",R.style.exampleToast).show()
                binding.editTextTextPassword.setError("password is empty")
            }
           if (confirmPass.isEmpty()) {
//                val t=   Toast.makeText(this, "confirmPassword is empty", Toast.LENGTH_LONG)
//                t.view!!.setBackgroundColor(Color.parseColor("#FF9800"))
//                t.show()
               // StyleableToast.makeText(this,"confirmPassword is empty",R.style.exampleToast).show()
               binding.editTextTextConfirmPassword.setError("confirmPassword is empty")
            }
            else if (confirmPass!==pass) {
//                val t=   Toast.makeText(this, "confirmPassword is not egual password", Toast.LENGTH_LONG)
//                t.view!!.setBackgroundColor(Color.parseColor("#FF9800"))
//                t.show()
                StyleableToast.makeText(this,"confirmPassword is not egual password",R.style.exampleToast).show()
            }
        }
    }
    override fun onBackPressed() {
        finishActivity(R.layout.activity_register)
    }
}