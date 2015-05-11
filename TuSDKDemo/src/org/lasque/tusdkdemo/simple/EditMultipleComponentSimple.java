/** 
 * TuSdkDemo
 * EditMultipleComponentSimple.java
 *
 * @author 		Clear
 * @Date 		2015-4-21 下午1:38:04 
 * @Copyright 	(c) 2015 Lasque. All rights reserved.
 * 
 */
package org.lasque.tusdkdemo.simple;

import org.lasque.tusdk.core.TuSdk;
import org.lasque.tusdk.core.TuSdkResult;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.impl.activity.TuFragment;
import org.lasque.tusdk.impl.components.TuEditMultipleComponent;
import org.lasque.tusdk.impl.components.base.TuSdkComponent.TuSdkComponentDelegate;
import org.lasque.tusdk.impl.components.base.TuSdkHelperComponent;
import org.lasque.tusdkdemo.R;

import android.app.Activity;

/**
 * 多功能图片编辑组件范例
 * 
 * @author Clear
 */
public class EditMultipleComponentSimple extends SimpleBase
{
	/**
	 * 多功能图片编辑组件范例
	 */
	public EditMultipleComponentSimple()
	{
		super(2, R.string.simple_EditMultipleComponent);
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
		// see-http://www.tusdk.com/docs/android/api/org/lasque/tusdk/impl/components/base/TuSdkHelperComponent.html
		this.componentHelper = new TuSdkHelperComponent(activity);

		TuSdk.albumCommponent(activity, new TuSdkComponentDelegate()
		{
			@Override
			public void onComponentFinished(TuSdkResult result, Error error,
					TuFragment lastFragment)
			{
				openEditMultiple(result, error, lastFragment);
			}
		}).showComponent();
	}

	/**
	 * 开启多功能图片编辑
	 * 
	 * @param result
	 * @param error
	 * @param lastFragment
	 */
	private void openEditMultiple(TuSdkResult result, Error error,
			TuFragment lastFragment)
	{
		if (result == null || error != null) return;

		// 组件委托
		TuSdkComponentDelegate delegate = new TuSdkComponentDelegate()
		{
			@Override
			public void onComponentFinished(TuSdkResult result, Error error,
					TuFragment lastFragment)
			{
				TLog.d("onEditMultipleComponentReaded: %s | %s", result, error);
			}
		};

		// 组件选项配置
		// @see-http://www.tusdk.com/docs/android/api/org/lasque/tusdk/impl/components/TuEditMultipleComponent.html
		TuEditMultipleComponent component = null;

		if (lastFragment == null)
		{
			component = TuSdk.editMultipleCommponent(
					this.componentHelper.activity(), delegate);
		}
		else
		{
			component = TuSdk.editMultipleCommponent(lastFragment, delegate);
		}

		// @see-http://www.tusdk.com/docs/android/api/org/lasque/tusdk/impl/components/TuEditMultipleComponentOption.html
		// component.componentOption()

		// @see-http://www.tusdk.com/docs/android/api/org/lasque/tusdk/impl/components/edit/TuEditMultipleOption.html
		// component.componentOption().editMultipleOption()

		// @see-http://www.tusdk.com/docs/android/api/org/lasque/tusdk/impl/components/edit/TuEditCuterOption.html
		// component.componentOption().editCuterOption()

		// @see-http://www.tusdk.com/docs/android/api/org/lasque/tusdk/impl/components/edit/TuEditFilterOption.html
		// component.componentOption().editFilterOption()

		// @see-http://www.tusdk.com/docs/android/api/org/lasque/tusdk/impl/components/edit/TuEditSkinOption.html
		// component.componentOption().editSkinOption()

		// @see-http://www.tusdk.com/docs/android/api/org/lasque/tusdk/impl/components/sticker/TuEditStickerOption.html
		// component.componentOption().editStickerOption()

		// @see-http://www.tusdk.com/docs/android/api/org/lasque/tusdk/impl/components/edit/TuEditAdjustOption.html
		// component.componentOption().editAdjustOption()

		// @see-http://www.tusdk.com/docs/android/api/org/lasque/tusdk/impl/components/edit/TuEditSharpnessOption.html
		// component.componentOption().editSharpnessOption()

		// @see-http://www.tusdk.com/docs/android/api/org/lasque/tusdk/impl/components/edit/TuEditApertureOption.html
		// component.componentOption().editApertureOption()

		// @see-http://www.tusdk.com/docs/android/api/org/lasque/tusdk/impl/components/edit/TuEditVignetteOption.html
		// component.componentOption().editVignetteOption()

		// 设置图片
		component.setImage(result.image)
		// 设置系统照片
				.setImageSqlInfo(result.imageSqlInfo)
				// 设置临时文件
				.setTempFilePath(result.imageFile)
				// 在组件执行完成后自动关闭组件
				.setAutoDismissWhenCompleted(true)
				// 开启组件
				.showComponent();
	}
}
