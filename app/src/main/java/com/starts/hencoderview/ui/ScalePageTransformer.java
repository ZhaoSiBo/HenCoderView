package com.starts.hencoderview.ui;

import android.view.View;

import androidx.viewpager2.widget.ViewPager2;

/**
 * @author Administrator
 */

public class ScalePageTransformer implements ViewPager2.PageTransformer {

    private static final float MIN_SCALE = 0.8f;
    private static final float MIN_ALPHA = 1f;

    /**
     * 当ViewPager滑动时，每一个界面都会回掉该方法，并且该方法跟随滑动不断回调
     * 参数：
     * view：当前停留显示的页面（注意：下面的循环不要直接用该view，因为该view会被自动加上一层FrameLayout布局）
     * position：当前页面滑动的位置，以当前页面的左侧边为0，两边终点分别为-1和1
     */
    @Override
    public void transformPage(View view, float position) {
        int pageWidth = view.getWidth();
        int pageHeight = view.getHeight();
        // [-Infinity,-1)
        if (position < -1) {
            // This page is way off-screen to the left.
            view.setAlpha(0);

        } else if (position <= 1) {
            // a页滑动至b页 ； a页从 0.0 -1 ；b页从1 ~ 0.0
            // Modify the default slide transition to shrink the page as well
            float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
            float vertMargin = pageHeight * (1 - scaleFactor) / 2;
            float horzMargin = pageWidth * (1 - scaleFactor) / 2;
            if (position < 0) {
                //api版本>=11，view相对原始位置的偏移量，不影响view属性
                view.setTranslationX(horzMargin - vertMargin / 2);
            } else {
                view.setTranslationX(-horzMargin + vertMargin / 2);
            }

            // Scale the page down (between MIN_SCALE and 1)
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);

            // Fade the page relative to its size.
            view.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE)
                    / (1 - MIN_SCALE) * (1 - MIN_ALPHA));

        } else {
            // This page is way off-screen to the right.
            view.setAlpha(0);
        }
    }
}
