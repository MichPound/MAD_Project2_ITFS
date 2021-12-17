package org.wit.itfs.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import org.wit.itfs.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.progressBar.visibility = View.INVISIBLE

        binding.loginbutton.setOnClickListener {

            val email = binding.editTextTextEmailAddress.text.toString().trim()
            val password = binding.editTextTextPassword.text.toString().trim()

            binding.progressBar.visibility = View.VISIBLE

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        binding.progressBar.visibility = View.INVISIBLE
                        val launcherIntent = Intent(this, TourSpotListActivity::class.java)
                        startActivityForResult(launcherIntent, 0)
                    } else {
                        binding.progressBar.visibility = View.INVISIBLE
                        Toast.makeText(
                            this,
                            task.exception!!.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }

        binding.textView4.setOnClickListener() {
            val launcherIntent = Intent(this, RegisterActivity::class.java)
            startActivityForResult(launcherIntent, 0)
        }
    }
}