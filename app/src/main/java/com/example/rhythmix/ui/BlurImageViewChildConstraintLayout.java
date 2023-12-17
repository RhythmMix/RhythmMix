package com.example.rhythmix.ui;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import androidx.constraintlayout.widget.ConstraintLayout;

public class BlurImageViewChildConstraintLayout extends ConstraintLayout {
    private ImageView imageView;
    private Drawable blurredShadow;
    private Paint paint;

    public BlurImageViewChildConstraintLayout(Context context) {
        super(context);
        init();
    }

    public BlurImageViewChildConstraintLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BlurImageViewChildConstraintLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        updateShadowBitmap();
    }

    private void updateShadowBitmap() {
        if (imageView != null) {
            Bitmap originalBitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            Bitmap blurredBitmap = BitmapUtils.createBlurredBitmap(getContext(), originalBitmap, 25);
                    blurredShadow = new BlurredShadowDrawable(blurredBitmap);
        }
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
        updateShadowBitmap();
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (blurredShadow != null) {
            blurredShadow.draw(canvas);
        }
    }

    private class BlurredShadowDrawable extends Drawable {
        private Bitmap blurredBitmap;
        private Rect bounds;

        public BlurredShadowDrawable(Bitmap blurredBitmap) {
            this.blurredBitmap = blurredBitmap;
            this.bounds = new Rect(0, 0, getWidth(), getHeight());
        }

        @Override
        public void draw(Canvas canvas) {
            canvas.drawBitmap(blurredBitmap, null, bounds, paint);
        }

        @Override
        public void setAlpha(int alpha) {
            paint.setAlpha(alpha);
        }

        @Override
        public void setColorFilter(android.graphics.ColorFilter colorFilter) {
            paint.setColorFilter(colorFilter);
        }

        @Override
        public int getOpacity() {
            return android.graphics.PixelFormat.TRANSLUCENT;
        }
    }
}
