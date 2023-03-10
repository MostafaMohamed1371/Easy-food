package com.example.lastorderfood.broadcast

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.ConnectivityManager
import android.util.Log
import android.widget.Toast
import com.example.lastorderfood.R
import io.github.muddz.styleabletoast.StyleableToast

/*
class BroadCast_reciver (): BroadcastReceiver() {

    override fun onReceive(context: Context?, p1: Intent?) {
        when(p1?.action){
            Intent.ACTION_AIRPLANE_MODE_CHANGED->{
                if(p1.extras?.getBoolean("state")==true){
                    Toast.makeText(context,"this is true", Toast.LENGTH_LONG).show()
                }else{
                    Toast.makeText(context,"this is false", Toast.LENGTH_LONG).show()
                }
            }
        }
        Log.i("AAAA",p1?.action.toString())

    }
}
*/

class BroadCast_reciver : BroadcastReceiver() {
    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context, intent: Intent) {
        val connMgr = context
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
        val mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
        if (wifi!!.isAvailable || mobile!!.isAvailable) {
//         val t= Toast.makeText(context,"Internet is connect", Toast.LENGTH_LONG)
//            t.view!!.setBackgroundColor(Color.parseColor("#FF9800"))
//            t.show()
            StyleableToast.makeText(context,"Internet is connect",R.style.exampleToast).show()
        }
        if (!wifi!!.isAvailable || !mobile!!.isAvailable){
//        val t=  Toast.makeText(context,"Internet is not connect", Toast.LENGTH_LONG)
//            t.view!!.setBackgroundColor(Color.parseColor("#FF9800"))
//            t.show()
            StyleableToast.makeText(context,"Internet is not connect",R.style.exampleToast).show()
        }
    }
}

