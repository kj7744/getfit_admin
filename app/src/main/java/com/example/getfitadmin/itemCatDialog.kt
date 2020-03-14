package com.example.getfitadmin

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_equipment.*
import kotlinx.android.synthetic.main.activity_equipment.progcircleequip
import kotlinx.android.synthetic.main.activity_trainer.*
import kotlinx.android.synthetic.main.addcatitem.*
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.util.*

class itemCatDialog: DialogFragment() {
    var image_selected_code: Int = 101
    var imagesrc = ""
    var inputStream: InputStream? =null
    lateinit var db: DatabaseReference
    lateinit var st: StorageReference
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view=inflater.inflate(R.layout.addcatitem,container,false)
        db= FirebaseDatabase.getInstance().reference
        st= FirebaseStorage.getInstance().reference
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db= FirebaseDatabase.getInstance().reference
        st= FirebaseStorage.getInstance().reference
        var edit=view.findViewById(R.id.itemcatedit) as Button
        var upload=view.findViewById(R.id.uploaditemcat) as Button
        edit.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(intent, image_selected_code)

        }
        upload.setOnClickListener {
            validateMenuItem()
        }

    }
    private fun validateMenuItem() {
        var name=itemcatname.text.toString().trim()

        if(name.isNotEmpty() ){
            if(inputStream!=null){
                var a= AlertDialog.Builder(context)
                    .setTitle("Add Equipment")
                    .setMessage(" Are you sure you want to add this")
                    .setPositiveButton("Yes"
                    ) { dialog, which ->
                        uploadMenuItem(name)
                        progcirclecat.visibility= View.VISIBLE
                        dialog.dismiss()
                    }
                    .setNegativeButton("No") { dialog, which ->
                        dialog.cancel()
                    }
                    .create()
                    .show()
            }
            else{
                Toast.makeText(context,"Please select equipment picture", Toast.LENGTH_SHORT).show()

            }
        }
        else{
            Toast.makeText(context,"Please enter valid details ", Toast.LENGTH_SHORT).show()
        }
    }

    private fun uploadMenuItem(name: String) {
        if(inputStream!=null){
            //compress the image
//                var bitmap=BitmapFactory.decodeStream(inputStream)
            var bitmap=(itemcatimg.drawable as BitmapDrawable).bitmap
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()
            var stref=st.child("fitnessM").child("${name}.jpeg")
            stref.putBytes(data)
                .addOnSuccessListener{
                    stref.downloadUrl.addOnSuccessListener {
                        Log.d("url",it.toString())
                        var hashMap= hashMapOf(
                            "url" to it.toString(),
                            "name" to name
                        )
                        db.child("Admin")
                            .child("fitnessM")
                            .child(name)
                            .setValue(hashMap)
                            .addOnSuccessListener {
                                Toast.makeText(context,"Equipment added successfully",
                                    Toast.LENGTH_SHORT).show()
                                progcirclecat.visibility=View.GONE
                                dismiss()
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
                    inputStream = context?.contentResolver?.openInputStream(data?.data!!)!!
                    var bit = BitmapFactory.decodeStream(inputStream)
                    itemcatimg.setImageBitmap(bit)
                } catch (e: Exception) {
                    Log.d("exe", e.toString())
                }

            }
        }
    }
}