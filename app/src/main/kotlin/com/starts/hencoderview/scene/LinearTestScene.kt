package com.starts.hencoderview.scene

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.bytedance.scene.ui.template.AppCompatScene
import com.starts.hencoderview.databinding.SceneLinearLayoutTestBinding
import com.starts.hencoderview.util.ARGUMENTS_STRING_TITLE
import timber.log.Timber

class LinearTestScene : AppCompatScene() {
    lateinit var binding: SceneLinearLayoutTestBinding
    @SuppressLint("SetTextI18n")
    override fun onCreateContentView(p0: LayoutInflater, p1: ViewGroup, p2: Bundle?): View {
        binding = SceneLinearLayoutTestBinding.inflate(p0)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setTitle(requireArguments().getString(ARGUMENTS_STRING_TITLE))
        binding.tvShort.postDelayed({binding.tvShort.text = "我是长文字，我是长文字，我是长文字，我是长文字，我是长文字，我是长文字，我是长文字，我是长文字，我是长文字，我是长文字，我是长文字，我是长文字，我是长文字，我是长文字，我是长文字，我是长文字，我是长文字，"},4000)
    }

}