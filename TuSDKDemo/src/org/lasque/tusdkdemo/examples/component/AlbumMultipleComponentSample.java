/** 
 * TuSdkDemo
 * AlbumComponentSimple.java
 *
 * @author 		Clear
 * @Date 		2015-4-21 下午1:39:10 
 * @Copyright 	(c) 2015 Lasque. All rights reserved.
 * 
 */
package org.lasque.tusdkdemo.examples.component;

import org.lasque.tusdkpulse.TuSdkGeeV1;
import org.lasque.tusdkpulse.core.TuSdkResult;
import org.lasque.tusdkpulse.core.utils.TLog;
import org.lasque.tusdkpulse.impl.activity.TuFragment;
import org.lasque.tusdkpulse.impl.components.TuAlbumMultipleComponent;
import org.lasque.tusdkpulse.modules.components.TuSdkComponent.TuSdkComponentDelegate;
import org.lasque.tusdkdemo.R;
import org.lasque.tusdkdemo.SampleBase;
import org.lasque.tusdkdemo.SampleGroup.GroupType;

import android.app.Activity;

/**
 * 多选相册组件范例
 * 
 * @author Clear
 */
public class AlbumMultipleComponentSample extends SampleBase
{
	/**
	 * 多选相册组件范例
	 */
	public AlbumMultipleComponentSample()
	{
		super(GroupType.ComponentSample, R.string.sample_AlbumMultipleComponent);
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
		
		// @see-http://tusdk.com/docs/android/image/api/org/lasque/tusdk/impl/components/TuAlbumMultipleComponent.html
		TuAlbumMultipleComponent comp = TuSdkGeeV1.albumMultipleCommponent(activity,
				new TuSdkComponentDelegate()
				{
					@Override
					public void onComponentFinished(TuSdkResult result,
							Error error, TuFragment lastFragment)
					{
						// if (lastFragment != null)
						// lastFragment.dismissActivityWithAnim();
						// 多选状态下使用 result.images 获取所选图片
						TLog.d("onAlbumCommponentReaded: %s | %s", result,
								error);
					}
				},
				// 设置最多选择九张照片
				9);

		// 组件选项配置
		// @see-http://tusdk.com/docs/android/image/api/org/lasque/tusdk/impl/components/TuAlbumComponentOption.html
		// comp.componentOption()

		// @see-http://tusdk.com/docs/android/image/api/org/lasque/tusdk/impl/components/album/TuAlbumListOption.html
		// comp.componentOption().albumListOption()

		// @see-http://tusdk.com/docs/android/image/api/org/lasque/tusdk/impl/components/camera/TuCameraOption.html
		// comp.componentOption().cameraOption()
		
		// 设置相册照片排序方式
		//comp.componentOption().albumListOption().setPhotosSortDescriptor(PhotoSortDescriptor.Date_Modified);
		
		// 设置最大支持的图片尺寸 默认：8000 * 8000
//		 comp.componentOption().albumListOption().setMaxSelectionImageSize(new TuSdkSize(8000, 8000));

		
		// 在组件执行完成后自动关闭组件
		comp.setAutoDismissWhenCompleted(true)
		// 显示组件
				.showComponent();
	}
}
