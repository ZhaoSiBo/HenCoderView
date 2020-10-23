package com.starts.hencoderview

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bytedance.scene.ui.template.AppCompatScene
import com.starts.hencoderview.databinding.SceneParticleScatteringBinding
import com.starts.hencoderview.databinding.SceneViewpagerTestBinding

/**

 *文件描述：.
 *作者：Created by Administrator on 2020/10/23.
 *版本号：1.0

 */
class ParticleScatteringScene: AppCompatScene() {

    lateinit var binding: SceneParticleScatteringBinding

    override fun onCreateContentView(
        inflater: LayoutInflater,
        container: ViewGroup,
        savedInstanceState: Bundle?
    ): View? {
        binding = SceneParticleScatteringBinding.inflate(inflater)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setTitle("ParticleScatteringView")


    }


}