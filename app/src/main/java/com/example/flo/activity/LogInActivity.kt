package com.example.flo.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.*
import com.example.flo.data.SongDB
import com.example.flo.data.User
import com.example.flo.databinding.ActivityLogInBinding

class LogInActivity : AppCompatActivity(), LogInView {

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
            login()
        }

        // 닫기버튼
        binding.logInCloseBtn.setOnClickListener {
            finish()
        }
    }
    
//    private fun login(userDB : SongDB){
//        if(binding.logInIdEt.text.toString().isEmpty() || binding.logInEmailEt.text.toString().isEmpty()){
//            Toast.makeText(this, "아이디를 입력해 주세요.", Toast.LENGTH_SHORT).show()
//            return
//        }
//        if(binding.logInPasswordEt.text.toString().isEmpty()){
//            Toast.makeText(this, "비밀번호를 입력해 주세요.", Toast.LENGTH_SHORT).show()
//            return
//        }
//
//        val email = binding.logInIdEt.text.toString() + '@' + binding.logInEmailEt.text.toString()
//        val password = binding.logInPasswordEt.text.toString()
//        val user = userDB.UserDao().getUser(email)
//        Log.d("jwt", user.toString())
//
//        user?.let{
//            if(user.email == email && user.password == password){
//                // jwt 저장
//                saveJwt(user.id)
//                Log.d("jwt", user.id.toString())
//                finish()
//            } else {
//                Toast.makeText(this, "아이디와 패스워드가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
//            }
//            return
//        }
//        Toast.makeText(this, "존재하지 않는 아이디입니다.", Toast.LENGTH_SHORT).show()
//        return
//    }

    private fun login(){
        if(binding.logInIdEt.text.toString().isEmpty() || binding.logInEmailEt.text.toString().isEmpty()){
            Toast.makeText(this, "아이디를 입력해 주세요.", Toast.LENGTH_SHORT).show()
            return
        }
        if(binding.logInPasswordEt.text.toString().isEmpty()){
            Toast.makeText(this, "비밀번호를 입력해 주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        val authService = AuthService()
        authService.setLogInView(this)

        authService.logIn(getUser())
    }

    private fun getUser() : User{
        val email = binding.logInIdEt.text.toString() + '@' + binding.logInEmailEt.text.toString()
        val password = binding.logInPasswordEt.text.toString()

        return User(email, password, "")
    }

    private fun startMainActivity(){
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        startActivity(intent)
    }

    override fun onLogInLoading() {
        binding.logInPb.visibility = View.VISIBLE
    }

    override fun onLogInSuccess(auth : Auth) {
        binding.logInPb.visibility = View.GONE

        saveJwt(this, auth.jwt)
        saveUserIdx(this, auth.userIdx)

        startMainActivity()
    }

    override fun onLogInFailure(code: Int, message: String) {
        binding.logInPb.visibility = View.GONE

        when(code){
            2015, 2019, 3014 -> {
                binding.logInErrorTv.visibility = View.VISIBLE
                binding.logInErrorTv.text = message
            }
        }
    }
}