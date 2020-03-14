package com.example.getfitadmin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_admin_homepage.*
import kotlinx.android.synthetic.main.activity_fitness_m_items.*
import kotlin.system.measureNanoTime

class adminHomepage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_homepage)
        train.setOnClickListener {
            startActivity(Intent(this, trainer::class.java))
        }
        equip.setOnClickListener {
            startActivity(Intent(this, equipment::class.java))
        }
        fitnessM.setOnClickListener {
            startActivity(Intent(this, fitnessM_items::class.java))

        }
        pack.setOnClickListener {
            startActivity((Intent(this, packages::class.java)))

        }

        notif.setOnClickListener {
            startActivity(Intent(this,sendNotfication::class.java))
        }



    }
}
