package com.example.getfitadmin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment

class FragmentContainer : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment_container)
        val classtocall=intent.extras?.get("class")
        var tocall: Any? =null
        when(classtocall)
        {
            "trainer"->{
                tocall=TrainerFragment()
            }
            "equip"->{
                tocall=EquipmentFragment()
            }
            "mer"->{
                tocall=FitnessFragment()
            }
            "pack"->{
                tocall=GymPackagesFragment()
            }
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame, tocall as Fragment)
            .commit()

    }
}
