package com.starts.hencoderview

import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
import android.text.TextPaint
import android.text.TextUtils
import android.text.style.DynamicDrawableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.bytedance.scene.ui.template.AppCompatScene
import com.starts.hencoderview.SpanUtils.ALIGN_BASELINE
import com.starts.hencoderview.databinding.SceneEllipsizedTextBinding
import com.starts.hencoderview.util.QMUIAlignMiddleImageSpan
import com.starts.hencoderview.util.QMUIAlignMiddleImageSpan.ALIGN_BASELINE
import com.starts.hencoderview.util.QMUIAlignMiddleImageSpan.ALIGN_MIDDLE


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
//        val drawable =
//            ContextCompat.getDrawable(requireSceneContext(), R.drawable.playing_com_into)!!
//        drawable.setBounds(0, 0, dp2px(10), dp2px(10))
//
        val text =
            "电影主题曲《my heart will go on 》,电影主题曲《myhe"
//        val span = QMUIAlignMiddleImageSpan(drawable, ALIGN_MIDDLE, -1f)
//
//        val spannableString = SpannableString(text)
//        spannableString.setSpan(
//            span,
//            text.length - 2,
//            text.length,
//            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
//        )
//        binding.tv1.text = spannableString

        measureTextWith(binding.tv1 ,text )
    }

    private fun measureTextWith(textView: TextView, text: String) {
        val textWith = resources.displayMetrics.widthPixels
        val textLength = textWith * 2 - dp2px(10)
        val paint = textView.paint
        paint.textSize = textView.textSize
        val temp = TextUtils.ellipsize(text , paint, textLength * 1f , TextUtils.TruncateAt.END)

        val drawable =
            ContextCompat.getDrawable(requireSceneContext(), R.drawable.playing_com_into)!!
        drawable.setBounds(0, 0, dp2px(10), dp2px(10))
//        val spannableString = SpannableString(temp)
//        spannableString.setSpan(QMUIAlignMiddleImageSpan(drawable, ALIGN_MIDDLE, -1f) , temp.length-2 ,temp.length -1  , SPAN_EXCLUSIVE_EXCLUSIVE)
        textView.text = SpanUtils(requireSceneContext())
            .append(temp)
            .appendImage(drawable , SpanUtils.ALIGN_BASELINE)
            .create()

    }


}

