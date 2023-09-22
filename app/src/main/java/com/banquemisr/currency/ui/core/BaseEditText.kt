package com.banquemisr.currency.ui.core

import android.content.Context
import android.text.Editable
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.res.ResourcesCompat
import com.areeb.moviesexplorer.R
import com.banquemisr.currency.ui.extesnion.getPxFromDp
import com.banquemisr.currency.ui.extesnion.replaceArabicNumbers

open class BaseEditText : AppCompatEditText {
    constructor(context: Context): super(context)
    constructor(context: Context,attrs:AttributeSet?): super(context,attrs)
    constructor(context: Context,attrs: AttributeSet?,defStyleAttr:Int): super(context,attrs,defStyleAttr)

    init {
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
        this.setLineSpacing(-10f, 0f)
        minHeight = getPxFromDp(36f, context)
        this.setPaddingRelative(
            getPxFromDp(12f, context),
            getPxFromDp(12f, context),
            getPxFromDp(12f, context),
            getPxFromDp(12f, context)
        )
        textSize = 12f
        background = ResourcesCompat.getDrawable(resources, R.drawable.et_currency_bg, null)
    }

    override fun getText(): Editable? {
        return super.getText().replaceArabicNumbers()
    }

    override fun onTextChanged(text: CharSequence?, start: Int, lengthBefore: Int, lengthAfter: Int) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
    }

    override fun setOnTouchListener(l: OnTouchListener?) {
        background = ResourcesCompat.getDrawable(resources, R.drawable.et_currency_filled_bg, null)
        super.setOnTouchListener(l)
    }

    override fun setError(error: CharSequence?) {
        if(background != null)
            background = ResourcesCompat.getDrawable(resources, R.drawable.et_currency_error_bg, null)
        super.setError(null, null)
    }

    open fun normalBg() {
        if(background != null)
            background = ResourcesCompat.getDrawable(resources, R.drawable.et_currency_bg, null)
    }

}