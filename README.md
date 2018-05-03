# LWidgets
整理的在工作中使用的小组件库，采用kotlin编码。

### 使用
> implementation 'lixs.com.lwidgetslib:lwidgetslib:1.0.3'

#### 1、LEditedView
- 输入时添加电话、银行卡空格
- 可设置空格格式
- 清除按钮的设置
```
<lixs.com.lwidgetslib.LEditedView
        android:id="@+id/paddingview"
        android:layout_width="match_parent"
        android:layout_height="50dp"
        android:maxLength="11"
        android:inputType="number"
        app:LPaddingViewType="TEL"
        app:clearButtonDrawable="@mipmap/ico_circle_close"
        app:clearButtonImageScale="1"
        app:clearButtonRightPadding="12dp"
        app:clearButtonShowType="INPUTTING" />
```
> 属性

attrs | value | description
---|---|---
LPaddingViewType | 输入类型 | TEL:移动电话格式  BANKCODE:银行卡号格式
LPaddingViewSpacingNumber | 添加空格 | 表示隔几个数字添加空格
clearButtonDrawable | 清除按钮 | 清除按钮图片
clearButtonImageScale | 清除按钮缩放比例| 缩放比例float值
clearButtonShowType | 清除按钮显示方式 | NEVER \ ALWAYS \   INPUTTING
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

> 简单使用

kotlin

```
paddingview.setInputListens({ s: String, s1: String -> Log.d(TAG1, "s$s  s1:$s1") })

btn.setOnClickListener { Log.d(TAG1, paddingview.getContentString()) }

paddingview.setOnClearListens({Log.d(TAG1, "clear")})
```





