package com.example.mvvmcleantemplate.data.repository

import com.example.mvvmcleantemplate.data.remote.ApiInterface
import com.example.mvvmcleantemplate.data.remote.safeApiCall
import com.example.mvvmcleantemplate.domain.model.ForgetPasswordRequest
import com.example.mvvmcleantemplate.domain.model.LoginRequest
import com.example.mvvmcleantemplate.domain.model.LoginResponse
import com.example.mvvmcleantemplate.domain.repository.AuthRepository
import com.example.mvvmcleantemplate.utils.ResultWrapper

class AuthRepositoryImpl(private val api: ApiInterface) : AuthRepository {


    override suspend fun login(request: LoginRequest): ResultWrapper<LoginResponse> {
        return safeApiCall { api.loginUser(request) }
    }

    override suspend fun forgetPassword(request: ForgetPasswordRequest): ResultWrapper<Any> {
        return safeApiCall { api.forgetPassword(request) }
    }

//    override suspend fun logout(): LogoutResponse {
//        TODO("Not yet implemented")
//    }

}