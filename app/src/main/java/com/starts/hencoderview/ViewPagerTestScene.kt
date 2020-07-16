package com.starts.hencoderview

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bytedance.scene.ktx.fragmentActivity
import com.bytedance.scene.ktx.requireFragmentActivity
import com.bytedance.scene.ui.template.AppCompatScene
import com.starts.hencoderview.databinding.SceneViewpagerTestBinding
import com.starts.hencoderview.ui.AbstractScene
import com.starts.hencoderview.ui.EmptyFragment

/**

 *文件描述：.
 *作者：Created by Administrator on 2020/7/16.
 *版本号：1.0

 */
class ViewPagerTestScene:AppCompatScene() {

    lateinit var binding:SceneViewpagerTestBinding

    override fun onCreateContentView(
        inflater: LayoutInflater,
        container: ViewGroup,
        savedInstanceState: Bundle?
    ): View? {
        binding =SceneViewpagerTestBinding.inflate(inflater)
        return  binding.root
//        return inflater.inflate(R.layout.scene_viewpager_test,null)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setTitle("Viewpager测试")
        val fragments = arrayListOf<Fragment>(EmptyFragment(),EmptyFragment(),EmptyFragment(),EmptyFragment(),EmptyFragment())

        binding.bt1.setOnClickListener {
            val f = requireFragmentActivity().supportFragmentManager.beginTransaction()
            f.add(fragments[0],"")
            f.commitAllowingStateLoss()
        }
        binding.bt2.setOnClickListener {
            val f = requireFragmentActivity().supportFragmentManager.beginTransaction()
            f.add(fragments[1],"")
            f.commitAllowingStateLoss()
        }

        //        binding.viewPager1.adapter = object :FragmentStatePagerAdapter(requireFragmentActivity().supportFragmentManager){
//            override fun getItem(position: Int): Fragment {
//                return fragments[position]
//            }
//
//            override fun getCount(): Int {
//                return fragments.size
//            }
//
//        }
        binding.viewPager2.adapter  = object :FragmentStateAdapter(requireFragmentActivity()){
            override fun getItemCount(): Int {
                return fragments.size
            }

            override fun createFragment(position: Int): Fragment {
                return EmptyFragment()
            }

        }


    }


}