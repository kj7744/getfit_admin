package com.example.getfitadmin

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_send_notfication.*
import java.security.acl.Permission

class sendNotfication : AppCompatActivity() {
    var reqcode=123
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_notfication)
        check()
        sendsms.setOnClickListener {
            if(check()) {
                startActivity(Intent(this, allUsers::class.java).apply {
                    putExtra("action", "sms")
                })
            }
            else{
                Toast.makeText(applicationContext,"Please grant permission",Toast.LENGTH_SHORT).show()

            }
        }
    }
    fun check():Boolean{
        if(ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.SEND_SMS)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.SEND_SMS,Manifest.permission.READ_SMS),
                reqcode)
        }
        else{
            return true
        }
        return false
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==reqcode){
            if(grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(applicationContext,"Permission granted",Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(applicationContext,"Permission denied",Toast.LENGTH_SHORT).show()

            }
        }
    }
}
