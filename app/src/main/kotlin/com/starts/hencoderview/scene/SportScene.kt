package com.starts.hencoderview.scene

import android.animation.Animator
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.SeekBar
import com.bytedance.scene.ui.template.AppCompatScene
import com.starts.hencoderview.R
import com.starts.hencoderview.util.ARGUMENTS_STRING_TITLE
import com.starts.hencoderview.view.SportView

/**

 *文件描述：.
 *作者：Created by Administrator on 2020/4/29.
 *版本号：1.0

 */
class SportScene : AppCompatScene() {

    lateinit var sportView: SportView
    lateinit var seekBar: SeekBar

    override fun onCreateContentView(
        inflater: LayoutInflater,
        container: ViewGroup,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.scene_sport_view, null, false)
        sportView = view.findViewById(R.id.sportView)
        seekBar = view.findViewById(R.id.seekBar)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setToolbarVisible(true)
        setStatusBarVisible(true)
        setTitle(requireArguments().getString(ARGUMENTS_STRING_TITLE))
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            requireActivity().window.decorView.systemUiVisibility =
//                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
//        }
        val animator = ObjectAnimator.ofInt(sportView, "progress", 0, 65)
        animator.interpolator = DecelerateInterpolator()
        animator.duration = 1000
        animator.startDelay = 500
        animator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator) {

            }

            override fun onAnimationEnd(animation: Animator) {
                seekBar.visibility = View.VISIBLE
            }

            override fun onAnimationCancel(animation: Animator) {
            }

            override fun onAnimationStart(animation: Animator) {
            }

        })

        animator.start()

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                sportView.progress = progress
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })


    }
}