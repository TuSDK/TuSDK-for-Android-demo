/** 
 * TuSdkDemo
 * AlbumComponentSimple.java
 *
 * @author 		Clear
 * @Date 		2015-4-21 下午1:39:10 
 * @Copyright 	(c) 2015 tusdk.com. All rights reserved.
 * 
 */
package org.lasque.tusdkdemo.examples.component;

import org.lasque.tusdk.TuSdkGeeV1;
import org.lasque.tusdk.core.TuSdkResult;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.impl.activity.TuFragment;
import org.lasque.tusdk.impl.components.TuAlbumComponent;
import org.lasque.tusdk.modules.components.TuSdkComponent.TuSdkComponentDelegate;
import org.lasque.tusdkdemo.R;
import org.lasque.tusdkdemo.SampleBase;
import org.lasque.tusdkdemo.SampleGroup.GroupType;

import android.app.Activity;

/**
 * 相册组件范例
 * 
 * @author Clear
 */
public class AlbumComponentSample extends SampleBase
{
	/** 相册组件范例 */
	public AlbumComponentSample()
	{
		super(GroupType.ComponentSample, R.string.sample_AlbumComponent);
	}

	/** 显示范例 */
	@Override
	public void showSample(Activity activity)
	{
		if (activity == null) return;

		// @see-http://tusdk.com/docs/android/api/org/lasque/tusdk/impl/components/TuAlbumComponent.html
		TuAlbumComponent comp = TuSdkGeeV1.albumCommponent(activity, new TuSdkComponentDelegate()
		{
			@Override
			public void onComponentFinished(TuSdkResult result, Error error, TuFragment lastFragment)
			{
				// if (lastFragment != null)
				// lastFragment.dismissActivityWithAnim();
				TLog.d("onAlbumCommponentReaded: %s | %s", result, error);
			}
		});

		// 组件选项配置
		// @see-http://tusdk.com/docs/android/api/org/lasque/tusdk/impl/components/TuAlbumComponentOption.html
		// comp.componentOption()

		// @see-http://tusdk.com/docs/android/api/org/lasque/tusdk/impl/components/album/TuAlbumListOption.html
		// comp.componentOption().albumListOption()

		// @see-http://tusdk.com/docs/android/api/org/lasque/tusdk/impl/components/album/TuPhotoListOption.html
		// comp.componentOption().photoListOption()

		// 设置选择照片的尺寸限制 默认：CGSize(8000,8000)
//		comp.componentOption().photoListOption().setMaxSelectionImageSize(new TuSdkSize(8000,8000));
		
		// 在组件执行完成后自动关闭组件
		comp.setAutoDismissWhenCompleted(true)
		// 显示组件
				.showComponent();
	}
}