package lixs.com.lwidgetslib

import android.content.Context
import android.util.TypedValue

/**
 * @description
 * @author  XinSheng
 * @date 2018/4/27
 */
fun Context.dp2px(dpVal: Float): Int {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
            dpVal, resources.displayMetrics).toInt()
}

fun Context.sp2px(spVal: Int): Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
            spVal.toFloat(), resources.displayMetrics)
}

