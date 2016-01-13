/** 
 * TuSdkDemo
 * GifImageViewSimple.java
 *
 * @author 		Yanlin
 * @Date 		2015-12-29 下午3:38:04 
 * @Copyright 	(c) 2015 tusdk.com. All rights reserved.
 * 
 */
package org.lasque.tusdkdemo.simple;

/**
 * Gif组件范例
 * 
 * @author Yanlin
 */
import org.lasque.tusdk.modules.components.TuSdkHelperComponent;
import org.lasque.tusdkdemo.R;
import org.lasque.tusdkdemo.view.DemoGifFragment;

import android.app.Activity;

public class GifImageViewSimple extends SimpleBase 
{

	public GifImageViewSimple() 
	{
		super(1, R.string.simple_GifComponent);
	}

	@Override
	public void showSimple(Activity activity) 
	{
		this.componentHelper = new TuSdkHelperComponent(activity);
		
		DemoGifFragment fragment = new DemoGifFragment();
		
		this.componentHelper.pushModalNavigationActivity(fragment);
	}

}
