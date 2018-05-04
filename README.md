# LWidgets
整理的在工作中使用的小组件库，采用kotlin编码。

### 使用
> implementation 'lixs.com.lwidgetslib:lwidgetslib:1.0.4'

#### 1、LEditedView
- 输入时添加电话、银行卡空格
- 可设置空格格式
- 右边按钮的设置
- 右边按钮模式，有清除按钮模式，切换密码明文模式
```
<lixs.com.lwidgetslib.LEditedView
        android:id="@+id/paddingview"
        android:layout_width="match_parent"
        android:layout_height="50dp"
        android:inputType="numberPassword"
        android:maxLength="11"
        app:LPaddingViewType="TEL"
        app:rightButtonDrawable="@mipmap/ico_circle_close"
        app:rightButtonImageScale="1"
        app:rightButtonRightPadding="12dp"
        app:rightButtonShowType="INPUTTING"
        app:rightButtonType="TOGGLE_PASSWORD"
        app:rightToggleDrawableChecked="@mipmap/icon_eye_on"
        app:rightToggleDrawableDefault="@mipmap/icon_eye_off" />
```
> 属性

attrs | description |  value
---|---|---
LPaddingViewType | 输入添加空格类型 | TEL:移动电话格式  BANKCODE:银行卡号格式
LPaddingViewSpacingNumber | 添加空格 | 表示隔几个数字添加空格
rightButtonShowType | 右侧按钮显示方式 | NEVER \ ALWAYS \   INPUTTING
rightButtonType|右侧按钮类型|CLEARED:清除按钮\TOGGLE_PASSWORD:点击切换密码明文\TOGGLE:仅仅切换响应
rightButtonDrawable|右侧按钮图片资源|仅在清除按钮模式下生效
rightToggleDrawableDefault|右侧按钮切换默认图片资源|仅在切换模式下生效
rightToggleDrawableChecked|右侧按钮切换选中状态图片资源|仅在切换模式下生效
rightButtonRightPadding|按钮距离右边的距离|dp
rightButtonImageScale|右侧图片缩放比例|float

> 方法

```
getContentString()
获取去掉空格的字符串
```
```
setInputListens()
输入监听
```

```
setOnClearListens()
清除监听
```
```
setOnRightToggleListens()
按钮切换监听
```
> 简单使用

kotlin

```
paddingview.setInputListens({ s: String, s1: String -> Log.d(TAG1, "s$s  s1:$s1") })

btn.setOnClickListener { Log.d(TAG1, paddingview.getContentString()) }

paddingview.setOnRightToggleListens { checked: Boolean -> Log.d(TAG1, "s$checked") }

paddingview.setOnClearListens({Log.d(TAG1, "clear")})
```
####  **用kotlin整理完善平时用到的小组件，不定时更新。新建技术讨论QQ群：776344631**




