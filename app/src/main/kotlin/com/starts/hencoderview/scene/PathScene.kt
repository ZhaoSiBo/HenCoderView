package com.starts.hencoderview.scene

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import com.bytedance.scene.ui.template.AppCompatScene
import com.starts.hencoderview.databinding.ScenePathBinding

/**

 *文件描述：.
 *作者：Created by LostStars on 2020/11/26.
 *版本号：1.0

 */
class PathScene : AppCompatScene() {
    lateinit var binding: ScenePathBinding

    override fun onCreateContentView(
        inflater: LayoutInflater,
        container: ViewGroup,
        savedInstanceState: Bundle?
    ): View? {
        binding = ScenePathBinding.inflate(inflater)
        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setTitle("PathView")
        binding.seekBar1.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                binding.pathView.startPercentage = seekBar.progress
            }

        })

        binding.seekBar2.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                binding.pathView.endPercentage = seekBar.progress
            }

        })

    }

}