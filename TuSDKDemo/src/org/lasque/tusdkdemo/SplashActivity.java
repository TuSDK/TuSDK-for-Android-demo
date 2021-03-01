package org.lasque.tusdkdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import org.lasque.tusdkpulse.core.utils.ThreadHelper;

/**
 * 启动页
 */
public class SplashActivity extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        ThreadHelper.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, DemoEntryActivity.class));
                overridePendingTransition(R.anim.lsq_fade_in,R.anim.lsq_fade_out);
                finish();
            }
        },2000);
    }

}
