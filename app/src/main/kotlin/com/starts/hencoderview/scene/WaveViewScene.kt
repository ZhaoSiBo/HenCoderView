package com.starts.hencoderview.scene

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bytedance.scene.ui.template.AppCompatScene
import com.starts.hencoderview.databinding.SceneWaveViewBinding
import com.starts.hencoderview.ui.ScalePageTransformer
import com.starts.hencoderview.util.dp2px
import com.starts.hencoderview.view.ViewPager2LoopAdapter
import com.starts.hencoderview.view.ViewPager2LoopViewHolder
import com.zhpan.bannerview.BannerViewPager
import com.zhpan.indicator.enums.IndicatorSlideMode
import com.zhpan.indicator.enums.IndicatorStyle

/**

 *文件描述：.
 *作者：Created by Administrator on 2020/11/10.
 *版本号：1.0

 */
class WaveViewScene : AppCompatScene() {
    lateinit var binding: SceneWaveViewBinding

    private val bannerAdapter = ViewPager2LoopAdapter()

    private lateinit var banner: BannerViewPager<String, ViewPager2LoopViewHolder>


    private val imageList = arrayListOf(
        "https://pic2.zhimg.com/80/v2-f6466210c615e67f70cb8c1f4bf4621f_720w.jpg?source=1940ef5c",
        "https://pic2.zhimg.com/80/v2-40a15ca300e5c32eb58bab6b54614cd8_720w.jpg?source=1940ef5c",
        "https://pic1.zhimg.com/80/v2-c3e869051a97652c5cd0289e8a9005a4_720w.jpg?source=1940ef5c",
        "https://pic1.zhimg.com/80/v2-361f4e84b5e728232485356ee50f2fe8_720w.jpg?source=1940ef5c",
        "https://pic2.zhimg.com/80/v2-c9cd38c46a5828d9875be3461229924f_720w.jpg?source=1940ef5c",
        "https://pic1.zhimg.com/80/v2-0efb007b8ea5f888bd5fe8de34611b9e_720w.jpg?source=1940ef5c",
        "https://pic2.zhimg.com/80/v2-561182aaf0345f806cd2e2df734e4ea8_720w.jpg?source=1940ef5c",
        "https://pic1.zhimg.com/80/v2-524e5a7f3589575ad0162d0c0fea4f22_720w.jpg?source=1940ef5c"
    )

    override fun onCreateContentView(
        inflater: LayoutInflater,
        container: ViewGroup,
        savedInstanceState: Bundle?
    ): View {
        binding = SceneWaveViewBinding.inflate(inflater)
        banner = binding.banner as BannerViewPager<String, ViewPager2LoopViewHolder>
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setTitle("WaveView")

        banner.apply {
            adapter = bannerAdapter
            setAutoPlay(true)
            setLifecycleRegistry(lifecycle)
            setIndicatorStyle(IndicatorStyle.ROUND_RECT)
            setIndicatorSliderGap(dp2px(5))
            setIndicatorMargin(0, 0, 0, dp2px(16))
            setIndicatorSlideMode(IndicatorSlideMode.NORMAL)
            setIndicatorSliderRadius(dp2px(10), dp2px(5))
            setPageTransformer(ScalePageTransformer())
            setScrollDuration(800)
            setIndicatorSliderColor(Color.parseColor("#80ffffff"), Color.WHITE)
        }.create(imageList)
        binding.waveView.bindViewPager(banner)
    }


}