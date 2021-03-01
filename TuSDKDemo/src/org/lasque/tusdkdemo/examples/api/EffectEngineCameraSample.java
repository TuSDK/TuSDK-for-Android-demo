package org.lasque.tusdkdemo.examples.api;

import android.app.Activity;
import android.os.Build;

import org.lasque.tusdkpulse.core.TuSdkContext;
import org.lasque.tusdkpulse.core.utils.hardware.Camera2Helper;
import org.lasque.tusdkpulse.core.utils.hardware.CameraHelper;
import org.lasque.tusdkpulse.core.view.TuSdkViewHelper;
import org.lasque.tusdkpulse.modules.components.TuSdkHelperComponent;
import org.lasque.tusdkdemo.R;
import org.lasque.tusdkdemo.SampleBase;
import org.lasque.tusdkdemo.SampleGroup;

/******************************************************************
 * droid-sdk-image
 * org.lasque.tusdkdemo.examples.api
 *
 * @author      : Clear
 * @Date        : 2020/6/9 12:33 PM
 * @Copyright   : (c) 2020 tutucloud.com. All rights reserved.
 * @brief       : 特效相机自定义 - 底层API
 * @details     : 
 ******************************************************************/
// 特效相机自定义 - 底层API
public class EffectEngineCameraSample extends SampleBase
{
    // 特效相机自定义 - 底层API
    public EffectEngineCameraSample()
    {
        super(SampleGroup.GroupType.APISample, R.string.sample_api_EffectEngineCamera);
    }

    @Override
    public void showSample(Activity activity)
    {
        if (activity == null) return;

        // 如果不支持摄像头显示警告信息
        if (CameraHelper.showAlertIfNotSupportCamera(activity)) return;

        // see-http://tusdk.com/docs/android/image/api/org/lasque/tusdk/modules/components/TuSdkHelperComponent.html
        this.componentHelper = new TuSdkHelperComponent(activity);

        this.componentHelper.presentModalNavigationActivity(new EffectEngineCameraFragment(), true);
    }
}
