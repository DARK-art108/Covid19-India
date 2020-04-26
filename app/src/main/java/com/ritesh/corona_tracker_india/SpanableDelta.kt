package com.ritesh.corona_tracker_india

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan

class SpanableDelta (text:String,color:String,start:Int) : SpannableString(text){
    init {
        setSpan(

            ForegroundColorSpan(Color.parseColor(color)),
            start,
            text.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }
}