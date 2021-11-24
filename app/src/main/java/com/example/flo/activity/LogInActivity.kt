package com.example.flo.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.data.SongDB
import com.example.flo.databinding.ActivityLogInBinding

class LogInActivity : AppCompatActivity() {

    lateinit var binding : ActivityLogInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userDB = SongDB.getInstance(this)!!

        // 회원가입 버튼
        binding.logInSignUpTv.setOnClickListener {
            val intent = Intent(this, SignUpSelectActivity::class.java)
            startActivity(intent)
        }

        // 로그인 버튼
        binding.logInLogInTv.setOnClickListener {
            login(userDB)
        }

        // 닫기버튼
        binding.logInCloseBtn.setOnClickListener {
            finish()
        }
    }
    
    private fun login(userDB : SongDB){
        if(binding.logInIdEt.text.toString().isEmpty() || binding.logInEmailEt.text.toString().isEmpty()){
            Toast.makeText(this, "아이디를 입력해 주세요.", Toast.LENGTH_SHORT).show()
            return
        }
        if(binding.logInPasswordEt.text.toString().isEmpty()){
            Toast.makeText(this, "비밀번호를 입력해 주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        val email = binding.logInIdEt.text.toString() + '@' + binding.logInEmailEt.text.toString()
        val password = binding.logInPasswordEt.text.toString()
        val user = userDB.UserDao().getUser(email)
        Log.d("jwt", user.toString())

        user?.let{
            if(user.email == email && user.password == password){
                // jwt 저장
                saveJwt(user.id)
                Log.d("jwt", user.id.toString())
                finish()
            } else {
                Toast.makeText(this, "아이디와 패스워드가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
            }
            return
        }
        Toast.makeText(this, "존재하지 않는 아이디입니다.", Toast.LENGTH_SHORT).show()
        return
    }

    private fun saveJwt(jwt : Int){
        val spf = getSharedPreferences("auth", MODE_PRIVATE)
        val editor = spf.edit()

        editor.putInt("jwt", jwt)
        editor.apply()
    }
}