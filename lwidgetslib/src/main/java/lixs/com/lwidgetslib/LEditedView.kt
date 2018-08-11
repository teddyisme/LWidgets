package lixs.com.lwidgetslib

import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import android.view.inputmethod.InputConnectionWrapper
import android.widget.EditText


/**
 * @author  XinSheng
 */
open class LEditedView(context: Context?, attrs: AttributeSet?) : EditText(context, attrs) {
    /**
     * 输入的文字
     */
    private var lstr = StringBuilder()
    /**
     * 添加空格格式类型
     */
    private var lType: String? = null
    /**
     * 右边按钮类型
     */
    private var rightBtnType: String? = null
    /**
     * 设置的空格数目
     */
    private var spaceNumber: Int = 1
    /**
     * 按钮距离右边距离
     */
    private var buttonRightPadding: Int = 1
    /**
     * 按钮缩放比例
     */
    private var buttonScale: Float = 1f
    /**
     * 右边按钮资源
     */
    private var mRightButton: Bitmap? = null
    /**
     * 切换按钮默认图片资源
     */
    private var mRightButtonToggleDefault: Bitmap? = null
    /**
     * 切换选中按钮图片资源
     */
    private var mRightButtonToggleChecked: Bitmap? = null
    /**
     * 切换按钮值
     */
    private var toggleValue: Boolean = false

    private var mPaint: Paint = Paint()

    private var isChange = false

    private var beforeLength = 0

    private var mClearBtnDecRect: Rect = Rect()

    private var mWidth: Int = 0
    private var mHeight: Int = 0

    enum class ClearButtonShowType {
        NEVER,//不显示清除按钮
        ALWAYS,//一直显示清除按钮
        INPUTTING//当输入有文字时显示清除按钮
    }

    enum class SpacingType {
        TEL,//移动电话格式 136 1234 1234
        BANKCODE//银行卡号格式 每隔四位添加一个空格
    }

    enum class RightBtnType {
        CLEARED,//清除按钮类型
        TOGGLE,//点击切换模式
        TOGGLE_PASSWORD//点击切换密码\明文
    }

    private var clearButtonShowType: String? = "ALWAYS"

    private lateinit var InputListen: (String, String) -> Unit
    private lateinit var OnRightClickListen: () -> Unit
    private lateinit var OnRightToggledListen: (Boolean) -> Unit

    fun setInputListens(listener: (String, String) -> Unit) {
        this.InputListen = listener
    }

    fun setOnRightListens(listener: () -> Unit) {
        this.OnRightClickListen = listener
    }

    fun setOnRightToggleListens(listener: (Boolean) -> Unit) {
        this.OnRightToggledListen = listener
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
            SpacingType.TEL.name -> {
                if (sblength > 3 && lstr[3] != ' ') {
                    lstr.insert(3, ' ')
                }
                if (sblength > 8 && lstr[8] != ' ') {
                    lstr.insert(8, ' ')
                }
            }
            SpacingType.BANKCODE.name -> {
                for (i in 0..sblength) {
                    if (i > 3 && i % 5 == 0 && lstr[i - 1] != ' ') {
                        lstr.insert(i - 1, ' ')
                    }
                }
            }
            else -> {
                if (spaceNumber > 0) {
                    for (i in 0..sblength) {
                        if (i > this.spaceNumber && i % (spaceNumber + 1) == 0 && lstr[i - 1] != ' ') {
                            lstr.insert(i - 1, ' ')
                        }
                    }
                }
            }
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        when (rightBtnType) {
            RightBtnType.TOGGLE_PASSWORD.name -> { //点击切换密码模式
                drawToggleButton(canvas)
            }
            RightBtnType.CLEARED.name -> {//清除按钮模式
                if (isHaveClearButton()) {
                    canvas?.drawBitmap(mRightButton, null, mClearBtnDecRect, mPaint)
                }
            }
            RightBtnType.TOGGLE.name -> {//点击切换响应模式
                drawToggleButton(canvas)
            }

        }
    }

    private fun drawToggleButton(canvas: Canvas?) {
        if (isHaveClearButton()) {
            if (toggleValue) {
                canvas?.drawBitmap(mRightButtonToggleChecked, null, mClearBtnDecRect, mPaint)
            } else {
                canvas?.drawBitmap(mRightButtonToggleDefault, null, mClearBtnDecRect, mPaint)
            }
        }
    }

    fun getContentString(): String {
        return deletAllSpac()
    }

    fun setSpaceNumber(nm: Int) {
        spaceNumber = nm
    }

    fun setSpaceType(type: String) {
        lType = type
    }
    
    fun isEmail(): Boolean {
        return deletAllSpac().matches(Regex("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+"))
    }

    init {
        this.addTextChangedListener(textWatcher)
        val ta: TypedArray = context!!.obtainStyledAttributes(attrs, R.styleable.LEdiTextView)
        lType = ta.getString(R.styleable.LEdiTextView_LPaddingViewType)
        rightBtnType = ta.getString(R.styleable.LEdiTextView_rightButtonType)
        clearButtonShowType = ta.getString(R.styleable.LEdiTextView_rightButtonShowType)
        spaceNumber = ta.getInt(R.styleable.LEdiTextView_LPaddingViewSpacingNumber, 0)
        buttonRightPadding = ta.getDimension(R.styleable.LEdiTextView_rightButtonRightPadding, -1f).toInt()
        buttonScale = ta.getFloat(R.styleable.LEdiTextView_rightButtonImageScale, 1f)

        val rightButton = ta.getResourceId(R.styleable.LEdiTextView_rightButtonDrawable, android.R.drawable.ic_delete)
        mRightButton = BitmapFactory.decodeResource(resources, rightButton)

        mRightButtonToggleDefault = BitmapFactory.decodeResource(resources,
                ta.getResourceId(R.styleable.LEdiTextView_rightToggleDrawableDefault, android.R.drawable.ic_delete))
        mRightButtonToggleChecked = BitmapFactory.decodeResource(resources,
                ta.getResourceId(R.styleable.LEdiTextView_rightToggleDrawableChecked, android.R.drawable.ic_delete))

        val namespace = "http://schemas.android.com/apk/res/android"
        val maxLength = attrs?.getAttributeIntValue(namespace, "maxLength", -1) ?: -1
        ta.recycle()
        if (maxLength > -1 || lType == SpacingType.TEL.name) {
            filters = arrayOf<InputFilter>(InputFilter.LengthFilter(getMaxLenght(maxLength)))
        }

        OnRightClickListen = {}
        InputListen = { _, _ -> run {} }
        OnRightToggledListen = { _ -> run {} }
        mPaint.isAntiAlias = true
        mPaint.style = Paint.Style.FILL
    }

    private fun getMaxLenght(maxLength: Int): Int {
        var max = maxLength
        when (lType) {
            SpacingType.TEL.name -> {
                max = 13
            }
            SpacingType.BANKCODE.name -> {
                max += maxLength / 4
                if ((maxLength) % 4 == 0)
                    max--
            }
            else -> {
                if (spaceNumber != 0) {
                    max += (maxLength) / spaceNumber
                    if ((maxLength) % spaceNumber == 0)
                        max--
                }
            }
        }
        return max
    }

    private fun isTogglePasswordMode(): Boolean {
        return inputType == (InputType.TYPE_CLASS_NUMBER + InputType.TYPE_NUMBER_VARIATION_PASSWORD)
                || inputType == (InputType.TYPE_CLASS_TEXT + InputType.TYPE_TEXT_VARIATION_PASSWORD)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mWidth = measuredWidth
        mHeight = measuredHeight
        var btnW = 0f
        var btnH = 0f
        if (rightBtnType == RightBtnType.TOGGLE_PASSWORD.name || rightBtnType == RightBtnType.TOGGLE.name) {
            btnW = (mRightButtonToggleDefault?.width ?: 0) * buttonScale
            btnH = (mRightButtonToggleDefault?.height ?: 0) * buttonScale
        } else if (rightBtnType == RightBtnType.CLEARED.name) {
            btnW = (mRightButton?.width ?: 0) * buttonScale
            btnH = (mRightButton?.height ?: 0) * buttonScale
        }
        if (buttonRightPadding == -1 && btnW != 0f) {
            buttonRightPadding = (btnW / 2).toInt()
        }
        mClearBtnDecRect.set((mWidth - btnW).toInt() - buttonRightPadding, ((mHeight - btnH) / 2).toInt(), mWidth - buttonRightPadding, (mHeight - (mHeight - btnH) / 2).toInt())
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if ((rightBtnType == RightBtnType.TOGGLE_PASSWORD.name || rightBtnType == RightBtnType.TOGGLE.name)
                || (isHaveClearButton() && rightBtnType == RightBtnType.CLEARED.name)) {
            when (event?.action) {
                MotionEvent.ACTION_UP -> {
                    if (event.x - mClearBtnDecRect.left >= 0) {
                        when (rightBtnType) {
                            RightBtnType.TOGGLE_PASSWORD.name -> //点击切换密码模式
                                toggleAction(false)
                            RightBtnType.CLEARED.name -> {//清除按钮模式
                                OnRightClickListen()
                                setText("")
                            }
                            RightBtnType.TOGGLE.name -> //点击切换响应模式
                                toggleAction(true)
                        }
                    }
                }
            }
        }
        return super.onTouchEvent(event)
    }

    private fun toggleAction(onlyToggle: Boolean) {
        if (!onlyToggle && isTogglePasswordMode()) {
            transformationMethod = if (toggleValue)
                PasswordTransformationMethod
                        .getInstance()
            else
                HideReturnsTransformationMethod.getInstance()
            setSelection(text.length)
        }
        OnRightToggledListen(toggleValue)
        toggleValue = !toggleValue
    }

    private fun isHaveClearButton(): Boolean {
        return clearButtonShowType == ClearButtonShowType.ALWAYS.name || (clearButtonShowType == ClearButtonShowType.INPUTTING.name && length() > 0)
    }

    override fun onCreateInputConnection(outAttrs: EditorInfo?): InputConnection {
        return if (inputType == InputType.TYPE_TEXT_VARIATION_PASSWORD + InputType.TYPE_CLASS_TEXT) {
            InnerInputConnecttion(super.onCreateInputConnection(outAttrs), false)
        } else {
            super.onCreateInputConnection(outAttrs)
        }
    }

    class InnerInputConnecttion(target: InputConnection?, mutable: Boolean) : InputConnectionWrapper(target, mutable), InputConnection {

        override fun commitText(text: CharSequence?, newCursorPosition: Int): Boolean {
            //过滤掉中文
            if (isChinese(text.toString())) {
                return false
            }
            return super.commitText(text, newCursorPosition)
        }

        private fun isChinese(c: Char): Boolean {
            return c.toInt() in 0x4E00..0x9FA5
        }

        private fun isChinese(str: String?): Boolean {
            if (str == null)
                return false
            for (c in str.toCharArray()) {
                if (isChinese(c))
                    return true
            }
            return false
        }
    }
}
