package com.example.mvvmcleantemplate.data.remote

import com.example.mvvmcleantemplate.constants.NetworkConfig
import com.example.mvvmcleantemplate.domain.model.ForgetPasswordRequest
import com.example.mvvmcleantemplate.domain.model.LoginRequest
import com.example.mvvmcleantemplate.domain.model.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiInterface {

    @POST(NetworkConfig.API_FORGOT_PASSWORD)
    suspend fun forgetPassword(@Body request: ForgetPasswordRequest): ApiResponse<Any>

    @POST(NetworkConfig.API_AUTHENTICATE_USER)
    suspend fun loginUser(@Body request: LoginRequest): ApiResponse<LoginResponse>

    @POST(NetworkConfig.API_LOGOUT_USER)
    suspend fun logoutUser()

}