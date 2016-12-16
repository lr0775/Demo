package cc.stbl.demo.util;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;

import cc.stbl.demo.App;

/**
 * Created by Administrator on 2016/10/17.
 */

public class ImageUtils {

    public static void load(File file, ImageView view) {
        Glide.with(App.getContext())
                .load(file)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(view);
    }

    public static void load(String url, ImageView view) {
        Glide.with(App.getContext())
                .load(url)
                .into(view);
    }

}
