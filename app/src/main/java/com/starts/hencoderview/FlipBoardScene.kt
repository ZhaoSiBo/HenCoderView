package com.starts.hencoderview

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.SeekBar
import com.bytedance.scene.ui.template.AppCompatScene
import com.starts.hencoderview.view.FlipBoardView


/**

 *文件描述：.
 *作者：Created by Administrator on 2020/4/30.
 *版本号：1.0

 */
class FlipBoardScene : AppCompatScene() {
    private lateinit var flipView:FlipBoardView
    lateinit var seekRotate: SeekBar
    lateinit var seekCamera:SeekBar
    override fun onCreateContentView(
        inflater: LayoutInflater,
        container: ViewGroup,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.scene_flip_bord_view, null, false)
        flipView = view.findViewById(R.id.flipView)
        seekRotate = view.findViewById(R.id.seekRotate)
        seekCamera = view.findViewById(R.id.seekCamera)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setToolbarVisible(true)
        setStatusBarVisible(true)
        setTitle("FlipBoardView")


        val cameraAnimator = ObjectAnimator.ofFloat(flipView, "cameraAngle", 0f, 45f)
        cameraAnimator.interpolator = LinearInterpolator()
        cameraAnimator.duration = 300

        val animator = ObjectAnimator.ofFloat(flipView, "angle", 0f, 360f)
        animator.interpolator = DecelerateInterpolator()
        animator.duration = 1000

        val animatorSet = AnimatorSet()
        animatorSet.playSequentially(cameraAnimator, animator)
        animatorSet.startDelay = 500
        animatorSet.start()
        animatorSet.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {

            }
            override fun onAnimationEnd(animation: Animator?) {
                seekRotate.visibility = View.VISIBLE
                seekCamera.visibility = View.VISIBLE
            }

            override fun onAnimationCancel(animation: Animator?) {

            }

            override fun onAnimationStart(animation: Animator?) {
            }

        })

        seekRotate.setOnSeekBarChangeListener(object :SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                flipView.angle = (progress/100f) * 360
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })

        seekCamera.setOnSeekBarChangeListener(object :SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                flipView.cameraAngle = (progress/100f) * 90
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })



    }
}