package com.hyperether.getgoing_kmp.android.ui.adapter.formatter

import android.annotation.SuppressLint
import com.dinuscxj.progressbar.CircleProgressBar

class TimeProgressFormatterInvisible : CircleProgressBar.ProgressFormatter {
    @SuppressLint("DefaultLocale")
    override fun format(progress: Int, max: Int): CharSequence {
        return ""
    }
}