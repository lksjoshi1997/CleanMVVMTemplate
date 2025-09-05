package com.example.mvvmcleantemplate.ui.base

import android.app.Dialog
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.example.mvvmcleantemplate.R
import com.example.mvvmcleantemplate.data.local.AppDatabase
import com.example.mvvmcleantemplate.data.local.PrefManagerImpl
import com.example.mvvmcleantemplate.data.repository.AppDbRepositoryImpl
import com.example.mvvmcleantemplate.domain.repository.AppDbRepository
import com.example.mvvmcleantemplate.domain.repository.PrefManager
import com.example.mvvmcleantemplate.ui.base.BaseViewModel
import com.example.mvvmcleantemplate.ui.factory.AppViewModelFactory
import com.example.mvvmcleantemplate.utils.AppLogger
import com.example.mvvmcleantemplate.utils.ConnectivityObserver
import com.example.mvvmcleantemplate.utils.showProgressDialog

abstract class BaseFragment<VB : ViewBinding, VM : BaseViewModel>(
    private val viewModelClass: Class<VM>
) : Fragment() {
    private lateinit var mContext: Context
    protected var TAG: String = "TAG"

    protected lateinit var binding: VB

    protected lateinit var viewModel: VM
    protected lateinit var log: AppLogger

    private val appDb: AppDbRepository by lazy {
        AppDbRepositoryImpl(AppDatabase.getInstance(mContext))
    }
    protected val prefManager: PrefManager by lazy {
        PrefManagerImpl(mContext)
    }
    private var progressDialog: Dialog? = null

    private var requestCode: Int = 0

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            onPermissionResult(permissions, requestCode)
        }


//    private val noti: VM by viewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.mContext = context;
        this.log = AppLogger(mContext)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = createView(inflater, container, savedInstanceState)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        log.d(TAG, "After view created ${binding.root}")
        initConnectivityObserver()
        val factory = AppViewModelFactory(mContext)
        viewModel = ViewModelProvider(this, factory)[viewModelClass]
        this.startWorking(savedInstanceState)
    }

    private fun initConnectivityObserver() {
        ConnectivityObserver.isConnected.observe(viewLifecycleOwner) { internetConnected ->
            connectionAvailable(internetConnected)
        }
    }

    protected fun showProgressDialog(cancellable: Boolean, animationRes: Int = R.raw.loading_gray) {
        try {
            if (progressDialog != null && progressDialog!!.isShowing) {
                progressDialog?.dismiss()
            }
            progressDialog = mContext.showProgressDialog(cancellable, animationRes)
        } catch (et: Exception) {
            et.printStackTrace()
        }
    }

    protected open fun connectionAvailable(isInternetAvailable: Boolean) {

    }

    protected fun hideProgressDialog() {
        progressDialog?.dismiss()
    }

//    protected fun getViewModel(): VM {
//        return viewModel
//    }

//    protected fun showBottomNavBar(isShow: Boolean) {
//        (mContext as MainActivity).showBottomNavBar(isShow)
//    }

    abstract fun createView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): VB

    override fun onStop() {
        super.onStop()
//        viewLifecycleOwner
//        viewModelStore.clear();
    }

    override fun onDestroyView() {
        super.onDestroyView()
        hideProgressDialog()
    }

    abstract fun startWorking(savedInstanceState: Bundle?)

    protected fun hasPermission(permissions: Array<String>): Boolean {
        for (element in permissions) {
            if (ContextCompat.checkSelfPermission(
                    mContext,
                    element
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }

    protected fun requestPermission(permission: Array<String>, requestCode: Int) {
        this.requestCode = requestCode
        requestPermissionLauncher.launch(permission)
        log.d(TAG, "Requesting permission: $permission with $requestCode")
    }

    protected open fun onPermissionResult(permission: Map<String, Boolean>, requestCode: Int) {

    }
}