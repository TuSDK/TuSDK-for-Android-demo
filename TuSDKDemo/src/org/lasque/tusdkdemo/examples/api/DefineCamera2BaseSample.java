/** 
 * TuSdkDemo
 * DefineCamera2BaseSimple.java
 *
 * @author 		Clear
 * @Date 		2015-12-8 上午11:36:41 
 * @Copyright 	(c) 2015 tusdk.com. All rights reserved.
 * 
 */
package org.lasque.tusdkdemo.examples.api;

import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.utils.hardware.Camera2Helper;
import org.lasque.tusdk.core.view.TuSdkViewHelper;
import org.lasque.tusdk.modules.components.TuSdkHelperComponent;
import org.lasque.tusdkdemo.R;
import org.lasque.tusdkdemo.SampleBase;
import org.lasque.tusdkdemo.SampleGroup.GroupType;

import android.app.Activity;
import android.os.Build;

/**
 * Camera2 底层API调用相机范例
 * 
 * @author Clear
 */
public class DefineCamera2BaseSample extends SampleBase
{
	/**
	 * Camera2 底层API调用相机范例
	 */
	public DefineCamera2BaseSample()
	{
		super(GroupType.APISample, R.string.sample_api_Camera2Base);
	}

	@Override
	public void showSample(Activity activity)
	{
		if (activity == null) return;

		// 低于5.0或者设备为有限支持不允许使用Camera2 API
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP || Camera2Helper.hardwareOnlySupportLegacy(activity))
		{
			TuSdkViewHelper.alert(activity, TuSdkContext.getString("lsq_carema_alert_title"), TuSdkContext.getString("lsq_carema_unsupport_camera2"),
					TuSdkContext.getString("lsq_button_done"));
			return;
		}

		// 如果不支持摄像头显示警告信息
		if (Camera2Helper.showAlertIfNotSupportCamera(activity)) return;

		// see-http://tusdk.com/docs/android/image/api/org/lasque/tusdk/modules/components/TuSdkHelperComponent.html
		this.componentHelper = new TuSdkHelperComponent(activity);
		this.componentHelper.presentModalNavigationActivity(new DefineCamera2BaseFragment(), true);
	}
}