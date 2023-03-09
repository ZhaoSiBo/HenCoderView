package com.starts.hencoderview.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.starts.hencoderview.InnerAdapter

/**

 *文件描述：.
 *作者：Created by lorizhao on 2022/3/2.
 *版本号：1.0

 */
class ListFragment:Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val  binding =  RecyclerView(requireContext()).apply {
            this.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)
        }
        val data = arrayListOf("我是第一个","12323124","48999584","239863436",
            "12323124","48999584","239863436","12323124","48999584","239863436","12323124",
            "48999584","239863436","12323124","48999584","239863436","12323124",
            "48999584","我是最后一个")
        binding.adapter = InnerAdapter(data)
        binding.layoutManager = LinearLayoutManager(requireContext())
        return binding
    }
}