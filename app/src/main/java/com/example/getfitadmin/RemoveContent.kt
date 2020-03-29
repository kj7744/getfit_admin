package com.example.getfitadmin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_remove_content.*

class RemoveContent : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_remove_content)
        delet.setOnClickListener {
            startActivity(Intent(applicationContext,FragmentContainer::class.java)
                .apply {
                    putExtra("class","trainer")
                })
        }
            deleequi.setOnClickListener {
            startActivity(Intent(applicationContext,FragmentContainer::class.java)
                .apply {
                    putExtra("class","equip")
                })
        }
        delemer.setOnClickListener {
            startActivity(Intent(applicationContext,FragmentContainer::class.java)
                .apply {
                    putExtra("class","mer")
                })
        }
        delepack.setOnClickListener {
            startActivity(Intent(applicationContext,FragmentContainer::class.java)
                .apply {
                    putExtra("class","pack")
                })
        }
    }

}
