package superconn.pds.sw.superconn.camera;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

public class testView extends SurfaceView {
    private ScaleGestureDetector SGD;
    private float width, height = 0;
    private float scale = 1f;
    private float minScale = 1f;
    private float maxScale = 2f;
    int left, top, right, bottom;
    private Context context;
    private boolean isSingleTouch;

    public testView(Context context, AttributeSet attr) {
        super(context,attr);
        Log.e("pylon 테스트","view 생성");
        //this.context = context;

       // init();
    }
    private void init() {

        setOnTouchListener(new MyTouchListeners());

        SGD = new ScaleGestureDetector(context, new ScaleListener());
        Log.e("pylon 테스트","sgd 생성");
        this.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

            }
        });
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (width == 0 && height == 0) {
            width = testView.this.getWidth();
            height = testView.this.getHeight();
            this.left = left;
            this.right = right;
            this.top = top;
            this.bottom = bottom;
        }

    }

    public void setContext(Context con) {
        context=con;
        init();
    }

    private class MyTouchListeners implements View.OnTouchListener {

        float dX, dY;

        MyTouchListeners() {
            super();
        }

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            SGD.onTouchEvent(event);
            if (event.getPointerCount() > 1) {
                isSingleTouch = false;
            } else {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    isSingleTouch = true;
                }
            }
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    dX = testView.this.getX() - event.getRawX();
                    dY = testView.this.getY() - event.getRawY();
                    break;

                case MotionEvent.ACTION_MOVE:
                    if (isSingleTouch) {
                        testView.this.animate()
                                .x(event.getRawX() + dX)
                                .y(event.getRawY() + dY)
                                .setDuration(0)
                                .start();
                        checkDimension(testView.this);
                    }
                    break;
                default:
                    return true;
            }
            return true;
        }
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            Log.e("onGlobalLayout: ", scale + " " + width + " " + height);
            scale *= detector.getScaleFactor();
            scale = Math.max(minScale, Math.min(scale, maxScale));

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    (int) (width * scale), (int) (height * scale));
            testView.this.setLayoutParams(params);
            checkDimension(testView.this);
            return true;
        }
    }

    private void checkDimension(View vi) {
        if (vi.getX() > left) {
            vi.animate()
                    .x(left)
                    .y(vi.getY())
                    .setDuration(0)
                    .start();
        }

        if ((vi.getWidth() + vi.getX()) < right) {
            vi.animate()
                    .x(right - vi.getWidth())
                    .y(vi.getY())
                    .setDuration(0)
                    .start();
        }

        if (vi.getY() > top) {
            vi.animate()
                    .x(vi.getX())
                    .y(top)
                    .setDuration(0)
                    .start();
        }

        if ((vi.getHeight() + vi.getY()) < bottom) {
            vi.animate()
                    .x(vi.getX())
                    .y(bottom - vi.getHeight())
                    .setDuration(0)
                    .start();
        }
    }

}
