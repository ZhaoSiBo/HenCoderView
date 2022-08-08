# HenCoderView
工作过程中和平时，针对自定义View进行的实践和总结

### 电影详情页视图框架
[![电影详情页视图](https://s1.ax1x.com/2022/04/16/LtkGhF.gif)](https://imgtu.com/i/LtkGhF)

仿照豆瓣详情页写的滚动视图，不过豆瓣本身是做的两个View，一个用来拖拽，一个用来滑动，这里做了个升级，直接用一个ViewGroup来实现拖动和无缝滚动
动图中显示效果为两个RcyclerView实现，内部可以无限制使用类似多类型Adapter方面的库，任意组装数据，构造多类型复杂列表

### 思路：
1. 通过自定义ViewGroup来实现布局部分，onMeasure和onLayout中自定义RecyclerView的测绘和布局
2. 通过NestScrollParent接口处理滚动联动和fling的分发
3. 通过ViewDragHelper来处理拖拽
4. 通过事件分发机制解决事件冲突，处理重叠部分的拖拽冲突

### 轮播图的水波纹背景
![轮播图的水波纹背景](https://ftp.bmp.ovh/imgs/2020/11/3286b2d39b8cd13d.gif)

项目中实际应用的轮播图水波纹背景，将图片的保存使用LurCache单独存放，在实际开发过程中可以使用图片加载框架，
加载网络图片到内存，充当背景，也可以自己修改逻辑使用纯色暂时充当背景
### 思路：
1. 通过BitmapCache单独使用图片的存取功能
2. 通过在registerOnPageChangeCallback中使用下面的代码来判断是向左滑动中还是向右滑动中


                        if(positionOffset != 0f && positionOffsetPixels != 0){
                            if (position + positionOffset > sumPositionAndPositionOffset) {
                                //right to left
                            
                                fromRightToLeft = true
                                currentPosition = fixPosition(position)ø
                                nextPosition = fixPosition(currentPosition + 1)

                            } else {
                                calculateDirection()
                                //left to right
                              
                                fromRightToLeft = false
                                currentPosition = fixPosition(position + 1)
                                nextPosition = fixPosition(position)

                            }
                            sumPositionAndPositionOffset = position + positionOffset
                        }else{
                            //最后一刻，当前位置和下一个位置 ，则根据position来确定
                            currentPosition = position
                            nextPo
                            sition = fixPosition(position + 1)
                        }


3. 为避免越界使用fixPosition（）方法控制越界情况
4. 在onDraw（）方法中，使用创建的canvas和bitmap，对要操作的bitmap进行绘制，并使用PorterDuffXfermode(PorterDuff.Mode.SRC_IN) ， （PorterDuff.Mode.DST_OVER）
注意：
1. 在使用过程中，先画的是目标图像，后画的源图像，使用过程中注意使用离屏缓冲，避免port的使用失效
2. port的原理是在融合出，通过算法处理颜色保留

### 网易云特效
![网易云特效](https://ftp.bmp.ovh/imgs/2020/11/794e1bec8869a26f.gif)

### 思路：
1. 封装Particle粒子类（小的圆点），主要是用来保存每个点的坐标，速度，角度，透明度，偏移量
2. 使用for循环，通过使用drawCircle，500个圆点根据x，y坐标进行绘制（测量绘制时间，毫秒数小于16，绘制很安全）
3. 通过ValueAnimator，控制选点从中心向周围飘散
4. 使用三角函数，计算x，y坐标随时间推移的下一个点


### 红板报特效
![红板报](https://ftp.bmp.ovh/imgs/2020/11/f81a25d05eea4d04.gif)
### 思路：
1. 将 bitmap分为两部分看， 一部分为被camera翻转的部分，一部分为正常部分
2. 先调试，使用裁切api分成两部分
3. 然后使用camera绘制被翻转的一部分
4. 在固定绘制另外一部分
5. 最后使用动画控制旋转的角度，来达到效果


### Taglayout
![Taglayout](https://ftp.bmp.ovh/imgs/2020/11/99b24a4f62fb1578.gif)
### 思路：
1. 整体上，是测绘完所有子View，得到子View的坐标点，然后保存起来，在onlayout中使用
2. 通过for循环，遍历所有子View，通过measureChildWithMargins方法，讲View的宽高，配合剩余的在纵横两个方向上的空间，得到子View的合适位置
3. 判断是否超过ViewGroup的最大空间
4. 在onLayout中使用


### ScaleImageView
![ScaleImageView](https://ftp.bmp.ovh/imgs/2020/11/e08163e6fa0f32ca.gif)
### 思路：
1. 在绘制方法中，使用drawBitmap 配合 translate  scale 方法，对Bitmap进行操作
2. 在onTouchEvent中，使用GestureDetectorCompat对View收到的事件进行捕捉，包括，双击，按压，fling
3. 通过控制变量offsetX offsetY ，实现偏移操作，在onFling方法，使用postOnAnimation 推进动画


### 环形进度条

![](https://ftp.bmp.ovh/imgs/2020/11/d518f5ad94529b68.gif)

### 思路：
1. 先画原，再画弧线（进度条完成）
2. 文字要根据实际需求，通过getTextBounds判断画文字的位置，因为文字不一样，可能导致中心点变化（基线的固定，和文字是否越过基线）
3. 通过drawText 绘制文字










