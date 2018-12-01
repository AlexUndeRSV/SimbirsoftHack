package com.simbirsoft.testmaps.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;

import java.net.URL;

public class BitmapUtils {
    public static Bitmap getBitmapFromSVG(Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = (DrawableCompat.wrap(drawable)).mutate();
        }
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public static Bitmap loadImage(Context context, String path, int dp) {
        try {
            URL url = new URL(path);
            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            float px = dp * context.getResources().getDisplayMetrics().density;
            float coef;
            if (bmp.getHeight() > bmp.getWidth()) {
                coef = px / bmp.getHeight();
            } else {
                coef = px / bmp.getWidth();
            }
            return Bitmap.createScaledBitmap(bmp,
                    (int) (bmp.getWidth() * coef),
                    (int) (bmp.getHeight() * coef),
                    false);
        } catch (Exception e) {
            return null;
        }
    }
}
