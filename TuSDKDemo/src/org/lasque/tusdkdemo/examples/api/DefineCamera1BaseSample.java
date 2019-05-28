/** 
 * TuSdkDemo
 * DefineCamera1BaseSimple.java
 *
 * @author 		Clear
 * @Date 		2015-12-27 上午10:29:54 
 * @Copyright 	(c) 2015 tusdk.com. All rights reserved.
 * 
 */
package org.lasque.tusdkdemo.examples.api;

import org.lasque.tusdk.core.utils.hardware.CameraHelper;
import org.lasque.tusdk.modules.components.TuSdkHelperComponent;
import org.lasque.tusdkdemo.R;
import org.lasque.tusdkdemo.SampleBase;
import org.lasque.tusdkdemo.SampleGroup.GroupType;

import android.app.Activity;

/**
 * Camera1 底层API调用相机范例
 * 
 * @author Clear
 */
public class DefineCamera1BaseSample extends SampleBase
{
	/**
	 * Camera1 底层API调用相机范例
	 */
	public DefineCamera1BaseSample()
	{
		super(GroupType.APISample, R.string.sample_api_Camera1Base);
	}

	/**
	 * 显示范例
	 * 
	 * @param activity
	 */
	@Override
	public void showSample(Activity activity)
	{
		if (activity == null) return;

		// 如果不支持摄像头显示警告信息
		if (CameraHelper.showAlertIfNotSupportCamera(activity)) return;

		// see-http://tusdk.com/docs/android/image/api/org/lasque/tusdk/modules/components/TuSdkHelperComponent.html
		this.componentHelper = new TuSdkHelperComponent(activity);

		this.componentHelper.presentModalNavigationActivity(new DefineCamera1BaseFragment(), true);
	}
}