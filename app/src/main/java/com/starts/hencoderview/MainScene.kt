package com.starts.hencoderview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import com.bytedance.scene.ui.template.AppCompatScene


/**

 *文件描述：.
 *作者：Created by Administrator on 2020/4/29.
 *版本号：1.0

 */
class MainScene : AppCompatScene() {
    private lateinit var btnSport: AppCompatButton
    private lateinit var btnFlipBoard: AppCompatButton
    private lateinit var btnTagLayout: AppCompatButton
    private lateinit var btnScaleImg: AppCompatButton
    private lateinit var btnFragmentTest: AppCompatButton

    override fun onCreateContentView(
        inflater: LayoutInflater,
        container: ViewGroup,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.activity_main, null, false)
        btnSport = root.findViewById(R.id.btnSport)
        btnFlipBoard = root.findViewById(R.id.btnFlipBoard)
        btnTagLayout = root.findViewById(R.id.btnTagLayout)
        btnScaleImg = root.findViewById(R.id.btnScaleImg)
        btnFragmentTest = root.findViewById(R.id.btnFragmentTest)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setTitle("HenCoderView")
        btnSport.setOnClickListener {
            requireNavigationScene().push(SportScene::class.java)
        }
        btnFlipBoard.setOnClickListener {
            requireNavigationScene().push(FlipBoardScene::class.java)
        }
        btnTagLayout.setOnClickListener {
            requireNavigationScene().push(TagLayoutScene::class.java)
        }
        btnScaleImg.setOnClickListener {
            requireNavigationScene().push(ScaleImageScene::class.java)
        }
        btnFragmentTest.setOnClickListener {
            requireNavigationScene().push(ViewPagerTestScene::class.java)
        }

        setStatusBarVisible(true)
        setToolbarVisible(true)
    }

}