package com.banquemisr.currency.ui.ui.details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.banquemisr.currency.databinding.ItemHistoricalRateDateBinding
import com.banquemisr.currency.databinding.ItemHistoricalRateExchangeRateBinding
import com.banquemisr.currency.ui.ui.base.BaseAdapter
import com.banquemisr.currency.ui.ui.base.BaseDiffCallback

class HistoricalRatesAdapter : BaseAdapter<ViewBinding, Any>(BaseDiffCallback()) {

    // Define the view types
    companion object {
        private const val VIEW_TYPE_DATE = 1
        private const val VIEW_TYPE_CURRENCY_RATE = 2
    }

    // Update getItemViewType to handle both date and currency-rate items
    override fun getItemViewType(position: Int): Int {
        return when (val item = getItem(position)) {
            is String -> VIEW_TYPE_DATE
            else -> VIEW_TYPE_CURRENCY_RATE
        }
    }

    // Update createBinding to inflate the appropriate layout based on view type
    override fun createBinding(parent: ViewGroup, viewType: Int): ViewBinding {
        return when (viewType) {
            VIEW_TYPE_DATE -> ItemHistoricalRateDateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            VIEW_TYPE_CURRENCY_RATE -> ItemHistoricalRateExchangeRateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    // Update the bind function to populate data based on item type
    override fun bind(binding: ViewBinding, position: Int) {
        val item = getItem(position)
        when (binding) {
            is ItemHistoricalRateDateBinding -> {
                if (item is String) {
                    binding.tvDate.text = item
                }
            }
            is ItemHistoricalRateExchangeRateBinding -> {
                if (item is HistoricalRate) {
                    binding.tvCurrencyName.text = item.currencyName
                    binding.tvRateValue.text = item.rate.toString()
                }
            }
        }
    }
}


//class HistoricalRatesAdapter : BaseAdapter<ViewBinding, Any>(BaseDiffCallback()) {
//    companion object {
//        private const val VIEW_TYPE_ONE = 1
//        private const val VIEW_TYPE_TWO = 2
//    }
//
//    override fun getItemViewType(position: Int): Int {
//        return if (position % 2 == 0) VIEW_TYPE_ONE else VIEW_TYPE_TWO
//    }
//
//    override fun createBinding(parent: ViewGroup, viewType: Int): ViewBinding {
//        return when (viewType) {
//            VIEW_TYPE_ONE -> ItemHistoricalRateDateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//            else -> ItemHistoricalRateExchangeRateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        }
//    }
//
//    override fun bind(binding: ViewBinding, position: Int) {
//        when (binding) {
//            is ItemHistoricalRateDateBinding -> {
//                binding.tvDate.text = "Your Text for Item One at position $position"
//            }
//            is ItemHistoricalRateExchangeRateBinding -> {
//                binding.tvCurrencyName.text = "USD"
//                binding.tvRateValue.text = "30"
//            }
//        }
//    }
//}