package lixs.com.lwidgetslib

import android.support.v4.content.ContextCompat
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.TextView

/**
 * @description 一些扩展
 * @author  XinSheng
 * @date 2018/6/7
 */

/**
 * 设置颜色和点击事件
 */
fun TextView.setSpans(colorString: MutableList<String>?
                      , colors: MutableList<Int>? = null
                      , clicksEnable: Boolean? = false
                      , clicksActions: ((Int) -> Unit)? = {}) {

    colorString?.withIndex()?.forEach { (index, value) ->
        val span = SpannableString(value)
        if (!(colors == null || clicksEnable!!)) {
            span.setSpan(ForegroundColorSpan(ContextCompat.getColor(context, colors[index])),
                    0, value.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        }
        if (clicksEnable!!) {
            val clickableSpan = object : ClickableSpan() {
                override fun onClick(p0: View?) {
                    if (clicksActions != null) {
                        clicksActions(index)
                    }
                }

                override fun updateDrawState(ds: TextPaint?) {
                    super.updateDrawState(ds)
                    if (colors != null) {
                        ds?.color = ContextCompat.getColor(context, colors[index])
                    }
                    ds?.isUnderlineText = false
                    ds?.clearShadowLayer()
                }
            }
            span.setSpan(clickableSpan, 0, value.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
            this.movementMethod = LinkMovementMethod.getInstance();
        }
        this.append(span)
    }
}
