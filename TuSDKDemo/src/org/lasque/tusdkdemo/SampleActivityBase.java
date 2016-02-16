/** 
 * TuSdkDemo
 * SampleActivityBase.java
 *
 * @author 		Yanlin
 * @Date 		2016-1-21 下午16:01:05 
 * @Copyright 	(c) 2015 tusdk.com. All rights reserved.
 * 
 */
package org.lasque.tusdkdemo;

import org.lasque.tusdkdemo.SampleGroup.GroupType;

import android.app.Activity;

/**
 * SDK 功能示例，演示单个功能的用法
 * 
 * @author Yanlin
 */
public class SampleActivityBase extends SampleBase
{

	public SampleActivityBase (GroupType groupId, int titleResId)
	{
		super(groupId, titleResId);
	}

	/** 视图类 */
	public Class<?> activityClazz;
	
	/** 显示范例 */
	public void showSample(Activity activity)
	{
		// leave it blank
	}
}
