package com.starts.hencoderview

import android.graphics.Bitmap
import android.util.LruCache
import android.util.SparseArray
import androidx.core.util.contains
import androidx.recyclerview.widget.RecyclerView

/**

 *文件描述：.
 *作者：Created by LostStars on 2020/12/13.
 *版本号：1.0
 */
class RecyclerPool {
    var mScrap = SparseArray<ScrapData>()

    val lruCache = object :LruCache<Int,Bitmap>(1024*1024){
        override fun sizeOf(key: Int?, value: Bitmap?): Int {
            return super.sizeOf(key, value)

        }

        override fun trimToSize(maxSize: Int) {
            super.trimToSize(maxSize)

        }

    }


    fun put(holder: RecyclerView.ViewHolder){
        val type = holder.itemViewType

        val scrapData = mScrap.get(type)
        if(scrapData == null){
            mScrap.put(type , ScrapData())
        }else{
            scrapData.scarpHeap.add(holder)
        }
    }

    fun getHolder(viewType:Int):RecyclerView.ViewHolder?{
        val scrapData = mScrap.get(viewType)
        if (scrapData.scarpHeap.isEmpty()){
            return null
        }
        scrapData.scarpHeap.forEachIndexed {index,holder->
//            if(holder.isAttachedToTransitionOverlay()){
                return scrapData.scarpHeap.removeAt(index)
//            }
        }
        return null
    }

}




class ScrapData{
    val scarpHeap = ArrayList<RecyclerView.ViewHolder>()
    val maxType = 5
    var mCreateRunningAverageNs: Long = 0
    var mBindRunningAverageNs: Long = 0
}