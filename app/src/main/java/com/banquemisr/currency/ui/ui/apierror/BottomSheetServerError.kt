package com.banquemisr.currency.ui.ui.apierror

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.banquemisr.currency.databinding.BottomSheetServerErrorBinding
import com.banquemisr.currency.ui.ui.base.BaseBottomSheet
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BottomSheetServerError : BaseBottomSheet<BottomSheetServerErrorBinding>()
{
    override fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?, b: Boolean) = BottomSheetServerErrorBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.isCancelable = true
    }

    override fun onFragmentReady() {

        mBinding.apply {

            btnOk.setOnClickListener {
                closeBottomSheet()
            }
        }
    }
}