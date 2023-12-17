package com.example.rhythmix.ui;
import android.content.Context;
import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

public class BitmapUtils {

    public static Bitmap createBlurredBitmap(Context context, Bitmap originalBitmap, int radius) {
        // Create an empty bitmap with the same size as the original
        Bitmap blurredBitmap = Bitmap.createBitmap(originalBitmap.getWidth(), originalBitmap.getHeight(), Bitmap.Config.ARGB_8888);

        // Create RenderScript
        RenderScript rs = RenderScript.create(context);

        // Create an allocation from Bitmap
        Allocation input = Allocation.createFromBitmap(rs, originalBitmap);
        Allocation output = Allocation.createFromBitmap(rs, blurredBitmap);

        // Create ScriptIntrinsicBlur
        ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        script.setInput(input);

        // Set the blur radius (0 < radius <= 25)
        script.setRadius(radius);

        // Start the ScriptIntrinsicBlur
        script.forEach(output);

        // Copy the output Allocation to the blurredBitmap
        output.copyTo(blurredBitmap);

        // Release resources
        rs.destroy();

        return blurredBitmap;
    }
}
