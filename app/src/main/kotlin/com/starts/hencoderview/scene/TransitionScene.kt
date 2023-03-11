package com.starts.hencoderview.scene

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.transition.Scene
import androidx.transition.TransitionManager
import com.bytedance.scene.ui.template.AppCompatScene
import com.starts.hencoderview.container.CustomLayout
import com.starts.hencoderview.util.ARGUMENTS_STRING_TITLE
import com.starts.hencoderview.util.getScreenWidth
import com.starts.hencoderview.view.TransitionLayout

class TransitionScene : AppCompatScene() {
    private lateinit var rootView : TransitionLayout
    override fun onCreateContentView(
        inflater: LayoutInflater,
        container: ViewGroup,
        savedInstanceState: Bundle?
    ): View {
        rootView = TransitionLayout(container.context)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbarVisible(true)
        setStatusBarVisible(true)
        setTitle(requireArguments().getString(ARGUMENTS_STRING_TITLE))
        rootView.button.setOnClickListener {
            val scene2 = Scene(rootView)
            val manager = TransitionManager()
            manager.transitionTo(scene2)
        }
    }
}