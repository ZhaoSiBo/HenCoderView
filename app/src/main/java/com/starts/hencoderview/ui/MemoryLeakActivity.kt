package com.starts.hencoderview.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.starts.hencoderview.R
import kotlin.concurrent.thread

/**

 *文件描述：.
 *作者：Created by Administrator on 2020/11/13.
 *版本号：1.0

 */
class MemoryLeakActivity : AppCompatActivity() {

    lateinit var tvPost: TextView

    //    private val handler = Handler{
//        tvPost.text = "收到消息"
//        false
//    }
    private val handler = Handler{
        tvPost.text = "收到"
        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memory_lead)

        tvPost = findViewById(R.id.tvPost)
        tvPost.setOnClickListener {
            handler.postDelayed({
                Toast.makeText(this,"开始执行" ,Toast.LENGTH_LONG).show()
            },15000)
        }
    }


}