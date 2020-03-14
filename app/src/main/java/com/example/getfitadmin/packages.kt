package com.example.getfitadmin

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_equipment.*
import kotlinx.android.synthetic.main.activity_packages.*
import kotlinx.android.synthetic.main.activity_trainer.editbtn
import kotlinx.android.synthetic.main.activity_trainer.itemimage
import kotlinx.android.synthetic.main.activity_trainer.progcircle
import kotlinx.android.synthetic.main.activity_trainer.uploaditem
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.util.*

class packages : AppCompatActivity() {
    //    var image_selected_code: Int = 101
    var imagesrc = ""
    //    var inputStream: InputStream? =null
    lateinit var db: DatabaseReference
    lateinit var st: StorageReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_packages)
        db = FirebaseDatabase.getInstance().reference
        st = FirebaseStorage.getInstance().reference
        puploaditem.setOnClickListener {
            validateMenuItem()
        }
    }

    private fun validateMenuItem() {
        var name = pname.text.toString().trim()
        var desc = pdesc.text.toString().trim()
        var price = pprice.text.toString().trim()


        if (name.isNotEmpty() and desc.isNotEmpty() and price.isNotEmpty()) {
            var a = AlertDialog.Builder(this)
                .setTitle("Add Package")
                .setMessage(" Are you sure you want to add this")
                .setPositiveButton(
                    "Yes"
                ) { dialog, which ->
                    uploadMenuItem(name, desc,price)
                    progcirclepackage.visibility = View.VISIBLE
                    dialog.dismiss()
                }
                .setNegativeButton("No") { dialog, which ->
                    dialog.cancel()
                }
                .create()
                .show()

        } else {
            Toast.makeText(applicationContext, "Please enter valid details ", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun uploadMenuItem(name: String, desc: String,price:String) {
        var hashMap = hashMapOf(
            "name" to name,
            "desc" to desc,
            "price" to price
        )
        db.child("Admin")
            .child("Package")
            .child(UUID.randomUUID().toString())
            .setValue(hashMap)
            .addOnSuccessListener {
                Toast.makeText(
                    applicationContext,
                    "Package is added successfully",
                    Toast.LENGTH_SHORT
                ).show()
                progcirclepackage.visibility = View.GONE
            }
    }


}


