package com.banquemisr.currency.ui.extesnion

import android.content.Context
import android.content.res.Resources
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.banquemisr.currency.ui.core.HashMapParams
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONObject
import timber.log.Timber
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun getMostCommonCurrencies(): ArrayList<String>{
    return arrayListOf(
         "EGP", "USD", "EUR", "JPY", "GBP", "CNY", "AUD", "CAD", "CHF", "HKD",
    )
}

fun getCurrentTimeInMilliSeconds() : Long {
    return System.currentTimeMillis()
}

fun Long.toFormattedDate(): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
    return sdf.format(Date(this))
}

fun getPxFromDp(dp: Float, context: Context): Int {
    val r: Resources = context.resources
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.displayMetrics).toInt()
}

fun Editable?.replaceArabicNumbers(): Editable? {
    this?.toString()?.replace("٠", "0")
        ?.replace("١", "1")
        ?.replace("٢", "2")
        ?.replace("٣", "3")
        ?.replace("٤", "4")
        ?.replace("٥", "5")
        ?.replace("٦", "6")
        ?.replace("٧", "7")
        ?.replace("٨", "8")
        ?.replace("٩", "9")
    return this
}

inline fun <reified T : AppCompatActivity> Fragment.castToActivity(
    callback: (T?) -> Unit,
): T? {
    return if (requireActivity() is T) {
        callback(requireActivity() as T)
        requireActivity() as T
    } else {
        callback(null)
        null
    }
}

fun CoroutineScope.delayWithAction(
    delayMillis: Long,
    action: () -> Unit
) {
    launch {
        delay(delayMillis)
        action()
    }
}

fun formatAmount(amount: Double): String {
    return try {
        val numberFormat: NumberFormat = NumberFormat.getNumberInstance(Locale.ENGLISH)
        val formatter: DecimalFormat = numberFormat as DecimalFormat
        formatter.applyPattern("#,###,###.##")
        formatter.format(amount)
    } catch (ex: NumberFormatException) {
        amount.toString()
    }
}

fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }
    })
}

//check if network is connected
fun isNetworkAvailable(context: Context?): Boolean {
    if (context == null) return false
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                    return true
                }
            }
        }
    } else {
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
            return true
        }
    }
    return false
}

fun Any?.showLogMessage(tag: String? = null) = Timber.tag("Banque_Misr $tag").e(this.toString())

fun HashMapParams?.toHashMapParams2(): HashMap<String?, String?>? {
    if (this == null) return null
    val params by lazy<HashMap<String?, String?>?> { HashMap() }
    try {
        JSONObject(Gson().toJson(dataClass())).let {
            if (it.names() != null)
                for (i in 0 until it.names()!!.length()) {
                    params?.set(
                        it.names()!!.getString(i),
                        it[it.names()!!.getString(i)].toString() + ""
                    )
                }
        }
    } catch (e: Exception) {
        e.message.showLogMessage("Hashmap")
        print(e)
    }
    return if (params!!.isEmpty()) null else params
}


fun HashMapParams?.toHashMapParams(): HashMap<String, String?>? {
    if (this == null) return null
    val params by lazy<HashMap<String, String?>> { HashMap() }
    try {
        JSONObject(Gson().toJson(dataClass())).let {
            if (it.names() != null)
                for (i in 0 until it.names()!!.length()) {
                    params[it.names()!!.getString(i)] =
                        it[it.names()!!.getString(i)].toString() + ""
                }
        }
    } catch (e: Exception) {
        e.message.showLogMessage("Hashmap")
        print(e)
    }
    return if (params.isEmpty()) null else params
}