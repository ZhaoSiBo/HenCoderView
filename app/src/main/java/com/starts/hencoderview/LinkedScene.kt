package com.starts.hencoderview

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.bytedance.scene.ktx.requireFragmentActivity
import com.bytedance.scene.ui.template.AppCompatScene
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.starts.hencoderview.link.NestScrollContainerLayout
import com.starts.hencoderview.ui.ListFragment
import com.starts.hencoderview.view.BOTTOM_SHEET_STATE_EXTENDED
import com.starts.hencoderview.view.DoubanDetailView

/**

 *文件描述：.
 *作者：Created by lorizhao on 2022/3/11.
 *版本号：1.0

 */
class LinkedScene : AppCompatScene() {
    lateinit var binding : NestScrollContainerLayout
    override fun onCreateContentView(
        inflater: LayoutInflater,
        container: ViewGroup,
        savedInstanceState: Bundle?
    ): View {
        binding = NestScrollContainerLayout(requireSceneContext())
        return binding
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitle("LinkedScene")
        setStatusBarVisible(false)
        binding.rvTopInfo.layoutManager = LinearLayoutManager(requireSceneContext())
        val data = arrayListOf("1000","2000","346","50000","2000","346","50000","2000","346","50000","2000","346","50000","2000","346","50000","2000","346","50000","2000","我到底了")
        binding.rvTopInfo.adapter = InnerAdapter(data)
        binding.bottomSheetLayout.setBackgroundColor(Color.WHITE)
//        LayoutInflater.from(binding.bottomLayout.context).inflate(R.layout.view_douban_bottom_content, douban.bottomLayout)
        val titles = arrayListOf("歌曲面板","相似面板")
        val fragments = arrayListOf(ListFragment(), ListFragment())
        binding.bottomSheetLayout.floatViewPager.adapter = object :FragmentStateAdapter(requireFragmentActivity()){
            override fun getItemCount(): Int {
                return fragments.size
            }

            override fun createFragment(position: Int): Fragment {
                return fragments[position]
            }
        }
        TabLayoutMediator(binding.bottomSheetLayout.floatTabLayout,binding.bottomSheetLayout.floatViewPager){ tab, positon->
            tab.text = titles[positon]
        }.attach()

//        val viewPager = binding.bottomLayout.findViewById<ViewPager2>(R.id.view_pager)
//        viewPager.adapter = object :FragmentStateAdapter(requireFragmentActivity()){
//            override fun getItemCount(): Int {
//                return fragments.size
//            }
//
//            override fun createFragment(position: Int): Fragment {
//                return fragments[position]
//            }
//        }
//        val tabLayout = douban.bottomLayout.findViewById<TabLayout>(R.id.tab_layout)
//        TabLayoutMediator(tabLayout,viewPager){ tab, positon->
//            tab.text = titles[positon]
//        }.attach()
//        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
//            override fun onPageSelected(position: Int) {
//                super.onPageSelected(position)
//                if (douban.isBottomViewFloating
//                    && douban.bottomSheetLayout.state != BOTTOM_SHEET_STATE_EXTENDED
//                ) {
//                    douban.bottomSheetLayout.setProcess(1F, true)
//                }
//            }
//        })
//            douban.bottomScrollViewProvider = { findChildScrollView() }
    }

//    private fun findChildScrollView():RecyclerView?{
//        val viewpage2 = douban.bottomLayout.findViewById<ViewPager2>(R.id.view_pager)
//        val layoutManagerFiled = viewpage2::class.java.getDeclaredField("mLayoutManager")
//        layoutManagerFiled.isAccessible = true
//        val pagerLayoutManager = layoutManagerFiled.get(viewpage2) as LinearLayoutManager
//        val currentChild = pagerLayoutManager.findViewByPosition(viewpage2!!.currentItem)
//        return if (currentChild is RecyclerView) {
//            currentChild
//        } else {
//            null
//        }
//    }
}