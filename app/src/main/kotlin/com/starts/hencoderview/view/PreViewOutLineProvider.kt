package com.starts.hencoderview.view

import android.graphics.Outline
import android.graphics.Rect
import android.view.View
import android.view.ViewOutlineProvider

class PreViewOutLineProvider : ViewOutlineProvider() {
    override fun getOutline(view: View?, outline: Outline?) {
        val rect = Rect()
        view?.getGlobalVisibleRect(rect)
        val selfRect = Rect(0,0,rect.width(),rect.height())
        outline?.setRoundRect(selfRect,20f)
    }

}