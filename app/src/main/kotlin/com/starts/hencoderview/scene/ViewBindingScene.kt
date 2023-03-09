package com.starts.hencoderview.scene

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.bytedance.scene.ui.template.AppCompatScene

class ViewBindingScene<T:ViewBinding> :AppCompatScene() {
    lateinit var binding:T
    override fun onCreateContentView(p0: LayoutInflater, p1: ViewGroup, p2: Bundle?): View {
        return binding.root
    }
}