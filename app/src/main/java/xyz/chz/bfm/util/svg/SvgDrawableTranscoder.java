package xyz.chz.bfm.util.svg;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.resource.SimpleResource;
import com.bumptech.glide.load.resource.transcode.ResourceTranscoder;
import com.caverock.androidsvg.SVG;

/**
 * Convert the {@link SVG}'s internal representation to an Android-compatible one ({@link Picture}).
 */
public class SvgDrawableTranscoder implements ResourceTranscoder<SVG, Drawable> {
    private final Resources resources;

    public SvgDrawableTranscoder(Context context) {
        resources = context.getResources();
    }

    @Nullable
    @Override
    public Resource<Drawable> transcode(
            @NonNull Resource<SVG> toTranscode, @NonNull Options options) {
        SVG svg = toTranscode.get();
        float width = svg.getDocumentWidth();
        float height = svg.getDocumentHeight();

        Drawable drawable;
        if (width > 0 && height > 0) {
            float density = resources.getDisplayMetrics().density;
            Bitmap bitmap = Bitmap.createBitmap(Math.round(width * density), Math.round(height * density), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            canvas.scale(density, density);
            svg.renderToCanvas(canvas);
            drawable = new BitmapDrawable(resources, bitmap);
        } else {
            Picture picture = svg.renderToPicture();
            drawable = new PictureDrawable(picture);
        }
        return new SimpleResource<>(drawable);
    }
}
