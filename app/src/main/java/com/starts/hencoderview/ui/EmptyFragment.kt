package com.starts.hencoderview.ui

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.starts.hencoderview.R
import com.starts.hencoderview.databinding.FragmentEmptyBinding
import java.util.*
import java.util.logging.Logger

/**

 *文件描述：.
 *作者：Created by Administrator on 2020/7/16.
 *版本号：1.0

 */
class EmptyFragment : Fragment() {

    lateinit var  binding:FragmentEmptyBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        return super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentEmptyBinding.inflate(inflater)
        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val random = Random()
        val r = random.nextInt(256)
        val g = random.nextInt(256)
        val b = random.nextInt(256)
        binding.root.setBackgroundColor(Color.rgb(r, g, b))
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        Log.d("fragmentLife", "onHiddenChanged${hidden}")
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        Log.d("fragmentLife", "setUserVisibleHint${isVisibleToUser}")
    }

    override fun onResume() {
        super.onResume()
        Log.d("fragmentLife", "onResume")
    }


}