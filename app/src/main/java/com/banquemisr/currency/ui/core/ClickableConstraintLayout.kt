package com.banquemisr.currency.ui.core

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout

class ClickableConstraintLayout(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }
}
