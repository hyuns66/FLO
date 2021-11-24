package com.example.flo.activity

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.data.SongDB
import com.example.flo.data.User
import com.example.flo.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {

    lateinit var binding : ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userDB = SongDB.getInstance(this)!!

        binding.signUpCompleteTv.setOnClickListener {
            signUp(userDB)
        }
    }

    private fun getUser() : User{
        val email : String = binding.logInIdEt.text.toString() + "@" + binding.logInEmailEt.text.toString()
        val password : String = binding.logInPasswordEt.text.toString()

        return User(email, password)
    }

    private fun signUp(userDB : SongDB){
        if(binding.logInIdEt.text.isEmpty() || binding.logInEmailEt.text.isEmpty()){
            Toast.makeText(this, "이메일 형식이 올바르지 않습니다.", Toast.LENGTH_SHORT).show()
            return
        }
        if(binding.logInPasswordEt.text.isEmpty() || binding.logInPasswordEt.text.toString() != binding.logInPasswordCheckEt.text.toString()){
            Toast.makeText(this, "비밀번호 입력란을 확인해주세요", Toast.LENGTH_SHORT).show()
            return
        }

        if(userDB.UserDao().getUser(getUser().email) != null){
            Toast.makeText(this, "이미 존재하는 계정입니다.", Toast.LENGTH_SHORT).show()
            return
        }

        userDB.UserDao().insert(getUser())

        Log.d("가입완료!", userDB.UserDao().getUsers().toString())
    }
}