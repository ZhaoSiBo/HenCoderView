package com.starts.hencoderview.ui

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_DRAGGING
import com.starts.hencoderview.InnerAdapter
import com.starts.hencoderview.databinding.FragmentListBinding
import java.util.*

/**

 *文件描述：.
 *作者：Created by lorizhao on 2022/3/2.
 *版本号：1.0

 */
class ListFragment:Fragment() {
    lateinit var  binding: FragmentListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListBinding.inflate(inflater)
        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val random = Random()
        val r = random.nextInt(256)
        val g = random.nextInt(256)
        val b = random.nextInt(256)
        binding.root.setBackgroundColor(Color.rgb(r, g, b))
        val data = arrayListOf("我是第一个","12323124","48999584","239863436","12323124","48999584","239863436","12323124","48999584","239863436","12323124","48999584","239863436","12323124","48999584","239863436","12323124","48999584","我是最后一个")
        binding.list.adapter = InnerAdapter(data)
        binding.list.layoutManager = LinearLayoutManager(requireContext())
        var isLoadMore = false
        binding.list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if(!recyclerView.canScrollVertically(dy) && !isLoadMore){
                    isLoadMore = true
                    Toast.makeText(requireContext() , "滑动到底", Toast.LENGTH_SHORT).show()
                    val newData = arrayListOf("加载更多数据","120023","12323124","48999584","239863436")
                    val oldsize = data.size
                    data.addAll(newData)
                    recyclerView.adapter?.notifyItemRangeInserted(data.size - newData.size , data.size)
                }
            }
        })

    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        Log.d("fragmentLife", "onHiddenChanged${hidden}")
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        Log.d("fragmentLife", "setUserVisibleHint${isVisibleToUser}")
    }

    override fun onStart() {
        super.onStart()
        Log.d("fragmentLife", "hashCode:${this.hashCode()}:onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d("fragmentLife", "hashCode:${this.hashCode()}onResume")
    }


}