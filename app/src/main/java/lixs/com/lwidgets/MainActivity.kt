package lixs.com.lwidgets

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val TAG1 = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        paddingview.setInputListens({ s: String, s1: String -> Log.d(TAG1, "s$s  s1:$s1") })
        btn.setOnClickListener { Log.d(TAG1, paddingview.getContentString()) }
    }
}
