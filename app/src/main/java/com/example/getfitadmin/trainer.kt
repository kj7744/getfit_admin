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
import kotlinx.android.synthetic.main.activity_trainer.*
import kotlinx.android.synthetic.main.activity_trainer.editbtn
import kotlinx.android.synthetic.main.activity_trainer.itemimage
import kotlinx.android.synthetic.main.activity_trainer.progcircle
import kotlinx.android.synthetic.main.activity_trainer.uploaditem
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.util.*

class trainer : AppCompatActivity() {
    var image_selected_code: Int = 101
    var imagesrc = ""
    var inputStream: InputStream? =null
    lateinit var db: DatabaseReference
    lateinit var st: StorageReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trainer)
        db= FirebaseDatabase.getInstance().reference
        st= FirebaseStorage.getInstance().reference
        editbtn.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(intent, image_selected_code)
        }
        uploaditem.setOnClickListener {
            validateMenuItem()
        }
    }

    private fun validateMenuItem() {
        var name=tname.text.toString().trim()
        var desc=tdesc.text.toString().trim()
        var exp=texp.text.toString().trim()
        var gen=tgen.text.toString().trim()

        if(name.isNotEmpty() and desc.isNotEmpty() and exp.isNotEmpty() and gen.isNotEmpty() ){
            if(inputStream!=null){
                var a= AlertDialog.Builder(this)
                    .setTitle("Add Trainer")
                    .setMessage(" Are you sure you want to add this")
                    .setPositiveButton("Yes"
                    ) { dialog, which ->
                        uploadMenuItem(name,desc,exp,gen)
                        progcircle.visibility= View.VISIBLE
                        dialog.dismiss()
                    }
                    .setNegativeButton("No") { dialog, which ->
                        dialog.cancel()
                    }
                    .create()
                    .show()
            }
            else{
                Toast.makeText(applicationContext,"Please select trainer picture", Toast.LENGTH_SHORT).show()

            }
        }
        else{
            Toast.makeText(applicationContext,"Please enter valid details ", Toast.LENGTH_SHORT).show()
        }
    }

    private fun uploadMenuItem(name: String, desc: String, exp: String, gen: String) {
        if(inputStream!=null){
            //compress the image
//                var bitmap=BitmapFactory.decodeStream(inputStream)
            var bitmap=(itemimage.drawable as BitmapDrawable).bitmap
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()
            var stref=st.child("Trainer").child("${name}.jpeg")
            stref.putBytes(data)
                .addOnSuccessListener{
                    stref.downloadUrl.addOnSuccessListener {
                        Log.d("url",it.toString())
                        var hashMap= hashMapOf(
                            "url" to it.toString(),
                            "name" to name,
                            "desc" to desc,
                            "exp" to exp,
                            "gen" to gen
                        )
                        db.child("Admin")
                            .child("Trainer")
                            .child(UUID.randomUUID().toString())
                            .setValue(hashMap)
                            .addOnSuccessListener {
                                Toast.makeText(applicationContext,"Trainer added succefully",Toast.LENGTH_SHORT).show()
                                progcircle.visibility=View.GONE
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
                    itemimage.setImageBitmap(bit)
                } catch (e: Exception) {
                    Log.d("exe", e.toString())
                }

            }
        }
    }

}
