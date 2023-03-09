package com.starts.hencoderview

import android.app.Application
import kotlin.properties.Delegates

/**

 *文件描述：.
 *作者：Created by Administrator on 2020/11/10.
 *版本号：1.0

 */
class HenApplication : Application() {

    companion object{
        var instance: HenApplication by Delegates.notNull()
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

}