package com.starts.hencoderview.scene

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bytedance.scene.ui.template.AppCompatScene
import com.starts.hencoderview.databinding.SceneLinearLayoutTestBinding

class LinearTestScene : AppCompatScene() {
    lateinit var binding: SceneLinearLayoutTestBinding
    override fun onCreateContentView(p0: LayoutInflater, p1: ViewGroup, p2: Bundle?): View {
        binding = SceneLinearLayoutTestBinding.inflate(p0)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setTitle("LinearTestScene")
        binding.tvShort.postDelayed({
            binding.tvShort.text = "现在我变成了长文字，现在我变成了长文字，现在我变成了长文字，现在我变成了长文字，" +
                    "现在我变成了长文字现在我变成了长文字，现在我变成了长文字，现在我变成了长文字，现在我变成了长文字"
        }, 4000)
    }
}