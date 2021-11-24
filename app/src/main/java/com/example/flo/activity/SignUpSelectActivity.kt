package com.example.flo.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivitySignUpSelectBinding
import java.util.zip.Inflater

class SignUpSelectActivity : AppCompatActivity() {

    lateinit var binding : ActivitySignUpSelectBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpSelectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signUpEmailLayout.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }
}