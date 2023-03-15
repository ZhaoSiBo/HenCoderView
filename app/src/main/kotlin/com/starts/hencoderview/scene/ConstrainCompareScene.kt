package com.starts.hencoderview.scene

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import com.bytedance.scene.ui.template.AppCompatScene
import com.starts.hencoderview.databinding.ConstrainCompareLayoutBinding
import com.starts.hencoderview.util.ARGUMENTS_STRING_TITLE
import timber.log.Timber

class ConstrainCompareScene : AppCompatScene() {
    var firstDate = 0L
    var secondDate = 0L
    val preDraw = ViewTreeObserver.OnPreDrawListener {
        secondDate = System.currentTimeMillis()
        val result = secondDate - firstDate
        firstDate = secondDate
        Timber.d("result = $result")
        true
    }
    lateinit var binding: ConstrainCompareLayoutBinding
    override fun onCreateContentView(p0: LayoutInflater, p1: ViewGroup, p2: Bundle?): View {
        firstDate = System.currentTimeMillis()
        binding = ConstrainCompareLayoutBinding.inflate(p0)
        binding.root.viewTreeObserver.addOnPreDrawListener(preDraw)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setTitle(requireArguments().getString(ARGUMENTS_STRING_TITLE))
    }
    override fun onDestroyView() {
        binding.root.viewTreeObserver.removeOnPreDrawListener(preDraw)
        super.onDestroyView()

    }
}