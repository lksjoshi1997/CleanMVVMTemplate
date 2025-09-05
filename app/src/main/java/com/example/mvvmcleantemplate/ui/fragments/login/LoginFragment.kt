package com.example.mvvmcleantemplate.ui.fragments.login

/**
 *
 * Created by Laxmi Kant Joshi on 05/09/2025
 *
 * */

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.eva.checkin.data.remote.RetrofitClient
import com.example.mvvmcleantemplate.R
import com.example.mvvmcleantemplate.data.repository.AuthRepositoryImpl
import com.example.mvvmcleantemplate.databinding.FragmentLoginBinding
import com.example.mvvmcleantemplate.domain.model.LoginResponse
import com.example.mvvmcleantemplate.ui.base.BaseFragment
import com.example.mvvmcleantemplate.ui.dialog.ForgotPasswordDialog
import com.example.mvvmcleantemplate.utils.ResultWrapper
import com.example.mvvmcleantemplate.utils.ToastType
import com.example.mvvmcleantemplate.utils.handleFailure
import com.example.mvvmcleantemplate.utils.observe
import com.example.mvvmcleantemplate.utils.showToast

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment :
    BaseFragment<FragmentLoginBinding, LoginViewModel>(LoginViewModel::class.java) {

    private lateinit var mContext: Context
    private var emailAddress: String = ""

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.mContext = context
        this.TAG = "LoginFragment"
    }

    override fun createView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): FragmentLoginBinding {
        return FragmentLoginBinding.inflate(inflater, container, false)
    }

    override fun startWorking(savedInstanceState: Bundle?) {
        log.d(TAG, "Startworking -: initialise view")
        this.init()
        this.initListener()
    }

    private fun init() {
        log.d(TAG, "init AuthRepositoryImpl & Observer")
        this.setTheme()
        this.initRepo()
        this.initObserver()
    }

    private fun setTheme() {
    }

    private fun initRepo() {
        val repository = AuthRepositoryImpl(RetrofitClient.apiService)
        viewModel.initRepository(repository)
    }

    private fun initObserver() {
        viewModel.apply {
            observe(loader) { showLoader() }
            observe(login) { handleUserLogin(it) }
            observe(forgetPassword) { handleForgetPassword(it) }
        }
    }

    private fun showLoader() {
        showProgressDialog(false)
    }

    private fun handleForgetPassword(result: ResultWrapper<Any>) {
        result?.let {
            when (it) {
                is ResultWrapper.Loading -> {}
                is ResultWrapper.Success -> {
//                    mContext.showToast(R.string.eva_password_link_send_msg, ToastType.SUCCESS)
//                    showVerificationCodeDialog()
                }

                is ResultWrapper.Error -> {
                    handleFailure(it.exception)
                }
            }
        }
        hideProgressDialog()
    }

    private fun handleUserLogin(result: ResultWrapper<LoginResponse>?) {
        result?.let {
            when (it) {
                is ResultWrapper.Loading -> {}
                is ResultWrapper.Success -> {
                    saveUserInfoAndMove(it.data)
                }

                is ResultWrapper.Error -> {
                    handleFailure(it.exception)
                }
            }
        }
        hideProgressDialog()
    }

    private fun saveUserInfoAndMove(data: LoginResponse?) {
        // implement logic for moving next screen.
    }

    private fun initListener() {
        binding.loginBtn.setOnClickListener {
            if (validateLoginField()) {
                callLoginUserApi()
            }
        }

        binding.tvForgotPassword.setOnClickListener {
            showForgotPasswordDialog()
        }
    }

    private fun callLoginUserApi() {
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()
        viewModel.loginUser(email, password)
    }

    private fun validateLoginField(): Boolean {
        if (binding.etEmail.text.isNullOrEmpty()) {
            mContext.showToast(R.string.empty_email_field, ToastType.ERROR)
            return false
        }
        if (binding.etPassword.text.isNullOrEmpty()) {
            mContext.showToast(R.string.empty_password_field, ToastType.ERROR)
            return false
        }
        return true
    }

    private fun showForgotPasswordDialog() {
        log.d(TAG, "forget password clicked")
        val dialog = ForgotPasswordDialog()
        dialog.onDialogConfirmationListener = { status, email ->
            if (status) {
                this.emailAddress = email
                viewModel.forgetPassword(email)
                dialog.dismiss()
            }
        }
        dialog.show(requireActivity().supportFragmentManager, "EvaForgotPasswordDialog")
    }

//    private fun showVerificationCodeDialog() {
//        val dialog = VerifyCodeDialog()
//        val bundle = Bundle()
//        bundle.putString("user_email", emailAddress)
//        dialog.arguments = bundle
//
//        dialog.onVerificationDialogConfirmationListener = { action, text ->
//            if (action == "resend_otp") {
//                viewModel.forgetPassword(text)
//            } else {
//                viewModel.verifyOtp(text)
//            }
//        }
//        dialog.show(requireActivity().supportFragmentManager, "EvaVerifyCodeDialog")
//    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LoginFragment.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoginFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}