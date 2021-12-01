package com.example.flo.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.AuthResponse
import com.example.flo.AuthRetrofitInterface
import com.example.flo.AuthService
import com.example.flo.SignUpView
import com.example.flo.data.SongDB
import com.example.flo.data.User
import com.example.flo.databinding.ActivitySignUpBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SignUpActivity : AppCompatActivity(), SignUpView {

    lateinit var binding : ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userDB = SongDB.getInstance(this)!!

        binding.signUpCompleteTv.setOnClickListener {
            signUp()
        }
    }

    private fun getUser() : User{
        val email : String = binding.signUpIdEt.text.toString() + "@" + binding.signUpEmailEt.text.toString()
        val password : String = binding.signUpPasswordEt.text.toString()
        val name : String = binding.signUpUserNameEt.text.toString()

        return User(email, password, name)
    }

    private fun signUp(){
        if(binding.signUpIdEt.text.isEmpty() || binding.signUpEmailEt.text.isEmpty()){
            binding.signUpWarningEmailOverlappedTv.visibility = View.GONE
            binding.signUpWarningEmailCheckTv.visibility = View.VISIBLE
            return
        }
        if(binding.signUpPasswordEt.text.isEmpty() || binding.signUpPasswordEt.text.toString() != binding.signUpPasswordCheckEt.text.toString()){
            Toast.makeText(this, "비밀번호 입력란을 확인해주세요", Toast.LENGTH_SHORT).show()
            return
        }

        if(binding.signUpUserNameEt.text.isEmpty()){
            Toast.makeText(this, "사용하실 닉네임을 입력해 주세요.", Toast.LENGTH_SHORT).show()
        }

        val authService = AuthService()
        authService.setSignUpView(this)

        authService.signUp(getUser())

        Log.d("SIGNUP", "HELLO")
    }

    override fun onSignUpLoading() {
        binding.signUpLoadingPb.visibility = View.VISIBLE
    }

    override fun onSignUpSuccess() {
        binding.signUpLoadingPb.visibility = View.GONE
        finish()
    }

    override fun onSignUpFailure(code: Int, message: String) {
        binding.signUpLoadingPb.visibility = View.GONE

        binding.signUpWarningEmailCheckTv.visibility = View.VISIBLE
        binding.signUpWarningEmailCheckTv.text = message
    }
}