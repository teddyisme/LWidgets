# LWidgets
整理的在工作中使用的小组件库，采用kotlin编码。

#### 1、LpaddingEditedView 
输入时添加电话、银行卡空格的编辑框
```
<lixs.com.lwidgetslib.LPaddingEditedView
       android:id="@+id/paddingview"
       android:layout_width="match_parent"
       android:layout_height="50dp"
       app:LPaddingViewType="bankCode"/>
```
> 属性

attrs | value
---|---
LPaddingViewType | tel \ bankCode 分别表示手机号和银行卡号
LPaddingViewSpacingNumber | 表示隔几个数字添加空格
> 方法

```
getContentString()
获取去掉空格的字符串
```
```
setInputListens()
输入监听
```
> 简单使用

kotlin

```
paddingview.setInputListens({ s: String, s1: String -> Log.d(TAG1, "s$s  s1:$s1") })

btn.setOnClickListener { Log.d(TAG1, paddingview.getContentString()) }
```





