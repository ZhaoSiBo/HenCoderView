package com.starts.hencoderview

import com.bytedance.scene.Scene
import com.bytedance.scene.ui.SceneActivity
import com.starts.hencoderview.scene.MainScene

class MainActivity : SceneActivity() {

    override fun supportRestore(): Boolean {
        return false
    }

    override fun getHomeSceneClass(): Class<out Scene> {
        return MainScene::class.java
    }
}
