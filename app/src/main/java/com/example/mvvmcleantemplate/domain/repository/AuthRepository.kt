package com.example.mvvmcleantemplate.domain.repository

import com.example.mvvmcleantemplate.domain.model.ForgetPasswordRequest
import com.example.mvvmcleantemplate.domain.model.LoginRequest
import com.example.mvvmcleantemplate.domain.model.LoginResponse
import com.example.mvvmcleantemplate.utils.ResultWrapper

interface AuthRepository {

    suspend fun login(request: LoginRequest): ResultWrapper<LoginResponse>

    suspend fun forgetPassword(request: ForgetPasswordRequest): ResultWrapper<Any>

//    suspend fun logout(): LogoutResponse

}