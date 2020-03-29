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
import kotlinx.android.synthetic.main.activity_trainer.editbtn
import kotlinx.android.synthetic.main.activity_trainer.itemimage
import kotlinx.android.synthetic.main.activity_trainer.progcircle
import kotlinx.android.synthetic.main.activity_trainer.uploaditem
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.util.*

class equipment : AppCompatActivity() {
    var image_selected_code: Int = 101
    var imagesrc = ""
    var inputStream: InputStream? =null
    lateinit var db: DatabaseReference
    lateinit var st: StorageReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_equipment)
        db= FirebaseDatabase.getInstance().reference
        st= FirebaseStorage.getInstance().reference
        editbtnequip.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(intent, image_selected_code)
        }
        uploaditemequip.setOnClickListener {
            validateMenuItem()
        }
    }

    private fun validateMenuItem() {
        var name=enameequip.text.toString().trim()
        var desc=edescequip.text.toString().trim()


        if(name.isNotEmpty() and desc.isNotEmpty()  ){
            if(inputStream!=null){
                var a= AlertDialog.Builder(this)
                    .setTitle("Add Equipment")
                    .setMessage(" Are you sure you want to add this")
                    .setPositiveButton("Yes"
                    ) { dialog, which ->
                        uploadMenuItem(name,desc)
                        progcircleequip.visibility= View.VISIBLE
                        dialog.dismiss()
                    }
                    .setNegativeButton("No") { dialog, which ->
                        dialog.cancel()
                    }
                    .create()
                    .show()
            }
            else{
                Toast.makeText(applicationContext,"Please select equipment picture", Toast.LENGTH_SHORT).show()

            }
        }
        else{
            Toast.makeText(applicationContext,"Please enter valid details ", Toast.LENGTH_SHORT).show()
        }
    }

    private fun uploadMenuItem(name: String, desc: String) {
        if(inputStream!=null){
            //compress the image
//                var bitmap=BitmapFactory.decodeStream(inputStream)
            var bitmap=(itemimageequip.drawable as BitmapDrawable).bitmap
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()
            var stref=st.child("Equipment").child("${name}.jpeg")
            stref.putBytes(data)
                .addOnSuccessListener{
                    stref.downloadUrl.addOnSuccessListener {
                        Log.d("url",it.toString())
                        var hashMap= hashMapOf(
                            "url" to it.toString(),
                            "name" to name,
                            "desc" to desc
                        )
                        db.child("Admin")
                            .child("Equipment")
                            .child(UUID.randomUUID().toString())
                            .setValue(hashMap)
                            .addOnSuccessListener {
                                Toast.makeText(applicationContext,"Equipment added successfully",Toast.LENGTH_SHORT).show()
                                progcircleequip.visibility=View.GONE
                            }
                    }
                }


        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == image_selected_code) {
                try {
                    var uri = data?.data
                    inputStream = applicationContext.contentResolver.openInputStream(data?.data!!)!!
                    var bit = BitmapFactory.decodeStream(inputStream)
                    itemimageequip.setImageBitmap(bit)
                } catch (e: Exception) {
                    Log.d("exe", e.toString())
                }

            }
        }
    }

}
