package com.example.flo

import android.util.Log
import com.example.flo.data.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AuthService {
    private lateinit var signUpView: SignUpView
    private lateinit var logInView: LogInView

    fun setSignUpView(signUpView : SignUpView){
        this.signUpView = signUpView
    }

    fun setLogInView(logInView: LogInView){
        this.logInView = logInView
    }

    fun signUp(user : User){
        // Retrofit 객체 생성
        val signUpService = getRetrofit().create(AuthRetrofitInterface::class.java)
        signUpView.onSignUpLoading()
        // API 호출
        signUpService.signUp(user).enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                Log.d("SIGNUP/SUCCESS", response.body().toString())
                val resp = response.body()!!
                when (resp.code){
                    1000 -> signUpView.onSignUpSuccess()
                    else -> signUpView.onSignUpFailure(resp.code, resp.message)
                }
            }
            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                Log.d("SIGNUP/FAILURE", t.toString())

                signUpView.onSignUpFailure(400, "네트워크 오류가 발생했습니다.")
            }
        })
    }

    fun logIn(user : User){
        // Retrofit 객체 생성
        val loginService = getRetrofit().create(AuthRetrofitInterface::class.java)
        logInView.onLogInLoading()
        // API 호출
        loginService.login(user).enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                Log.d("SIGNUP/SUCCESS", response.body().toString())
                val resp = response.body()!!
                when (resp.code){
                    1000 -> logInView.onLogInSuccess(resp.result!!)
                    else -> logInView.onLogInFailure(resp.code, resp.message)
                }
            }
            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                Log.d("SIGNUP/FAILURE", t.toString())

                logInView.onLogInFailure(400, "네트워크 오류가 발생했습니다.")
            }
        })
    }

}