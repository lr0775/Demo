package cc.stbl.demo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import cc.stbl.demo.App;
import cc.stbl.demo.R;

public class SampleSizeActivity extends AppCompatActivity {

    private ImageView mIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_size);

        mIv = (ImageView) findViewById(R.id.iv);
        String url = "http://wx.qlogo.cn/mmopen/JU1WWEdBoOBU3jfZ4BiaVBJhog6rRr4UbibgouY16Rn1If6waZYoevR47J5ggVzQ04AIOTiaEFSgGtx1YiaMyRFpfhvwo4OABllr/0";
        Glide.with(App.getContext()).load(url).centerCrop().into(mIv);

        computeSampleSize();
    }

    private void computeSampleSize() {
//        BitmapFactory.Options opts = new BitmapFactory.Options();
//        opts.inJustDecodeBounds = true;
//        BitmapFactory.decodeFile(FileUtils.getFile(fileName).getAbsolutePath(), opts);
//        opts.inSampleSize = BitmapUtil.calculateInSampleSize(opts, Device.getWidth(), 600);
//        opts.inJustDecodeBounds = false;
//        Bitmap bm = BitmapFactory.decodeFile(FileUtils.getFile(fileName).getAbsolutePath(), opts);
    }
}
