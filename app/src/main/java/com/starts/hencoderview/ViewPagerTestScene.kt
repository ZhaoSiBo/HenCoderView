package com.starts.hencoderview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bytedance.scene.ktx.requireFragmentActivity
import com.bytedance.scene.ui.template.AppCompatScene
import com.starts.hencoderview.databinding.SceneViewpagerTestBinding
import com.starts.hencoderview.ui.EmptyFragment

/**

 *文件描述：.
 *作者：Created by Administrator on 2020/7/16.
 *版本号：1.0

 */
class ViewPagerTestScene : AppCompatScene() {


    var f1: Fragment? = null
    var f2: Fragment? = null

    lateinit var binding: SceneViewpagerTestBinding

    override fun onCreateContentView(
        inflater: LayoutInflater,
        container: ViewGroup,
        savedInstanceState: Bundle?
    ): View? {
        binding = SceneViewpagerTestBinding.inflate(inflater)
        return binding.root
//        return inflater.inflate(R.layout.scene_viewpager_test,null)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setTitle("Viewpager测试")
        val fragments = arrayListOf<Fragment>(EmptyFragment(),EmptyFragment(),EmptyFragment(),EmptyFragment(),EmptyFragment())
        binding.bt1.setOnClickListener {
            val b = requireFragmentActivity().supportFragmentManager.beginTransaction()
            if (f1 == null) {
                f1 = EmptyFragment()
                b.add(binding.viewPager1.id, f1!!)
            } else {
                b.show(f1!!)
                f2?.let {
                    b.hide(it)
                }
            }
            b.commitAllowingStateLoss()
        }
        binding.bt2.setOnClickListener {
            val b = requireFragmentActivity().supportFragmentManager.beginTransaction()
            if (f2 == null) {
                f2 = EmptyFragment()
                b.add(binding.viewPager1.id, f2!!)
            } else {
                b.show(f2!!)
                f1?.let {
                    b.hide(it)
                }

            }
            b.commitAllowingStateLoss()
        }
        binding.viewPager2.offscreenPageLimit = 1
        binding.viewPager2.isUserInputEnabled = false

        binding.viewPager2.adapter = object : FragmentStateAdapter(requireFragmentActivity()) {
            override fun getItemCount(): Int {
                return fragments.size
            }

            override fun createFragment(position: Int): Fragment {
                return EmptyFragment()
            }

        }

        binding.bt3.setOnClickListener {
            binding.viewPager2.currentItem = 0
        }

        binding.bt4.setOnClickListener {
            binding.viewPager2.setCurrentItem(2,false)
        }

    }


}