package com.starts.hencoderview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bytedance.scene.ktx.requireFragmentActivity
import com.bytedance.scene.ui.template.AppCompatScene
import com.google.android.material.tabs.TabLayoutMediator
import com.starts.hencoderview.databinding.SceneBottomSheetBehaviorBinding
import com.starts.hencoderview.ui.ListFragment
import com.starts.hencoderview.util.getRandomColor
import com.starts.hencoderview.view.ColoredTextView

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
        val viewPagerAdapter = object :FragmentStateAdapter(requireFragmentActivity()){
            override fun getItemCount(): Int {
                return fragments.size
            }

            override fun createFragment(position: Int): Fragment {
                return fragments[position]
            }
        }

        binding.subViewPager .adapter = viewPagerAdapter
        TabLayoutMediator(binding.subTableLayout,binding.subViewPager){tab,positon->
            tab.text = titles[positon]
        }.attach()
    }
}

class InnerAdapter(private val data:List<String>) : RecyclerView.Adapter<InnerHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerHolder {
        return  InnerHolder(LayoutInflater.from(parent.context).inflate(R.layout.holder_bootom_sheet,parent,false))
    }

    override fun onBindViewHolder(holder: InnerHolder, position: Int) {
        holder.tvText.text = data[position]
    }

    override fun getItemCount(): Int {
        return data.size
    }


}

class InnerHolder(item:View) : RecyclerView.ViewHolder(item){
    val tvText: ColoredTextView = item.findViewById(R.id.tvText)
}