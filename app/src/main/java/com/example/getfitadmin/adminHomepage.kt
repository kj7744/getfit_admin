package com.example.getfitadmin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.sax.StartElementListener
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_admin_homepage.*
import kotlinx.android.synthetic.main.activity_fitness_m_items.*
import kotlin.system.measureNanoTime

class adminHomepage : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

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
        Queries.setOnClickListener {
            startActivity(Intent(this,User_queries::class.java))
        }
        remove.setOnClickListener {
            startActivity(Intent(applicationContext,RemoveContent::class.java))
        }
        logout.setOnClickListener {
            auth = FirebaseAuth.getInstance()
            auth.signOut()
            startActivity(Intent(this, MainActivity::class.java))
            finish()

        }


    }
}
