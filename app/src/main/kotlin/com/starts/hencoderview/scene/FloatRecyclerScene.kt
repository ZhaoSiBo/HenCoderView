package com.starts.hencoderview.scene

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bytedance.scene.ktx.requireFragmentActivity
import com.bytedance.scene.ui.template.AppCompatScene
import com.starts.hencoderview.R
import com.starts.hencoderview.container.MovieDetailContainer
import com.starts.hencoderview.ui.ListFragment
import com.starts.hencoderview.view.ColoredTextView

/**

 *文件描述：.
 *作者：Created by lorizhao on 2022/3/1.
 *版本号：1.0

 */
class FloatRecyclerScene : AppCompatScene() {
    lateinit var binding: MovieDetailContainer
    override fun onCreateContentView(
        inflater: LayoutInflater,
        container: ViewGroup,
        savedInstanceState: Bundle?
    ): View {
        binding = MovieDetailContainer(context = container.context)
        return binding
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setToolbarVisible(false)
        val data = arrayListOf("第一个","2000","346","50000","2000","346","50000","2000","346","50000","2000","346","50000","2000","346","50000","2000","346","50000","最后3个","最后2个","最后一个")
        val adapter = InnerAdapter(data)
        binding.topRecyclerView .adapter = adapter
        binding.topRecyclerView.layoutManager = LinearLayoutManager(requireSceneContext())
//        val titles = arrayListOf("影评","讨论")
        val fragments = arrayListOf(ListFragment())
        val viewPagerAdapter = object :FragmentStateAdapter(requireFragmentActivity()){
            override fun getItemCount(): Int {
                return fragments.size
            }

            override fun createFragment(position: Int): Fragment {
                return fragments[position]
            }
        }
        binding.bottomSheetLayout.bottomTabLayout.isGone = true
        binding.bottomSheetLayout.bottomViewPager.adapter = viewPagerAdapter
//        TabLayoutMediator(binding.bottomSheetLayout.bottomTabLayout,binding.bottomSheetLayout.bottomViewPager){tab,positon->
//            tab.text = titles[positon]
//        }.attach()
        setStatusBarVisible(false)
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