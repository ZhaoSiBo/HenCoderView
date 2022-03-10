package com.starts.hencoderview

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.bytedance.scene.ktx.requireFragmentActivity
import com.bytedance.scene.ui.template.AppCompatScene
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.tabs.TabLayoutMediator
import com.starts.hencoderview.databinding.FramgentAlbumDetailBinding
import com.starts.hencoderview.databinding.SceneBottomSheetBehaviorBinding
import com.starts.hencoderview.ui.ListFragment
import com.starts.hencoderview.util.getRandomColor
import com.starts.hencoderview.view.BehavioralScrollListener
import com.starts.hencoderview.view.BehavioralScrollView
import com.starts.hencoderview.view.BottomSheetLayout
import java.util.*

/**

 *文件描述：.
 *作者：Created by lorizhao on 2022/3/1.
 *版本号：1.0

 */
class BottomSheetBehaviorScene : AppCompatScene() {
    lateinit var binding: SceneBottomSheetBehaviorBinding
    override fun onCreateContentView(
        inflater: LayoutInflater,
        container: ViewGroup,
        savedInstanceState: Bundle?
    ): View {
        binding = SceneBottomSheetBehaviorBinding.inflate(inflater)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setToolbarVisible(false)
        val data = arrayListOf("1000","2000","346","50000","2000","346","50000","2000","346","50000","2000","346","50000","2000","346","50000","2000","346","50000","2000","346","50000")
        val adapter = InnerAdapter(data)
        binding.rvInfo .adapter = adapter
        binding.rvInfo.layoutManager = LinearLayoutManager(requireSceneContext())
        val titles = arrayListOf("歌曲面板","相似面板")
        val fragments = arrayListOf(ListFragment(), ListFragment())
        binding.subViewPager .adapter = object :FragmentStateAdapter(requireFragmentActivity()){
            override fun getItemCount(): Int {
                return fragments.size
            }

            override fun createFragment(position: Int): Fragment {
                return fragments[position]
            }
        }
        TabLayoutMediator(binding.subTableLayout,binding.subViewPager){tab,positon->
            tab.text = titles[positon]
        }.attach()

//        binding.linkageScroll.topScrollTarget = { binding.topRecycler }
//        binding.linkageScroll.listeners.add(object: BehavioralScrollListener {
//            override fun onScrollChanged(v: BehavioralScrollView, from: Int, to: Int) {
//                updateFloatState()
//            }
//        })
//
//        binding.bottomSheet.setup(BottomSheetLayout.POSITION_MIN, floatingHeight)
//        updateFloatState()
    }

//    private fun updateFloatState() {
//        if (binding.bottomSheet.indexOfChild(binding.bottomViewPager) >= 0) {
//            if (binding.linkageScroll.scrollY >= floatingHeight) {
//                binding.bottomSheet.visibility = View.GONE
//                binding.bottomSheet.removeView(binding.rvLinkageBottom)
//                binding.bottomSheet.removeAllViews()
//                if (binding.layoutBottom.indexOfChild(binding.bottomViewPager) < 0) {
//                    binding.layoutBottom.addView(binding.bottomTabLayout)
//                    binding.layoutBottom.addView(binding.bottomViewPager)
//                }
//                binding.linkageScroll.bottomScrollTarget = { findCurrentChildRecyclerView() }
//            }
//        } else {
//            if (binding.linkageScroll.scrollY < floatingHeight) {
//                binding.linkageScroll.bottomScrollTarget = null
//                if (binding.layoutBottom.indexOfChild(binding.bottomViewPager) >= 0) {
//                    binding.layoutBottom.removeAllViews()
//                }
//                if (binding.bottomSheet.indexOfChild(binding.bottomViewPager) < 0) {
//                    binding.bottomSheet.addView(binding.bottomTabLayout)
//                    binding.bottomSheet.addView(binding.bottomViewPager)
//                }
//                binding.bottomSheet.visibility = View.VISIBLE
//            }
//        }
//    }


//    private fun findCurrentChildRecyclerView(): RecyclerView? {
//        val layoutManagerFiled = binding.bottomViewPager::class.java.getDeclaredField("mLayoutManager")
//        layoutManagerFiled.isAccessible = true
//        val pagerLayoutManager = layoutManagerFiled.get(binding.bottomViewPager) as LinearLayoutManager
//        val currentChild = pagerLayoutManager.findViewByPosition(binding.bottomViewPager.currentItem)
//
//        return if (currentChild is RecyclerView) {
//            return currentChild
//        } else {
//            null
//        }
//    }


}

class InnerAdapter(private val data:List<String>) : RecyclerView.Adapter<InnerHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerHolder {
        return  InnerHolder(LayoutInflater.from(parent.context).inflate(R.layout.holder_bootom_sheet,parent,false))
    }

    override fun onBindViewHolder(holder: InnerHolder, position: Int) {
        holder.tvText.setBackgroundColor(getRandomColor())
        holder.tvText.text = data[position]
    }

    override fun getItemCount(): Int {
        return data.size
    }

}

class InnerHolder(item:View) : RecyclerView.ViewHolder(item){
    val tvText: TextView = item.findViewById(R.id.tvText)
}