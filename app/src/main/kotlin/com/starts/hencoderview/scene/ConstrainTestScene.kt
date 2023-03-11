package com.starts.hencoderview.scene

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.FrameMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.bytedance.scene.ui.template.AppCompatScene
import com.starts.hencoderview.databinding.SceneConstraintLayoutTestBinding
import timber.log.Timber

class ConstrainTestScene : AppCompatScene() {
    lateinit var binding: SceneConstraintLayoutTestBinding
    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateContentView(p0: LayoutInflater, p1: ViewGroup, p2: Bundle?): View {
        binding = SceneConstraintLayoutTestBinding.inflate(p0)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setTitle("LinearTestScene")
        binding.tvShort.postDelayed({binding.tvShort.text = "我是长文字，我是长文字，我是长文字，我是长文字，我是长文字，我是长文字，我是长文字，我是长文字，我是长文字，我是长文字，我是长文字，我是长文字，我是长文字，我是长文字，我是长文字，我是长文字，我是长文字，"},4000)
    }

}