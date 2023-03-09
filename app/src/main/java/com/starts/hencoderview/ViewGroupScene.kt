package com.starts.hencoderview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bytedance.scene.ui.template.AppCompatScene
import com.starts.hencoderview.databinding.LayoutViewGroupBinding

/**

 *文件描述：.
 *作者：Created by zhaosibo on 2021/3/24.
 *版本号：1.0

 */
class ViewGroupScene : AppCompatScene() {

    lateinit var binding:LayoutViewGroupBinding

    override fun onCreateContentView(
        inflater: LayoutInflater,
        container: ViewGroup,
        savedInstanceState: Bundle?
    ): View? {
        val root = LayoutViewGroupBinding.inflate(inflater)
        return root.root
    }



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setStatusBarVisible(true)
        setToolbarVisible(true)
        setTitle("SongDetailViewGroup")
    }

}