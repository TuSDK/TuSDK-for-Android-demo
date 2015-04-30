/** 
 * TuSdkDemo
 * AlbumComponentSimple.java
 *
 * @author 		Clear
 * @Date 		2015-4-21 下午1:39:10 
 * @Copyright 	(c) 2015 Lasque. All rights reserved.
 * 
 */
package org.lasque.tusdkdemo.simple;

import org.lasque.tusdk.core.TuSdk;
import org.lasque.tusdk.core.TuSdkResult;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.impl.activity.TuFragment;
import org.lasque.tusdk.impl.components.TuAlbumComponent;
import org.lasque.tusdk.impl.components.base.TuSdkComponent.TuSdkComponentDelegate;
import org.lasque.tusdkdemo.R;

import android.app.Activity;

/**
 * 相册组件范例
 * 
 * @author Clear
 */
public class AlbumComponentSimple extends SimpleBase
{
	/**
	 * 相册组件范例
	 */
	public AlbumComponentSimple()
	{
		super(1, R.string.simple_AlbumComponent);
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

		// @see-http://www.tusdk.com/docs/android/api/org/lasque/tusdk/impl/components/TuAlbumComponent.html
		TuAlbumComponent comp = TuSdk.albumCommponent(activity,
				new TuSdkComponentDelegate()
				{
					@Override
					public void onComponentFinished(TuSdkResult result,
							Error error, TuFragment lastFragment)
					{
						// if (lastFragment != null)
						// lastFragment.dismissActivityWithAnim();
						TLog.d("onAlbumCommponentReaded: %s | %s", result,
								error);
					}
				});

		// 组件选项配置
		// @see-http://www.tusdk.com/docs/android/api/org/lasque/tusdk/impl/components/TuAlbumComponentOption.html
		// comp.componentOption()

		// @see-http://www.tusdk.com/docs/android/api/org/lasque/tusdk/impl/components/album/TuAlbumListOption.html
		// comp.componentOption().albumListOption()

		// @see-http://www.tusdk.com/docs/android/api/org/lasque/tusdk/impl/components/album/TuPhotoListOption.html
		// comp.componentOption().photoListOption()

		// 在组件执行完成后自动关闭组件
		comp.setAutoDismissWhenCompleted(true)
		// 显示组件
				.showComponent();
	}
}
