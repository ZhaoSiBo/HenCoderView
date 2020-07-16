package com.starts.hencoderview.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.bytedance.scene.ui.template.AppCompatScene

/**

 *文件描述：.
 *作者：Created by Administrator on 2020/7/16.
 *版本号：1.0

 */
abstract class AbstractScene<DB:ViewDataBinding>:AppCompatScene() {

    @get:LayoutRes
    protected abstract val resId:Int

    protected lateinit var dataBinding: DB


    override fun onCreateContentView(
        inflater: LayoutInflater,
        container: ViewGroup,
        savedInstanceState: Bundle?
    ): View? {
        val createDataBinding :DB? = DataBindingUtil.findBinding( inflater.inflate(resId,null))
        return if (createDataBinding != null) {
            dataBinding = createDataBinding
            dataBinding.root
        }else{
            inflater.inflate(resId,null)
        }
    }
}