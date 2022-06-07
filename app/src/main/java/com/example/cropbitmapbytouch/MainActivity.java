package com.example.cropbitmapbytouch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.example.cropbitmapbytouch.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new CutImage(this));
    }

    class CutImage extends View {
        Bitmap bitmap;
        Path path = new Path();
        Paint paint;

        public CutImage(Context context) {
            super(context);
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_solarus_logo);
            paint = new Paint();
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.RED);
            paint.setStrokeWidth(5f);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.drawBitmap(bitmap, 0, 0, null);
            canvas.drawPath(path, paint);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float x = event.getX();
            float y = event.getY();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    path.moveTo(x, y);
                    break;
                case MotionEvent.ACTION_MOVE:
                    path.lineTo(x, y);
                    break;

                case MotionEvent.ACTION_UP:
                    path.lineTo(x, y);
                    crop();
                    break;
            }
            invalidate();
            return true;
        }

        private void crop(){
            Bitmap croppedBitmap = Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(),bitmap.getConfig());
            Canvas cropCanvas = new Canvas(croppedBitmap);
            Paint paint = new Paint();
            cropCanvas.drawPath(path,paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            cropCanvas.drawBitmap(bitmap,0,0,paint);
            bitmap = croppedBitmap;
            invalidate();
        }
    }
}