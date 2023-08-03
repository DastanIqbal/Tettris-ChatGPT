package com.dastanapps.tettris.util

import android.content.Context

/**
 *
 * Created by Iqbal Ahmed on 03/08/2023 10:01 PM
 *
 */

fun Int.px(context: Context): Float {
    return context.resources.displayMetrics.density * this
}