package com.lightcone.feedback;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.NinePatch;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.lightcone.common.R;

/**
 * Created by chenkehui on 0017 2017/2/17.
 */

public class BubbleMaskFrame extends RelativeLayout {

    private Bitmap maskBitmap;
    private BitmapDrawable maskDrawable;
    private Paint paint;
    private Paint bgPaint;


    private int MAX_VIEW_WIDTH;
    private int MAX_VIEW_HEIGHT;
    public int height;
    public int width;

    public BubbleMaskFrame(Context context) {
        super(context);
        initBitmap(context);
    }

    public BubbleMaskFrame(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        initBitmap(context);
    }

    private void initBitmap(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics m = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(m);
        MAX_VIEW_WIDTH = m.widthPixels / 2;
        MAX_VIEW_HEIGHT = m.heightPixels / 4;
        height = MAX_VIEW_HEIGHT;
        width = MAX_VIEW_WIDTH;

        if (Integer.parseInt((String)getTag()) == 0) {
            maskBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.chat_bubble1);
        } else {
            maskBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.chat_bubble2);
        }

        bgPaint = new Paint();
        bgPaint.setColor(Color.RED);
        paint = new Paint();
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.saveLayer(null, null, Canvas.ALL_SAVE_FLAG);

        super.dispatchDraw(canvas);
        Rect rect = new Rect(0, 0, width, height);
        NinePatch patch = new NinePatch(maskBitmap, maskBitmap.getNinePatchChunk(), null);

        patch.draw(canvas, rect, paint);
    }

    public void setRatio(double ratio) {
        if (ratio <= 0) {
            ratio = 1;
        }
        if (ratio > 1) {
            width = MAX_VIEW_WIDTH;
            height = (int) (MAX_VIEW_WIDTH / ratio);
        } else {
            height = MAX_VIEW_HEIGHT;
            width = (int) (MAX_VIEW_HEIGHT * ratio);
        }
        invalidate();
    }


}
