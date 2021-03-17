package com.starts.hencoderview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bytedance.scene.ui.template.AppCompatScene
import com.starts.hencoderview.databinding.SceneEllipsizedTextBinding
import com.starts.hencoderview.databinding.SceneRelateBinding

/**

 *文件描述：.
 *作者：Created by zhaosibo on 2021/3/16.
 *版本号：1.0

 */
class RelateScene: AppCompatScene()  {
    lateinit var binding: SceneRelateBinding
    override fun onCreateContentView(
        inflater: LayoutInflater,
        container: ViewGroup,
        savedInstanceState: Bundle?
    ): View? {
        binding = SceneRelateBinding.inflate(inflater)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.tv2.visibility = View.GONE
    }

}