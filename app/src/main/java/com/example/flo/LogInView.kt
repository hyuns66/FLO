package com.example.flo

interface LogInView {
    fun onLogInLoading()
    fun onLogInSuccess(auth : Auth)
    fun onLogInFailure(code : Int, message : String)
}