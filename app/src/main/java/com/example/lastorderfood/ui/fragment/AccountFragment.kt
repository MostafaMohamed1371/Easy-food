package com.example.lastorderfood.ui.fragment

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.lastorderfood.R
import com.example.lastorderfood.databinding.FragmentAccountBinding
import com.example.lastorderfood.login.DataLogin
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import io.github.muddz.styleabletoast.StyleableToast
import kotlinx.android.synthetic.main.fragment_account.*
import java.io.File
import java.util.*


class AccountFragment : Fragment() {
    private lateinit var binding: FragmentAccountBinding
    private lateinit var user: FirebaseUser
    private lateinit var reference: DatabaseReference
    private lateinit var userId: String
    private lateinit var mStorage: StorageReference
    private lateinit var imageStorage: StorageReference
    private lateinit var changePassword:String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        user = FirebaseAuth.getInstance().currentUser!!
        reference = FirebaseDatabase.getInstance().getReference("Register")
        userId = user.uid
        mStorage = FirebaseStorage.getInstance().reference
        imageStorage=FirebaseStorage.getInstance().getReferenceFromUrl("gs://easy-food-ed236.appspot.com/").child(FirebaseAuth.getInstance().currentUser!!.uid)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentAccountBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        referenceRealDb()
        updateProfile()
        //changeProfile()
        getImageFromFirebase()


    }
//    override fun onResume() {
//        super.onResume()
//        (activity as AppCompatActivity).supportActionBar?.hide()
//    }
    private fun getImageFromFirebase() {
        val file:File=File.createTempFile("image","jpeg")
        imageStorage.getFile(file).addOnSuccessListener {
            val  bitmap:Bitmap=BitmapFactory.decodeFile(file.absolutePath)
            binding.imageProfile.setImageBitmap(bitmap)
        }.addOnFailureListener {
            StyleableToast.makeText(requireActivity(),"Image failed to load",R.style.exampleToast).show()
        }
    }

    private fun changeProfile() {
       var user = FirebaseAuth.getInstance().currentUser!!
        val password=binding.editTextTextPassword.text.toString()
        val email=binding.editTextTextEmailAddress3.text.toString()
        binding.UpdateProfile.setOnClickListener {
            if(checkPassword()){
                user.updatePassword(password)?.addOnCompleteListener {
                    if (it.isSuccessful){
                        StyleableToast.makeText(requireActivity(), "update password successful ", R.style.exampleToast).show()
                    }else{
                        StyleableToast.makeText(requireActivity(), it.exception.toString(), R.style.exampleToast).show()
                    }
                }
            }
            if(checkEmail()){
                user?.updateEmail(email)?.addOnCompleteListener {
                    if (it.isSuccessful){
                        StyleableToast.makeText(requireActivity(), "update email successful ", R.style.exampleToast).show()
                    }else{
                        StyleableToast.makeText(requireActivity(), it.exception.toString(), R.style.exampleToast).show()
                    }
                }
            }

        }
    }





    private fun checkPassword():Boolean{
        if(binding.editTextTextPassword.text.toString()==""){
            StyleableToast.makeText(requireActivity(),"password is empty",R.style.exampleToast).show()
            return false
        }
        if(binding.editTextTextPassword.text.toString().length<6){
            StyleableToast.makeText(requireActivity(),"password is leaast than 6 character",R.style.exampleToast).show()
            return false
        }
        return true

    }

    private fun checkEmail():Boolean{
        val email=binding.editTextTextEmailAddress3.text.toString()
        if(email==""){
            StyleableToast.makeText(requireActivity(),"Email is empty",R.style.exampleToast).show()
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            StyleableToast.makeText(requireActivity(), "Please provide valid email!", R.style.exampleToast).show()
            return false
        }
        return true

    }
    private fun checkUserName():Boolean{
        val userName=binding.editTextTextUserName.toString()
        if(userName==""){
            StyleableToast.makeText(requireActivity(),"userName is empty",R.style.exampleToast).show()
            return false
        }

        return true

    }
    lateinit var nameChange:String
    lateinit var emailChange:String
    lateinit var passChange:String
    private fun referenceRealDb() {
        reference.child(userId).addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                var userProfile: DataLogin = snapshot.getValue(DataLogin::class.java)!!
                nameChange=snapshot.child("userName").getValue().toString()
                emailChange=snapshot.child("email").getValue().toString()
                passChange=snapshot.child("password").getValue().toString()
                if (userProfile!=null){
                    binding.editTextTextUserName.setText(userProfile.userName)
                        binding.editTextTextEmailAddress3.setText(userProfile.email)
                        binding.editTextTextPassword.setText(userProfile.password)
                }

            }


            override fun onCancelled(error: DatabaseError) {
                StyleableToast.makeText(activity!!,"sucess add",R.style.exampleToast).show()
            }

        })
    }

    private fun updateProfile() {
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("Register")
        binding.UpdateProfile.setOnClickListener {
            if (binding.editTextTextUserName.text.toString()
                    .isEmpty() || binding.editTextTextEmailAddress3.text.toString()
                    .isEmpty() || binding.editTextTextPassword.text.toString().isEmpty()
            ) {
                StyleableToast.makeText(requireActivity(), "one or many field is empty", R.style.exampleToast).show()
            } else {
                //
                // user.updateEmail(email).addOnSuccessListener(object : OnSuccessListener<Void>{
                //   override fun onSuccess(p0: Void?) {

//                var edited = HashMap<String, String>()
//                var email = binding.editTextTextUserName.text.toString().trim()
//                edited.put("email", email)
//                edited.put("password", binding.editTextTextPassword.text.toString())
//                edited.put("userName", binding.editTextTextUserName.text.toString())
//
//                //  myRef.push().setValue(arrayRegister)
//                myRef.child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(edited)
//                    .addOnCompleteListener {
//                        if (it.isSuccessful) {
//                            StyleableToast.makeText(requireActivity(),
//                                "user has been change successful",
//                                R.style.exampleToast).show()
//                        } else {
//                            StyleableToast.makeText(requireActivity(), "Failed to change try again", R.style.exampleToast).show()
//                        }
//                    }

          /*      var credential:AuthCredential = EmailAuthProvider
                    .getCredential(emailChange,passChange)

                user.reauthenticate(credential)
                    .addOnCompleteListener(object : OnCompleteListener<Void> {

                        override fun onComplete(p0: Task<Void>) {
                            if (p0.isSuccessful()) {
                                user.updatePassword(binding.editTextTextPassword.text.toString()).addOnCompleteListener(object :OnCompleteListener<Void>{
                                    override fun onComplete(p0: Task<Void>) {
                                        if (p0.isSuccessful()) {
                                            StyleableToast.makeText(requireActivity(), "Password updated", R.style.exampleToast).show()
                                        } else {
                                            StyleableToast.makeText(requireActivity(), "Error password not updated", R.style.exampleToast).show()
                                        }
                                    }
                                });

                                user.updateEmail(binding.editTextTextEmailAddress3.text.toString()).addOnCompleteListener(object :OnCompleteListener<Void>{
                                    override fun onComplete(p0: Task<Void>) {
                                        if (p0.isSuccessful()) {
                                            StyleableToast.makeText(requireActivity(), "Email updated", R.style.exampleToast).show()
                                        } else {
                                            StyleableToast.makeText(requireActivity(), "Error email not updated", R.style.exampleToast).show()
                                        }
                                    }
                                });
                            } else {
                                StyleableToast.makeText(requireActivity(), "Error auth failed", R.style.exampleToast).show()
                            }
                        }
                    })*/
                if (checkUserName()) {
                    reference.child(FirebaseAuth.getInstance().currentUser!!.uid).child("userName")
                        .setValue(binding.editTextTextUserName.text.toString())
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                StyleableToast.makeText(requireActivity(),
                                    "update userName",
                                    R.style.exampleToast).show()
                            } else {
                                StyleableToast.makeText(requireActivity(),
                                    "failed update userName",
                                    R.style.exampleToast).show()
                            }
                        }
                }
                val user2 = FirebaseAuth.getInstance().currentUser
                val credential = EmailAuthProvider.getCredential(emailChange, passChange)
                user2!!.reauthenticate(credential).addOnCompleteListener { task: Task<Void?>? ->
                    val user1 = FirebaseAuth.getInstance().currentUser
                    if (checkEmail()) {
                        user1!!.updateEmail(binding.editTextTextEmailAddress3.text.toString())
                            .addOnCompleteListener { task1: Task<Void?> ->
                                if (task1.isSuccessful) {
                                    reference.child(user2.uid).child("email")
                                        .setValue(binding.editTextTextEmailAddress3.text.toString())
                                    StyleableToast.makeText(requireActivity(),
                                        "update email",
                                        R.style.exampleToast).show()
                                } else {
                                    StyleableToast.makeText(requireActivity(),
                                        "fail update email",
                                        R.style.exampleToast).show()
                                }
                            }
                    }
                    if (checkPassword()) {
                        user1!!.updatePassword(binding.editTextTextPassword.text.toString())
                            .addOnCompleteListener { task1: Task<Void?> ->
                                if (task1.isSuccessful) {
                                    reference.child(user2.uid).child("password")
                                        .setValue(binding.editTextTextPassword.text.toString())
                                    StyleableToast.makeText(requireActivity(),
                                        "update password",
                                        R.style.exampleToast).show()
                                } else {
                                    StyleableToast.makeText(requireActivity(),
                                        "fail update password",
                                        R.style.exampleToast).show()
                                }
                            }
                    }
                }



            }

            //  }).addOnFailureListener(object :OnFailureListener{
            //    override fun onFailure(p0: Exception) {
            //      StyleableToast.makeText(requireActivity(),p0.message,R.style.exampleToast).show()
            // }

            //})
        }

        binding.ChangePhoto.setOnClickListener {
            val intentImage = Intent(Intent.ACTION_PICK)
            intentImage.type = "image/*"
            startActivityForResult(intentImage, 1)


        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==1&&resultCode==RESULT_OK&&data!=null){
            val uriImage=data?.data
            val filePath=mStorage.child(FirebaseAuth.getInstance().currentUser!!.uid)
            if (uriImage != null) {
                filePath.putFile(uriImage).addOnSuccessListener {
                    StyleableToast.makeText(requireActivity(),"upload Image",R.style.exampleToast).show()
                }.addOnFailureListener {
                    StyleableToast.makeText(requireActivity(),"error upload",R.style.exampleToast).show()
                }
            }
        }
    }

}