package com.example.mvvmcleantemplate.ui.fragments.login

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.mvvmcleantemplate.domain.model.ForgetPasswordRequest
import com.example.mvvmcleantemplate.domain.model.LoginRequest
import com.example.mvvmcleantemplate.domain.model.LoginResponse
import com.example.mvvmcleantemplate.domain.repository.AuthRepository
import com.example.mvvmcleantemplate.ui.base.BaseViewModel
import com.example.mvvmcleantemplate.utils.ResultWrapper
import com.example.mvvmcleantemplate.utils.SingleLiveEvent
import kotlinx.coroutines.launch

class LoginViewModel(mContext: Context) : BaseViewModel(mContext) {
    private lateinit var authRepo: AuthRepository

    private val _loader = SingleLiveEvent<ResultWrapper.Loading>()
    val loader: LiveData<ResultWrapper.Loading> = _loader

    private val _login = SingleLiveEvent<ResultWrapper<LoginResponse>>()
    val login: LiveData<ResultWrapper<LoginResponse>> = _login

    private val _forgetpassword = SingleLiveEvent<ResultWrapper<Any>>()
    val forgetPassword: LiveData<ResultWrapper<Any>> = _forgetpassword

    fun initRepository(authRepo: AuthRepository) {
        this.authRepo = authRepo
    }

    fun loginUser(email: String, password: String) {
        val request = LoginRequest(email, password)
        viewModelScope.launch {
            _loader.value = ResultWrapper.Loading
            val result = authRepo.login(request)
            _login.value = result
        }
    }

    fun forgetPassword(email: String) {
        val request = ForgetPasswordRequest(email, "")
        viewModelScope.launch {
            _loader.value = ResultWrapper.Loading
            val result = authRepo.forgetPassword(request)
            _forgetpassword.value = result
        }
    }

}