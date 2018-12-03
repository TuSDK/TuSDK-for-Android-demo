/** 
 * TuSdkDemo
 * SimpleBase.java
 *
 * @author 		Clear
 * @Date 		2015-4-21 下午12:52:59 
 * @Copyright 	(c) 2015 tusdk.com. All rights reserved.
 * 
 */
package org.lasque.tusdkdemo;

import org.lasque.tusdk.modules.components.TuSdkHelperComponent;
import org.lasque.tusdkdemo.SampleGroup.GroupType;

import android.app.Activity;

/**
 * 范例接口，封装好的组件示例
 * 
 * @author Clear
 */
public abstract class SampleBase
{
	/** 分组ID */
	public GroupType groupId;

	/** 标题资源ID */
	public int titleResId;
	
	/** 组件帮助方法 */
	// see-http://tusdk.com/docs/android/image/api/org/lasque/tusdk/modules/components/TuSdkHelperComponent.html
	public TuSdkHelperComponent componentHelper;

	/**
	 * 范例分组头部信息
	 * 
	 * @param groupId
	 *            分组ID
	 * @param titleResId
	 *            标题资源ID
	 */
	public SampleBase(GroupType groupId, int titleResId)
	{
		this.groupId = groupId;
		this.titleResId = titleResId;
	}

	/** 显示范例 */
	public abstract void showSample(Activity activity);
}