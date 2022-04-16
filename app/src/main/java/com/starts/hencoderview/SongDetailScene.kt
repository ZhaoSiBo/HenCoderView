package com.starts.hencoderview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bytedance.scene.ui.template.AppCompatScene
import com.starts.hencoderview.view.PodcastRankItemView

/**

 *文件描述：.
 *作者：Created by zhaosibo on 2021/3/24.
 *版本号：1.0

 */
class SongDetailScene : AppCompatScene() {

    lateinit var root:PodcastRankItemView

    override fun onCreateContentView(
        inflater: LayoutInflater,
        container: ViewGroup,
        savedInstanceState: Bundle?
    ): View? {
        root = PodcastRankItemView(requireSceneContext())
        return root
    }



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setStatusBarVisible(true)
        setToolbarVisible(true)
        setTitle("SongDetailViewGroup")
//        root.songName.text = "Vibration/ep.9五月天的青春你需要了解下,Vibration/ep.9五月天的青春你需要了解下,Vibration/ep.9五月天的青春你需要了解下"
//        root.songName.setBackgroundColor(Color.GREEN)
////        root.songDetails.text = "歌曲详情"
//        root.songDetails.setTextWithKeepText("五月来了，据说这是一个非常多人都喜欢的月份，在盛夏来临之前不仅天气舒服，而且充满各种未知的幻想，而且充满各种未知的幻想，而且充满各种未知的幻想"
//            , ""
//            ,true , R.drawable.playing_com_into , -1  , -1)
//        root.songDetails.setTextColor(Color.WHITE)
//        root.songPublishTime.text = "2018年10月17日更新"
    }

}