package com.starts.hencoderview

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bytedance.scene.ui.template.AppCompatScene
import com.starts.hencoderview.databinding.ActivityMainBinding
import com.starts.hencoderview.ui.MemoryLeakActivity


/**

 *文件描述：.
 *作者：Created by Administrator on 2020/4/29.
 *版本号：1.0

 */
class MainScene : AppCompatScene() {
   lateinit var binding : ActivityMainBinding

    override fun onCreateContentView(
        inflater: LayoutInflater,
        container: ViewGroup,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivityMainBinding.inflate(inflater)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setTitle("HenCoderView")
        binding.btnSport.setOnClickListener {
            requireNavigationScene().push(SportScene::class.java)
        }
        binding.btnFlipBoard.setOnClickListener {
            requireNavigationScene().push(FlipBoardScene::class.java)
        }
        binding.btnTagLayout.setOnClickListener {
            requireNavigationScene().push(TagLayoutScene::class.java)
        }
        binding.btnScaleImg.setOnClickListener {
            requireNavigationScene().push(ScaleImageScene::class.java)
        }
        binding.btnFragmentTest.setOnClickListener {
            requireNavigationScene().push(ViewPagerTestScene::class.java)
        }
        binding.btnPSView.setOnClickListener {
            requireNavigationScene().push(ParticleScatteringScene::class.java)
        }
        binding.btnWave.setOnClickListener{
            requireNavigationScene().push(WaveViewScene::class.java)
        }
        binding.btnLeak.setOnClickListener {
            val intent = Intent(requireActivity() , MemoryLeakActivity::class.java)
            requireActivity().startActivity(intent)
        }
        binding.btnPath.setOnClickListener {
            requireNavigationScene().push(PathScene::class.java)
        }
        binding.btnEllipsize.setOnClickListener {
            requireNavigationScene().push(EllipsizedTextScene::class.java)
        }
        binding.btnCustom.setOnClickListener {
            requireNavigationScene().push(SongDetailScene::class.java)
        }
        binding.btnScaleAlphaImg.setOnClickListener {
            requireNavigationScene().push(ScaleAlphaAnimScene::class.java)
        }
        binding.btnBottomSheetBehavior.setOnClickListener {
            requireNavigationScene().push(MovieDetailScene::class.java)
        }

        setStatusBarVisible(true)
        setToolbarVisible(true)
    }
}