package com.starts.hencoderview.scene

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import com.bytedance.scene.ui.template.AppCompatScene
import com.starts.hencoderview.util.getMaterialColor

/**

 *文件描述：.
 *作者：Created by zhaosibo on 2021/3/24.
 *版本号：1.0

 */
class ViewGroupScene : AppCompatScene() {


    override fun onCreateContentView(
        inflater: LayoutInflater,
        container: ViewGroup,
        savedInstanceState: Bundle?
    ): View {
        val root = View(container.context).apply {
            layoutParams = ViewGroup.LayoutParams(MATCH_PARENT,MATCH_PARENT)
            background = ColorDrawable(getMaterialColor(resources , 1))
        }
        return root
    }



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setStatusBarVisible(true)
        setToolbarVisible(true)
        setTitle("ViewGroupScene")
    }

}