package com.starts.hencoderview.scene

import android.content.Intent
import android.graphics.Color
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.bytedance.scene.Scene
import com.bytedance.scene.navigation.NavigationSceneGetter.requireNavigationScene
import com.bytedance.scene.ui.template.AppCompatScene
import com.starts.hencoderview.R
import com.starts.hencoderview.databinding.ActivityMainBinding
import com.starts.hencoderview.databinding.RelatelayoutUiTestBinding
import com.starts.hencoderview.util.ARGUMENTS_STRING_TITLE
import com.starts.hencoderview.util.dp2px
import timber.log.Timber


/**

 *文件描述：.
 *作者：Created by Administrator on 2020/4/29.
 *版本号：1.0
 */
class MainScene : AppCompatScene() {
    lateinit var binding: ActivityMainBinding

    override fun onCreateContentView(
        inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle?
    ): View {
        binding = ActivityMainBinding.inflate(inflater)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    val onFrameMetricsAvailableListener =
        (Window.OnFrameMetricsAvailableListener { window, frameMetrics, dropCountSinceLastInvocation ->
            val frameMetricsCopy = FrameMetrics(frameMetrics)
            val onMeasureAndLayout = frameMetricsCopy.getMetric(FrameMetrics.LAYOUT_MEASURE_DURATION) / 1000
//            val transformations = frameMetricsCopy.getMetric(FrameMetrics.DRAW_DURATION) / 1000
            Timber.tag("OnFrameMetricsAvailable")
                .d("测绘布局时间 = $onMeasureAndLayout")
        })

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setTitle("HenCoderView")
        setStatusBarVisible(true)
        setToolbarVisible(true)
        binding.rvCard.layoutManager =
            GridLayoutManager(requireSceneContext(), 2, RecyclerView.VERTICAL, false)
        val array = arrayListOf<MainCard>().apply {
            addAll(MainCard.values())
        }
        binding.rvCard.addItemDecoration(object : ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
            ) {
                super.getItemOffsets(outRect, view, parent, state)
                if (parent.getChildLayoutPosition(view) % 2 == 0) {
                    outRect.left += dp2px(6)
                    outRect.right += dp2px(3)
                }else if(parent.getChildLayoutPosition(view) % 2 == 1){
                    outRect.left += dp2px(3)
                    outRect.right += dp2px(6)
                }else{
                    outRect.left += dp2px(3)
                    outRect.right += dp2px(3)
                }
                outRect.top += dp2px(12)
            }
        })
        binding.rvCard.adapter = MainAdapter(array, this)
        requireActivity().window.addOnFrameMetricsAvailableListener(onFrameMetricsAvailableListener,Handler(Looper.getMainLooper()))

    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onDestroyView() {
        requireActivity().window.removeOnFrameMetricsAvailableListener(onFrameMetricsAvailableListener)
        super.onDestroyView()
    }

}

class MainAdapter(private val data: ArrayList<MainCard>, val scene: Scene) :
    RecyclerView.Adapter<MainHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        return MainHolder(AppCompatButton(parent.context).apply {
            layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp2px(80))
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

enum class MainCard(val content: String, val onItemClick: ((Scene) -> Unit)? = null) {
    Sport(
        "环形进度条",
        { scene ->
            requireNavigationScene(scene).push(SportScene::class.java, Bundle().apply {
                this.putString(ARGUMENTS_STRING_TITLE, "环形进度条")
            })
        }),
    FlipBoard(
        "FlipBoard", { scene ->
            requireNavigationScene(scene).push(FlipBoardScene::class.java,
                Bundle().apply {
                    this.putString(ARGUMENTS_STRING_TITLE, "FlipBoard")
                })
        }),
    TagLayout(
        "TagLayout", { scene ->
            requireNavigationScene(scene).push(TagLayoutScene::class.java,
                Bundle().apply {
                    this.putString(
                        ARGUMENTS_STRING_TITLE,
                        "TagLayout"
                    )
                })
        }),
    ScaleImage(
        "ScaleImage", { scene ->
            requireNavigationScene(scene).push(ScaleImageScene::class.java,
                Bundle().apply {
                    this.putString(
                        ARGUMENTS_STRING_TITLE,
                        "ScaleImage"
                    )
                })
        }),
    FragmentTest(
        "FragmentTest",
        { scene ->
            requireNavigationScene(scene).push(FragmentTestScene::class.java, android.os.Bundle()
                .apply {
                    this.putString(
                        ARGUMENTS_STRING_TITLE,
                        "FragmentTest"
                    )
                })
        }),
    Particle("粒子消散",
        { scene ->
            requireNavigationScene(scene).push(ParticleScatteringScene::class.java,
                Bundle().apply {
                    this.putString(
                        ARGUMENTS_STRING_TITLE,
                        "粒子消散"
                    )
                })
        }),
    WaveView("WaveView",
        { scene ->
            requireNavigationScene(scene).push(WaveViewScene::class.java,
                Bundle().apply {
                    this.putString(
                        ARGUMENTS_STRING_TITLE,
                        "WaveView"
                    )
                })
        }),
    MemoryLeak("内存泄漏", { scene ->
        val intent = Intent(
            scene.requireActivity(), com.starts.hencoderview.ui.MemoryLeakActivity::class.java
        )
        scene.requireActivity().startActivity(intent)
    }),
    ScaleAlpha("呼吸放大",
        { scene ->
            requireNavigationScene(scene).push(ScaleAlphaAnimScene::class.java,
                Bundle().apply {
                    this.putString(
                        ARGUMENTS_STRING_TITLE,
                        "呼吸放大"
                    )
                })
        }),
    DoubleRecycler("双RecyclerView分发",
        { scene ->
            requireNavigationScene(scene).push(FloatRecyclerScene::class.java,
                Bundle().apply {
                    this.putString(
                        ARGUMENTS_STRING_TITLE,
                        "双RecyclerView分发"
                    )
                })
        }),
    Transition("Transition动画",
        { scene ->
            requireNavigationScene(scene).push(TransitionScene::class.java,
                Bundle().apply {
                    this.putString(
                        ARGUMENTS_STRING_TITLE,
                        "Transition动画"
                    )
                })
        }),
    LinearLayoutTest("LinearLayoutTest", { scene ->
        requireNavigationScene(scene).push(
            LinearTestScene::class.java,
            Bundle().apply {
                this.putString(
                    ARGUMENTS_STRING_TITLE,
                    "LinearLayoutTest"
                )
            }
        )
    }),
    ConstrainLayoutTest("ConstrainLayoutTest", { scene ->
        requireNavigationScene(scene).push(
            ConstrainTestScene::class.java,
            Bundle().apply {
                this.putString(ARGUMENTS_STRING_TITLE, "ConstrainLayoutTest")
            }
        )
    }),
    CustomGroupTest("CustomLayoutTest", { scene ->
        requireNavigationScene(scene).push(
            CustomLayoutScene::class.java,
            Bundle().apply {
                this.putString(
                    ARGUMENTS_STRING_TITLE,
                    "CustomLayoutTest"
                )
            }
        )
    }),
    CustomUITest("CustomUITest", { scene ->
        requireNavigationScene(scene).push(
            CustomUIScene::class.java,
            Bundle().apply {
                this.putString(
                    ARGUMENTS_STRING_TITLE,
                    "CustomUITest"
                )
            }
        )
    }),ConstrainCompareTest("ConstrainCompareTest", { scene ->
        requireNavigationScene(scene).push(
            ConstrainCompareScene::class.java,
            Bundle().apply {
                this.putString(
                    ARGUMENTS_STRING_TITLE,
                    "ConstrainCompareTest"
                )
            }
        )
    }),RelateCompareTest("RelateCompareTest", { scene ->
        requireNavigationScene(scene).push(
            RelateLayoutScene::class.java,
            Bundle().apply {
                this.putString(
                    ARGUMENTS_STRING_TITLE,
                    "RelateCompareTest"
                )
            }
        )
    }),LinearCompareTest("LinearCompareTest", { scene ->
        requireNavigationScene(scene).push(
            LinearLayoutScene::class.java,
            Bundle().apply {
                this.putString(
                    ARGUMENTS_STRING_TITLE,
                    "LinearCompareTest"
                )
            }
        )
    })
}

