package com.starts.hencoderview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.bytedance.scene.ktx.requireFragmentActivity
import com.bytedance.scene.ui.template.AppCompatScene
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.starts.hencoderview.nest.NestScrollContainer
import com.starts.hencoderview.ui.ListFragment

/**

 *文件描述：.
 *作者：Created by lorizhao on 2022/3/24.
 *版本号：1.0

 */
class NestScene : AppCompatScene() {
    lateinit var mRoot:NestScrollContainer
    override fun onCreateContentView(
        inflater: LayoutInflater,
        container: ViewGroup,
        savedInstanceState: Bundle?
    ): View? {
        mRoot = NestScrollContainer(requireSceneContext())
        return mRoot
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbarVisible(false)
        setStatusBarVisible(false)
        val data = arrayListOf(
            "第一个",
            "2000",
            "346",
            "50000",
            "2000",
            "346",
            "50000",
            "2000",
            "346",
            "50000",
            "2000",
            "346",
            "50000",
            "2000",
            "346",
            "50000",
            "倒数第五",
            "倒数第四",
            "倒数第三",
            "倒数第二",
            "倒数第一"
        )
        mRoot.topRecyclerView.adapter = InnerAdapter(data)
        mRoot.topRecyclerView.layoutManager = LinearLayoutManager(requireSceneContext())
        val titles = arrayListOf("歌曲面板", "相似面板")
        val fragments = arrayListOf(ListFragment(), ListFragment())
        val viewpage = mRoot.floatView.viewPager2
        viewpage.adapter = object : FragmentStateAdapter(requireFragmentActivity()) {
            override fun getItemCount(): Int {
                return fragments.size
            }

            override fun createFragment(position: Int): Fragment {
                return fragments[position]
            }
        }
        val tabLayout =  mRoot.floatView.tabLayout
        TabLayoutMediator(tabLayout, viewpage) { tab, positon ->
            tab.text = titles[positon]
        }.attach()

    }
}