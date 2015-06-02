/** 
 * TuSdkDemo
 * AdvancedComponentSimple.java
 *
 * @author 		Clear
 * @Date 		2015-4-21 下午12:49:54 
 * @Copyright 	(c) 2015 Lasque. All rights reserved.
 * 
 */
package org.lasque.tusdkdemo.simple;

import org.lasque.tusdk.core.TuSdk;
import org.lasque.tusdk.core.TuSdkResult;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.impl.activity.TuFragment;
import org.lasque.tusdk.impl.components.TuEditComponent;
import org.lasque.tusdk.impl.components.base.TuSdkHelperComponent;
import org.lasque.tusdk.impl.components.base.TuSdkComponent.TuSdkComponentDelegate;
import org.lasque.tusdkdemo.R;

import android.app.Activity;

/**
 * 高级图片编辑组件范例
 * 
 * @author Clear
 */
public class EditAdvancedComponentSimple extends SimpleBase
{
	/**
	 * 高级图片编辑组件范例
	 */
	public EditAdvancedComponentSimple()
	{
		super(2, R.string.simple_EditAdvancedComponent);
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
				openEditAdvanced(result, error, lastFragment);
			}
		}).showComponent();
	}

	/**
	 * 开启图片高级编辑
	 * 
	 * @param result
	 * @param error
	 * @param lastFragment
	 */
	private void openEditAdvanced(TuSdkResult result, Error error,
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
				TLog.d("onEditAdvancedComponentReaded: %s | %s", result, error);
			}
		};

		// 组件选项配置
		// @see-http://www.tusdk.com/docs/android/api/org/lasque/tusdk/impl/components/TuEditComponent.html
		TuEditComponent component = null;

		if (lastFragment == null)
		{
			component = TuSdk.editCommponent(this.componentHelper.activity(),
					delegate);
		}
		else
		{
			component = TuSdk.editCommponent(lastFragment, delegate);
		}

		// @see-http://www.tusdk.com/docs/android/api/org/lasque/tusdk/impl/components/TuEditComponentOption.html
		// component.componentOption()

		// @see-http://www.tusdk.com/docs/android/api/org/lasque/tusdk/impl/components/edit/TuEditEntryOption.html
		// component.componentOption().editEntryOption()

		// @see-http://www.tusdk.com/docs/android/api/org/lasque/tusdk/impl/components/edit/TuEditCuterOption.html
		// component.componentOption().editCuterOption()

		// @see-http://www.tusdk.com/docs/android/api/org/lasque/tusdk/impl/components/filter/TuEditFilterOption.html
		// component.componentOption().editFilterOption()

		// @see-http://www.tusdk.com/docs/android/api/org/lasque/tusdk/impl/components/sticker/TuStickerChooseOption.html
		// component.componentOption().editStickerOption()

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
