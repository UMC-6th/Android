package com.example.flo.Login

import com.example.flo.Auth.Result

interface LoginView {
    fun onLoginSuccess(code : Int, result : Result)
    fun onLoginFailure()
}