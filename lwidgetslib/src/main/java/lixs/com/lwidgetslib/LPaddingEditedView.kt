package lixs.com.lwidgetslib

import android.content.Context
import android.content.res.TypedArray
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.widget.EditText

/**
 * @author  XinSheng
 */
class LPaddingEditedView(context: Context?, attrs: AttributeSet?) : EditText(context, attrs) {
    private var lstr = StringBuilder()
    /**
     * tel 手机号
     * bankCode 银行卡号
     * idCard 身份证号
     */
    private var lType: String? = null
    private var spaceNumber: Int? = 1

    private var isChange = false

    private var gIndex = 0

    private var beforeLength = 0

    private lateinit var InputListen: (String, String) -> Unit // 声明mListen是一个函数（单方法接口）,入参String，无返回值

    fun setInputListens(listener: (String, String) -> Unit) {
        this.InputListen = listener
    }

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            if (isChange) {
                insertSpace()
                setText(lstr.toString())
                try {
                    setSelection(lstr.toString().length)
                } catch (e: Exception) {

                }
                isChange = false


                InputListen(lstr.toString(), deletAllSpac())
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            beforeLength = s?.length ?: 0
            if (lstr.isNotEmpty()) {
                lstr.delete(0, lstr.length)
            }
            gIndex = 0
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            lstr.append(s.toString())
            if (beforeLength == s?.length ?: 0 || isChange) {
                isChange = false
                return
            }
            isChange = true
        }
    }

    private fun deletAllSpac(): String {
        val trueString = lstr
        val length = trueString.length

        if (length > 3 && trueString[3] == ' ') {
            trueString.deleteCharAt(3)
        }
        for (i in 0 until trueString.length) {
            if (i < trueString.length && trueString[i] == ' ') {
                trueString.deleteCharAt(i)
            }
        }
        return trueString.toString()
    }

    private fun insertSpace() {
        val sblength = lstr.length
        when (lType) {
            "tel" -> {
                for (i in 0..sblength) {
                    if ((i == 4 || (i > 4 && i % 5 == 0)) && lstr[i - 1] != ' ') {
                        lstr.insert(i - 1, ' ')
                    }
                }
            }
            "bankCode" -> {
                for (i in 0..sblength) {
                    if (i > 3 && i % 5 == 0 && lstr[i - 1] != ' ') {
                        lstr.insert(i - 1, ' ')
                    }
                }
            }
            else -> {
                for (i in 0..sblength) {
                    if (i > this.spaceNumber!! && i % (spaceNumber!! + 1) == 0 && lstr[i - 1] != ' ') {
                        lstr.insert(i - 1, ' ')
                    }
                }
            }
        }

    }

    fun getContentString(): String {
        return deletAllSpac()
    }

    init {
        this.addTextChangedListener(textWatcher)
        val ta: TypedArray = context!!.obtainStyledAttributes(attrs, R.styleable.LPaddingEdiTextView)
        lType = ta.getString(R.styleable.LPaddingEdiTextView_LPaddingViewType)
        spaceNumber = ta.getInt(R.styleable.LPaddingEdiTextView_LPaddingViewSpacingNumber, 1)
        ta.recycle()
    }
}