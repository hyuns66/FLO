package com.example.flo.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivityLogInBinding

class LogInActivity : AppCompatActivity() {

    lateinit var binding : ActivityLogInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.logInSignUpTv.setOnClickListener {
            val intent = Intent(this, SignUpSelectActivity::class.java)
            startActivity(intent)
        }
    }
}