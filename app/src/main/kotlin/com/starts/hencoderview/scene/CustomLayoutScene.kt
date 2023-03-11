package com.starts.hencoderview.scene

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bytedance.scene.ui.template.AppCompatScene
import com.starts.hencoderview.view.TestViewLayout
import com.starts.hencoderview.view.matchParent

class CustomLayoutScene : AppCompatScene() {
    private lateinit var rootView : TestViewLayout
    override fun onCreateContentView(p0: LayoutInflater, p1: ViewGroup, p2: Bundle?): View {
        rootView = TestViewLayout(p1.context).apply {
            layoutParams = ViewGroup.LayoutParams(matchParent, matchParent)
        }
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setTitle("CustomViewGroupScene")
        setToolbarVisible(true)
        rootView.leftTv.postDelayed({rootView.leftTv.text = "我是长文字，我是长文字，我是长文字，我是长文字，我是长文字，我是长文字，我是长文字，我是长文字，我是长文字，我是长文字，我是长文字，我是长文字，我是长文字，我是长文字，我是长文字，我是长文字，我是长文字，"},4000)
    }
}