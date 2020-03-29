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
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_fitness_m_items.*
import kotlinx.android.synthetic.main.activity_trainer.*
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.util.*
import kotlin.collections.ArrayList

class fitnessM_items : AppCompatActivity() {
    lateinit var catlist:ArrayList<String>
    lateinit var db:DatabaseReference
    lateinit var st:StorageReference
    var image_selected_code: Int = 101
    var inputStream: InputStream? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fitness_m_items)
        catlist= ArrayList()
        db=FirebaseDatabase.getInstance().reference
        st=FirebaseStorage.getInstance().reference
        catlist.add("Select category item")
        var catadapter=ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,catlist)
        catadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        fititemselect.adapter=catadapter
            db.child("Admin").child("fitnessM").addValueEventListener(object :ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.d("error",p0.details)
            }

            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    for(ds in p0.children){
                        Log.d("keys",ds.key.toString())
                        catlist.add(ds.key.toString())
                    }
//                    catadapter.addAll(catlist)
                    catadapter.notifyDataSetChanged()
//                    fititemselect.adapter=catadapter

                }
                else{
                    Toast.makeText(applicationContext,"Please first add category item",Toast.LENGTH_SHORT).show()
                }
            }

        })
        additemcat.setOnClickListener {
            var dialog=itemCatDialog()
            var ft=supportFragmentManager.beginTransaction()
            dialog.show(ft,"itemcardialog")
        }
        //call ke baad msg kar
        editbtnfit.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(intent, image_selected_code)
        }
        uploadfititem.setOnClickListener {
            validateMenuItem()
        }
    }
    private fun validateMenuItem() {
        var name=fititemname.text.toString().trim()
        var desc=fititemdesc.text.toString().trim()
        var price=fititemprice.text.toString().trim()
        var itemcat=fititemselect.selectedItem.toString().trim()
        if(itemcat == "Select item category name"){
            itemcat=""
        }
        if(name.isNotEmpty() and desc.isNotEmpty() and price.isNotEmpty() and itemcat.isNotEmpty()){
            if(inputStream!=null){
                var a= AlertDialog.Builder(this)
                    .setTitle("Add Item")
                    .setMessage(" Are you sure you want to add this")
                    .setPositiveButton("Yes"
                    ) { dialog, which ->
                        uploadMenuItem(name,desc,price,itemcat)
                        progcirclefit.visibility= View.VISIBLE
                        dialog.dismiss()
                    }
                    .setNegativeButton("No") { dialog, which ->
                        dialog.cancel()
                    }
                    .create()
                    .show()
            }
            else{
                Toast.makeText(applicationContext,"Please select  picture", Toast.LENGTH_SHORT).show()

            }
        }
        else{
            Toast.makeText(applicationContext,"Please enter valid details ", Toast.LENGTH_SHORT).show()
        }
    }

    private fun uploadMenuItem(name: String, desc: String,price:String,itemcat:String) {
        if(inputStream!=null){
            //compress the image
//                var bitmap=BitmapFactory.decodeStream(inputStream)
            var bitmap=(fititemimg.drawable as BitmapDrawable).bitmap
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()
            var stref=st.child("fitnessM").child(itemcat).child("${name}.jpeg")
            stref.putBytes(data)
                .addOnSuccessListener{
                    stref.downloadUrl.addOnSuccessListener {
                        Log.d("url",it.toString())
                        var hashMap= hashMapOf(
                            "url" to it.toString(),
                            "name" to name,
                            "desc" to desc,
                            "price" to price
                        )
                        db.child("Admin")
                            .child("fitnessM")
                            .child(itemcat)
                            .child("items")
                            .child(UUID.randomUUID().toString())
                            .setValue(hashMap)
                            .addOnSuccessListener {
                                Toast.makeText(applicationContext,"Item added successfully",Toast.LENGTH_SHORT).show()
                                progcirclefit.visibility= View.GONE
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
                    fititemimg.setImageBitmap(bit)
                } catch (e: Exception) {
                    Log.d("exe", e.toString())
                }

            }
        }
    }
}
