package lightcone.com.pack.util;

import android.view.View;

/**
 * Created by panjianye on 2018/2/10.
 * <p>
 * ViewPager.setPageTransformer(true, new PageTransformer() {
 * //iewPage页面滑动时回调的方法,
 * //@param page当前滑动的view
 * //@param position 当从右向左滑的时候,左边page的position是[0一-1]变化的
 * //右边page的position是[1一0]变化的,再次滑动的时候,刚才变化到-1(即已经画出视野的page)將从-1变化到-2,而当前可见的page和右边滑过来
 * //的page的position将再次从[0一-1]变化 和 [1一0]变化,但是我们关心是position是[-1一1]变化的page,所以处理动画的时候需要我们过滤一下
 *
 * @Override public void transformPage(View page, float position) {
 * rollingPage(page,position);//调用翻页效果
 * }
 * });
 */

public class ViewPageTransformer {

    /**
     * 动画效果1  凹陷的3D效果
     * 部分手机闪退
     */
    public static void sink3D(View view, float position) {
        if (position >= -1 && position <= 1) {
            view.setPivotX(position < 0 ? view.getWidth() : 0);
            view.setRotationY(-90 * position);
        }
    }

    /**
     * 动画效果2  凸起的3D效果
     * 部分手机（魅族）闪退
     */
    public static void raised3D(View view, float position) {
        if (position >= -1 && position <= 1) {
            view.setPivotX(position < 0 ? view.getWidth() : 0);//设置要旋转的Y轴的位置
            view.setRotationY(90 * position);//开始设置属性动画值
        }
    }

    /**
     * 动画效果3  仿QQ的缩放动画效果
     */
    public static void scaleImitate(View view, float position) {
        if (position >= -1 && position <= 1) {
            view.setPivotX(position > 0 ? 0 : view.getWidth() / 2);
            //view.setPivotY(view.getHeight()/2);
            view.setScaleX((float) ((1 - Math.abs(position) < 0.5) ? 0.5 : (1 - Math.abs(position))));
            view.setScaleY((float) ((1 - Math.abs(position) < 0.5) ? 0.5 : (1 - Math.abs(position))));
        }
    }

    /**
     * 动画效果4  仿掌阅的翻书动画效果
     * 分析翻书的效果,可以分解为两部分:1.左边的view绕着左边的轴旋转,同时x方向上有缩放的效果
     * 要注意的是因为是viewpager左边的view在滑动的时候是要向左边移动的,但我们要的翻书效果在翻页完成前
     * 是一直在读者视角内的,所以左边的view在滑动的时候要进行向右的平移
     * 2.右边的view从可见的时候开始就一直在左view的下方,但是作为viewpager他是从右边慢慢滑到当前的位置的
     * 所以要达到这个效果就需要进行一个x方向的平移动画
     */
    public static void rollingPage(View view, float position) {
        if (position >= -1 && position <= 1) {
            view.setPivotX(0);
            if (position < 0) {
                view.setTranslationX(-position * view.getWidth());
                view.setRotationY(90 * position);
                view.setScaleX(1 - Math.abs(position));
            } else {
                view.setTranslationX(-position * view.getWidth());
            }
        }
    }

    public static void depthPage(View view, float position) {
        final float MIN_SCALE = 0.75f;

        int pageWidth = view.getWidth();
        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            view.setAlpha(0);
        } else if (position <= 0) { // [-1,0]
            // Use the default slide transition when moving to the left page
            view.setAlpha(1);
            view.setTranslationX(0);
            view.setScaleX(1);
            view.setScaleY(1);
        } else if (position <= 1) { // (0,1]
            // Fade the page out.
            view.setAlpha(1 - position);
            // Counteract the default slide transition
            view.setTranslationX(pageWidth * -position);
            // Scale the page down (between MIN_SCALE and 1)
            float scaleFactor = MIN_SCALE + (1 - MIN_SCALE) * (1 - Math.abs(position));
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);
        } else { // (1,+Infinity]
            // This page is way off-screen to the right.
            view.setAlpha(0);
        }
    }
}
