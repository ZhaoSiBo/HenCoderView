package com.starts.hencoderview

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.text.*
import android.text.style.ImageSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.bytedance.scene.ui.template.AppCompatScene
import com.starts.hencoderview.databinding.SceneEllipsizedTextBinding
import kotlin.math.ceil


/**

 *文件描述：.实现 文字...文字，文字...icon，文字...文字+icon的样式
 *  当设置了isDisplay = true ，在Ellipsize = end 并且 多行的情况下，图片才会显示，并不会直接追加文本末尾
 *  keepText不管在当前TextView是在缩略状态，keepText都会被保留
 *  非缩略状态下，会自动通过SpannableStringBuilder方案实现图文混排
 *
 *  作者：Created by Lorizhao on 2021/3/4.
 *  版本号：1.0
 *
 *  使用时，请使用 setTextWithKeepText方法设置 保留图片和 保留文字
 *  可以单独设置保留文字的 大小，颜色，如果需要修改对齐方式，那就重写在OnDraw（）方法中的绘制方法
 *
 *  在onMeasure()方法中判断文字是否会超出，并设置needOverDraw，触发在onDraw()方法中追加绘制图片和文字
 *  在onMeasure(),onDraw()方法中调用setText()方法后会再次出发onMeasure(),onDraw()，所以要注意结束条件，自测onMeasure()，onDraw()方法的调用次数
 *  作者：Created by lorizhao on 2021/2/8.
 *  版本号：1.0
 */
class EllipsizedTextScene : AppCompatScene() {
    lateinit var binding: SceneEllipsizedTextBinding
    override fun onCreateContentView(
        inflater: LayoutInflater,
        container: ViewGroup,
        savedInstanceState: Bundle?
    ): View? {
        binding = SceneEllipsizedTextBinding.inflate(inflater)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val text =
            "电影主题曲《my heart will go on 》,电影主题曲《my heart will go ,"
        binding.tv1.text = text
        binding.tv1.setKeepTextSize(sp(12))
        binding.tv1.setKeepTextColor( Color.GREEN)
        binding.tv1.setTextWithKeepText(text , "已经播放30%",false, R.drawable.playing_com_into, -1 , -1 )
        binding.tv1.text = text
    }
}

