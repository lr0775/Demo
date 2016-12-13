package cc.stbl.demo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import cc.stbl.demo.R;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class UltraPullActivity extends AppCompatActivity {

    private PtrFrameLayout mPtrLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ultra_pull);

        mPtrLayout = (PtrFrameLayout) findViewById(R.id.layout_ptr);
        mPtrLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mPtrLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPtrLayout.refreshComplete();
                    }
                }, 500);
            }
        });
    }
}
