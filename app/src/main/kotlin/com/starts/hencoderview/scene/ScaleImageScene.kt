package com.starts.hencoderview.scene

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bytedance.scene.ui.template.AppCompatScene
import com.starts.hencoderview.R
import com.starts.hencoderview.util.ARGUMENTS_STRING_TITLE

/**

 *文件描述：.
 *作者：Created by Administrator on 2020/5/20.
 *版本号：1.0

 */
class ScaleImageScene :AppCompatScene() {
    override fun onCreateContentView(
        inflater: LayoutInflater,
        container: ViewGroup,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.scene_scale_image_view,null)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setTitle(requireArguments().getString(ARGUMENTS_STRING_TITLE))

    }

}