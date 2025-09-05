package com.example.mvvmcleantemplate.ui.base

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvvmcleantemplate.data.local.AppDatabase
import com.example.mvvmcleantemplate.data.repository.AppDbRepositoryImpl
import com.example.mvvmcleantemplate.utils.AppLogger
import com.example.mvvmcleantemplate.utils.SingleLiveEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

open class BaseViewModel(mcontext: Context) : ViewModel() {

    protected val log: AppLogger = AppLogger(mcontext)
    protected var repositoryDb: AppDbRepositoryImpl

    protected val _failure: SingleLiveEvent<Exception> by lazy { SingleLiveEvent() }

    val failure: LiveData<Exception> get() = _failure

    init {
        val userDao = AppDatabase.getInstance(mcontext)
        repositoryDb = AppDbRepositoryImpl(userDao)
    }

    protected fun launchViewModelScope(block: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch(Dispatchers.Default) {
            try {
                block()
            } catch (e: Exception) {
                _failure.postValue(e)
            }
        }
    }

}