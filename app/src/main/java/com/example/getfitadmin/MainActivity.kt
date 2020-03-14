package com.example.getfitadmin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth = FirebaseAuth.getInstance()
        login_button.setOnClickListener {
            dbLogin()
        }

    }

    override fun onStart() {
        super.onStart()
        updateUI(auth.currentUser)
    }
    private fun dbLogin() {
        if (!Patterns.EMAIL_ADDRESS.matcher(email1.text.toString()).matches()) {
                email1.error = " Invalid email"
                email1.requestFocus()
                return
            }
            if (pass1.text.toString().isEmpty()) {
                pass1.error = "password cannot be empty"
                pass1.requestFocus()
                return
            }
            auth.signInWithEmailAndPassword(email1.text.toString(), pass1.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        //   Log.d(TAG, "signInWithEmail:success")
                        val user = auth.currentUser
                        updateUI(user)
                        Toast.makeText(
                            baseContext, "Login successfull.",
                            Toast.LENGTH_SHORT
                        ).show()

                    } else {
                        // If sign in fails, display a message to the user.
                        //  Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(
                            baseContext, "Login failed.",
                            Toast.LENGTH_SHORT
                        ).show()
//                        updateUI(null)
                    }
                }
        }

    private fun updateUI(currentUser: FirebaseUser?) {
        if(currentUser!=null){
            startActivity(Intent(this,adminHomepage::class.java))
            finish()
        }/*else{
            Toast.makeText(baseContext,"Login Failed",
                Toast.LENGTH_SHORT).show()
        }*/

    }
}
