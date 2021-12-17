package org.wit.itfs.activities

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import org.wit.itfs.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.progressBar2.visibility = View.INVISIBLE

        binding.loginbutton.setOnClickListener {

            val email = binding.editTextTextEmailAddress.text.toString().trim()
            val password = binding.editTextTextPassword.text.toString().trim()

            binding.progressBar2.visibility = View.VISIBLE

            if (email != "" && password != "") {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            binding.progressBar2.visibility = View.INVISIBLE
                            finish()
                        } else {
                            binding.progressBar2.visibility = View.INVISIBLE
                            Toast.makeText(
                                this,
                                task.exception!!.message.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            } else {
                binding.progressBar2.visibility = View.INVISIBLE
                Toast.makeText(
                    this,
                    "Please fill in all fields!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.textView5.setOnClickListener() {
            finish()
        }
    }
}