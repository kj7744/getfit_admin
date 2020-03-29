package com.example.getfitadmin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.SmsManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_all_users.*
import java.lang.Exception

class allUsers : AppCompatActivity() {
    lateinit var db:DatabaseReference
    lateinit var alluserslist:ArrayList<mem>
    lateinit var alluser:ArrayList<mem>
    lateinit var adapter: allUserAdapter
    var isall_selected=0
    var action=""
    lateinit var smsManager:SmsManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_users)
        action=intent.extras!!.getString("action")!!
        smsManager= SmsManager.getDefault()
        db=FirebaseDatabase.getInstance().reference
        alluserslist= ArrayList()
        alluser=ArrayList()
        adapter= allUserAdapter(applicationContext,alluserslist)
        allusersrec.layoutManager=LinearLayoutManager(applicationContext)
        allusersrec.adapter=adapter
        db.child("User").addValueEventListener(object:ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                Log.d("exe",p0.details)
            }

            override fun onDataChange(p0: DataSnapshot){
                if(p0.exists() and p0.hasChildren()){
                    for(ds in p0.children) {
                        var m=mem()
                        m.address= ds.child("memdetails").child("address").value.toString()
                        m.bloodgrp= ds.child("memdetails").child("bloodgrp").value.toString()
                        m.dob= ds.child("memdetails").child("dob").value.toString()
                        m.email= ds.child("email").value.toString()
                        m.gen= ds.child("memdetails").child("gen").value.toString()
                        m.name= ds.child("username").value.toString()
                        m.phn= ds.child("memdetails").child("phn").value.toString()
                        m.uid=ds.key.toString()
                        alluserslist.add(m)
                    }
                    adapter.update(alluserslist)
                }
            }
        })
        sendtoall.setOnClickListener {
            if(action=="sms"){
                val alterdialog=AlertDialog.Builder(this)
                alterdialog.setTitle("SMS Message")
                val edittext=EditText(applicationContext)
                edittext.hint="Please enter your message"
                alterdialog.setView(edittext)
                alterdialog.setPositiveButton("Send") { dialog, which ->
                    val msg=edittext.text.toString()
                    val phonno = ArrayList<String>()
                    alluserslist.forEach {
                        if (it.is_selected == 1) {
                            phonno.add(it.phn)
                        }
                    }
                    if (phonno.isNotEmpty()) {
                        phonno.forEach {
                            try {
                                smsManager.sendTextMessage(it, null, msg, null, null)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                        Toast.makeText(applicationContext,"SMS is sent",Toast.LENGTH_SHORT).show()
                    }
                }
                alterdialog.setNegativeButton("Cancel"){d,w->
                    d.cancel()
                }
                alterdialog.show()
            }
            else{
                val alterdialog=AlertDialog.Builder(this)
                alterdialog.setTitle("Notifcation Message")
                val edittext=EditText(applicationContext)
                edittext.hint="Please enter your message"
                alterdialog.setView(edittext)
                alterdialog.setPositiveButton("Send") { dialog, which ->
                    val msg=edittext.text.toString()
                    val user = ArrayList<String>()
                    alluserslist.forEach {
                        if (it.is_selected == 1) {
                            user.add(it.uid)
                        }
                    }
                    if (user.isNotEmpty()) {

                        sendNotif(user,edittext.text.toString())
                    }
                }
                alterdialog.setNegativeButton("Cancel"){d,w->
                    d.cancel()
                }
                alterdialog.show()
                    Log.d("notif","notif")
            }

        }
    }

    private fun sendNotif(
        user: ArrayList<String>,
        msg: String
    ) {
        val h= hashMapOf(
            "message" to msg,
            "Users" to user
        )
        db.child("Admin").child("Notification").setValue(h).addOnSuccessListener {
            Toast.makeText(applicationContext, "SMS is sent", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.usermenu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.addall->{
                if(isall_selected==0){
                if(alluserslist.isNotEmpty()) {
                    alluserslist.forEach {
                        it.is_selected = 1
                    }
                }
                    isall_selected=1
                }
                    else{
                    if(alluserslist.isNotEmpty()) {
                        alluserslist.forEach {
                            it.is_selected = 0
                        }
                    }
                    isall_selected=0
                }

                    adapter.update(alluserslist)

            }
        }

        return true
    }
}
