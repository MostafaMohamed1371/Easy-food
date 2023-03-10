package com.example.lastorderfood.activity

import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.lastorderfood.R
import com.example.lastorderfood.broadcast.BroadCast_reciver
import com.example.lastorderfood.login.DataLogin
import com.example.lastorderfood.roomdb.MealDatabase
import com.example.lastorderfood.service.service_forground
import com.example.lastorderfood.viewmodel.HomeViewModel
import com.example.lastorderfood.viewmodel.HomeViewModelFactory
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import io.github.muddz.styleabletoast.StyleableToast
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.hardware.*
import kotlinx.android.synthetic.main.hardware.view.*
import java.io.File

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    val viewModel: HomeViewModel by lazy {
        val mealDatabase= MealDatabase.getinstance(this)
        val viewModelFactory= HomeViewModelFactory(mealDatabase)
        ViewModelProvider(this,viewModelFactory)[HomeViewModel::class.java]
    }
    private lateinit var user: FirebaseUser
    private lateinit var reference: DatabaseReference
    private lateinit var userId: String
    private lateinit var imageStorage: StorageReference
    private val navController by lazy {
        Navigation.findNavController(this, R.id.sub_fragment)
    }
    private var changeData= MutableLiveData<Any>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        val button_navigation= ActionBarDrawerToggle(
            this,drawerLayout,toolbar,R.string.open,R.string.close
        )
        drawerLayout.addDrawerListener(button_navigation)
        button_navigation.syncState()
        navigationView.setNavigationItemSelectedListener(this)
        imageStorage= FirebaseStorage.getInstance().getReferenceFromUrl("gs://easy-food-ed236.appspot.com/").child(FirebaseAuth.getInstance().currentUser!!.uid)
        user = FirebaseAuth.getInstance().currentUser!!
        reference = FirebaseDatabase.getInstance().getReference("Register")
        userId = user.uid
        val broadcast= BroadCast_reciver()
        val filter= IntentFilter()
        filter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED)
        registerReceiver(broadcast,filter)
        val navcontroller=findNavController(R.id.sub_fragment)
        NavigationUI.setupActionBarWithNavController(this,navcontroller)
        bottom_nav.setupWithNavController(navcontroller )
        val intent=Intent(this, service_forground::class.java)
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            startForegroundService(intent)
        }
        else{
            startService(intent)
        }

            getDataToNavigation()
           getImageFromFirebase()



        setupDrawerLayout()

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            if (destination.id == R.id.homeFragment) {
                supportActionBar?.show()
            } else {
                supportActionBar?.hide()
            }
        }

    }

   private  fun  getImageFromFirebase() {
        val file: File = File.createTempFile("image","jpeg")
       progressBarNavigation.visibility= View.VISIBLE
        imageStorage.getFile(file).addOnSuccessListener {
            progressBarNavigation.visibility= View.GONE
            navigationView.invalidate()
            val  bitmap: Bitmap = BitmapFactory.decodeFile(file.absolutePath)
            navigationView.imageNavigation.setImageBitmap(bitmap)
            navigationView.invalidate()
            navigationView.setNavigationItemSelectedListener(this);
        }.addOnFailureListener {
            StyleableToast.makeText(applicationContext,"Image failed to load",R.style.exampleToast).show()
        }
    }

    lateinit var nameChange:String
    lateinit var emailChange:String
    private  fun getDataToNavigation() {
        reference.child(userId).addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                var userProfile: DataLogin = snapshot.getValue(DataLogin::class.java)!!
                nameChange = snapshot.child("userName").getValue().toString()
                emailChange = snapshot.child("email").getValue().toString()
                progressBarNavigation.visibility = View.VISIBLE
                if (userProfile != null) {
                    navigationView.invalidate()
                    navigationView.navigation_username.setText(userProfile.userName)
                    navigationView. navigation_email.setText(userProfile.email)
                    navigationView.invalidate()
                    progressBarNavigation.visibility = View.GONE
                }

            }
            override fun onCancelled(error: DatabaseError) {

            }
        })


            }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, drawerLayout)
    }

    private fun setupDrawerLayout() {
        navigationView.setupWithNavController(navController)
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.searchFragment-> findNavController(R.id.sub_fragment).navigate(R.id.action_homeFragment_to_searchFragment)
            R.id.accountFragment-> findNavController(R.id.sub_fragment).navigate(R.id.action_homeFragment_to_accountFragment)
            R.id.loginActivity->{
                FirebaseAuth.getInstance().signOut()
                findNavController(R.id.sub_fragment).navigate(R.id.action_homeFragment_to_loginActivity)



            }
        }

//        if (item.title=="search"){
//            findNavController(R.id.sub_fragment).navigate(R.id.action_homeFragment_to_searchFragment)
//
//        }
//        if (item.title=="account"){
//            findNavController(R.id.sub_fragment).navigate(R.id.action_homeFragment_to_accountFragment)
//        }
        Log.e("aaaa","${item.itemId}")
        Toast.makeText(this,item.title,Toast.LENGTH_SHORT).show()
         drawerLayout.closeDrawer(Gravity.LEFT)
        return  true
    }
    override fun onBackPressed() {
        if(drawerLayout.isDrawerOpen(Gravity.LEFT)){
            drawerLayout.closeDrawer(Gravity.LEFT)
        }
        else{
            super.onBackPressed()
        }
    }

    /**
     * Called when an item in the navigation menu is selected.
     *
     * @param item The selected item
     * @return true to display the item as the selected item
     */



}