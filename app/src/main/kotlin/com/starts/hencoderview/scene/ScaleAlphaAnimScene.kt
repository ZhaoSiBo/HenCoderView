package com.starts.hencoderview.scene

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationSet
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import com.bytedance.scene.ui.template.AppCompatScene
import com.starts.hencoderview.databinding.SceneScaleAlphaBinding

/**

 *文件描述：.
 *作者：Created by lorizhao on 2021/10/31.
 *版本号：1.0

 */
class ScaleAlphaAnimScene : AppCompatScene() {
    lateinit var binding: SceneScaleAlphaBinding
    override fun onCreateContentView(
        inflater: LayoutInflater,
        container: ViewGroup,
        savedInstanceState: Bundle?
    ): View {
        binding = SceneScaleAlphaBinding.inflate(inflater)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setStatusBarVisible(true)
        setToolbarVisible(true)
        setTitle("ScaleAlphaAnim")
        startScaleAnimation(binding.view)
    }

    private fun startScaleAnimation(view: View) {
        val scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 1.4f)
        val scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 1.4f)

        val alpha = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f)

        scaleX.repeatCount = ValueAnimator.INFINITE
        scaleY.repeatCount = ValueAnimator.INFINITE
        alpha.repeatCount = ValueAnimator.INFINITE

        scaleX.repeatMode = ValueAnimator.RESTART
        scaleY.repeatMode = ValueAnimator.RESTART
        alpha.repeatMode = ValueAnimator.RESTART
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(scaleX,scaleY,alpha)
        animatorSet.interpolator = DecelerateInterpolator()
        animatorSet.duration = 800
        animatorSet.start()

    }
}