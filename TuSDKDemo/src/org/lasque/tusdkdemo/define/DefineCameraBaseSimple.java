/** 
 * TuSdkDemo
 * DefineCameraSimple.java
 *
 * @author 		Clear
 * @Date 		2015-4-21 下午1:40:59 
 * @Copyright 	(c) 2015 tusdk.com. All rights reserved.
 * 
 */
package org.lasque.tusdkdemo.define;

import org.lasque.tusdk.core.utils.hardware.CameraHelper;
import org.lasque.tusdk.modules.components.TuSdkHelperComponent;
import org.lasque.tusdkdemo.R;
import org.lasque.tusdkdemo.simple.SimpleBase;

import android.app.Activity;

/**
 * 自定义相机范例
 * 
 * @author Clear
 */
public class DefineCameraBaseSimple extends SimpleBase
{
	/**
	 * 自定义相机范例
	 */
	public DefineCameraBaseSimple()
	{
		super(4, R.string.define_CameraBase);
	}

	/**
	 * 显示范例
	 * 
	 * @param activity
	 */
	@Override
	public void showSimple(Activity activity)
	{
		if (activity == null) return;

		// 如果不支持摄像头显示警告信息
		if (CameraHelper.showAlertIfNotSupportCamera(activity)) return;

		// see-http://tusdk.com/docs/android/api/org/lasque/tusdk/impl/components/base/TuSdkHelperComponent.html
		this.componentHelper = new TuSdkHelperComponent(activity);

		this.componentHelper.presentModalNavigationActivity(
				new DefineCameraBaseFragment(), true);
	}
}
