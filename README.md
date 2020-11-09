# HenCoderView
工作过程中和平时，针对自定义View进行的实践和总结

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