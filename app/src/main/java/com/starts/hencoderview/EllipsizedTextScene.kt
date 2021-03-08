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

 *文件描述：.
 *作者：Created by zhaosibo on 2021/2/8.
 *版本号：1.0

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
            "电影主题曲《my heart will go on 》,电影主题曲《my heart will go on 》,电影主题曲《my heart will go on 》,电影主题曲《my heart will go on 》,电影主题曲《my heart will go on 》,电影主题曲《my heart will go on 》,电影主题曲《my heart will go on 》,电影主题曲《my heart will go on 》,电影主题曲《my heart will go on 》,"
        binding.tv1.text = text
        binding.tv1.setKeepTextSize(sp(12))
        binding.tv1.setKeepTextColor( Color.GREEN)
//        binding.tv1.setTextWithKeepText(text , "",true, R.drawable.playing_com_into, -1 , -1 )
        binding.tv1.text = text
    }
}

