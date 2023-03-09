package com.starts.hencoderview

import android.content.Intent
import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.bytedance.scene.Scene
import com.bytedance.scene.navigation.NavigationSceneGetter.requireNavigationScene
import com.bytedance.scene.ui.template.AppCompatScene
import com.starts.hencoderview.databinding.ActivityMainBinding
import com.starts.hencoderview.util.dp2px


/**

 *文件描述：.
 *作者：Created by Administrator on 2020/4/29.
 *版本号：1.0

 */
class MainScene : AppCompatScene() {
    lateinit var binding: ActivityMainBinding

    override fun onCreateContentView(
        inflater: LayoutInflater,
        container: ViewGroup,
        savedInstanceState: Bundle?
    ): View {
        binding = ActivityMainBinding.inflate(inflater)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setTitle("HenCoderView")
        setStatusBarVisible(true)
        setToolbarVisible(true)
        binding.rvCard.layoutManager =
            GridLayoutManager(requireSceneContext(), 3, RecyclerView.VERTICAL, false)
        val array = arrayListOf<MainCard>().apply {
            addAll(MainCard.values())
        }
        binding.rvCard.addItemDecoration(object : ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                super.getItemOffsets(outRect, view, parent, state)
                outRect.top += dp2px(12)
            }
        })
        binding.rvCard.adapter = MainAdapter(array,this)
    }
}

class MainAdapter(private val data: ArrayList<MainCard>,val scene: Scene) : RecyclerView.Adapter<MainHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        return MainHolder(AppCompatButton(parent.context).apply {
            layoutParams = ViewGroup.LayoutParams(dp2px(120), dp2px(60))
            this.setTextColor(ContextCompat.getColor(this.context, R.color.white))
            elevation = 5f
            isAllCaps = false
            this.setBackgroundColor(Color.parseColor("#59B0FF"))
        })
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        holder.itemRoot.text = data[position].content
        holder.itemRoot.setOnClickListener {
            data[position].onItemClick?.invoke(scene)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

}

class MainHolder(val itemRoot: AppCompatButton) : RecyclerView.ViewHolder(itemRoot)

enum class MainCard(val id: Int, val content: String,val onItemClick:((Scene)->Unit)? = null){
    Sport(0,"环形进度条",{scene->requireNavigationScene(scene).push(SportScene::class.java)}),
    FlipBoard(1,"FlipBoard",{scene->requireNavigationScene(scene).push(FlipBoardScene::class.java)}),
    TagLayout(2,"TagLayout",{scene->requireNavigationScene(scene).push(TagLayoutScene::class.java)}),
    ScaleImage(3,"ScaleImage",{scene->requireNavigationScene(scene).push(ScaleImageScene::class.java)}),
    FragmentTest(4,"FragmentTest",{scene->requireNavigationScene(scene).push(FragmentTestScene::class.java)}),
    Particle(5,"粒子消散",{scene->requireNavigationScene(scene).push(ParticleScatteringScene::class.java)}),
    WaveView(6,"WaveView",{scene->requireNavigationScene(scene).push(WaveViewScene::class.java)}),
    MemoryLeak(7,"内存泄漏",{scene->
        val intent = Intent(scene.requireActivity() , com.starts.hencoderview.ui.MemoryLeakActivity::class.java)
        scene.requireActivity().startActivity(intent)
    }),
    ScaleAlpha(8,"呼吸放大",{scene->requireNavigationScene(scene).push(ScaleAlphaAnimScene::class.java)}),
    DoubleRecycler(9,"双RecyclerView分发",{scene->requireNavigationScene(scene).push(FloatRecyclerScene::class.java)}),
    Transition(10,"Transition动画",{scene->requireNavigationScene(scene).push(TransitionScene::class.java)})
}

