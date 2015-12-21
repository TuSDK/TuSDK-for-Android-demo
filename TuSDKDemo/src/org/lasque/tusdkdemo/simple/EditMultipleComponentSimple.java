/** 
 * TuSdkDemo
 * EditMultipleComponentSimple.java
 *
 * @author 		Clear
 * @Date 		2015-4-21 下午1:38:04 
 * @Copyright 	(c) 2015 tusdk.com. All rights reserved.
 * 
 */
package org.lasque.tusdkdemo.simple;

import org.lasque.tusdk.TuSdkGeeV1;
import org.lasque.tusdk.core.TuSdkResult;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.impl.activity.TuFragment;
import org.lasque.tusdk.impl.components.TuEditMultipleComponent;
import org.lasque.tusdk.modules.components.TuSdkComponent.TuSdkComponentDelegate;
import org.lasque.tusdk.modules.components.TuSdkHelperComponent;
import org.lasque.tusdkdemo.R;

import android.app.Activity;

/**
 * 多功能图片编辑组件范例
 * 
 * @author Clear
 */
public class EditMultipleComponentSimple extends SimpleBase
{
	/** 多功能图片编辑组件范例 */
	public EditMultipleComponentSimple()
	{
		super(2, R.string.simple_EditMultipleComponent);
	}

	/** 显示范例 */
	@Override
	public void showSimple(Activity activity)
	{
		if (activity == null) return;
		// see-http://tusdk.com/docs/android/api/org/lasque/tusdk/impl/components/base/TuSdkHelperComponent.html
		this.componentHelper = new TuSdkHelperComponent(activity);

		TuSdkGeeV1.albumCommponent(activity, new TuSdkComponentDelegate()
		{
			@Override
			public void onComponentFinished(TuSdkResult result, Error error, TuFragment lastFragment)
			{
				openEditMultiple(result, error, lastFragment);
			}
		}).showComponent();
	}

	/** 开启多功能图片编辑 */
	private void openEditMultiple(TuSdkResult result, Error error, TuFragment lastFragment)
	{
		if (result == null || error != null) return;

		// 组件委托
		TuSdkComponentDelegate delegate = new TuSdkComponentDelegate()
		{
			@Override
			public void onComponentFinished(TuSdkResult result, Error error, TuFragment lastFragment)
			{
				TLog.d("onEditMultipleComponentReaded: %s | %s", result, error);
			}
		};

		// 组件选项配置
		// @see-http://tusdk.com/docs/android/api/org/lasque/tusdk/impl/components/TuEditMultipleComponent.html
		TuEditMultipleComponent component = null;

		if (lastFragment == null)
		{
			component = TuSdkGeeV1.editMultipleCommponent(this.componentHelper.activity(), delegate);
		}
		else
		{
			component = TuSdkGeeV1.editMultipleCommponent(lastFragment, delegate);
		}

		// @see-http://tusdk.com/docs/android/api/org/lasque/tusdk/impl/components/TuEditMultipleComponentOption.html
		// component.componentOption()

		// @see-http://tusdk.com/docs/android/api/org/lasque/tusdk/impl/components/edit/TuEditMultipleOption.html
		// component.componentOption().editMultipleOption()

		// @see-http://tusdk.com/docs/android/api/org/lasque/tusdk/impl/components/edit/TuEditCuterOption.html
		// component.componentOption().editCuterOption()

		// @see-http://tusdk.com/docs/android/api/org/lasque/tusdk/impl/components/filter/TuEditFilterOption.html
		// component.componentOption().editFilterOption()

		// @see-http://tusdk.com/docs/android/api/org/lasque/tusdk/impl/components/filter/TuEditSkinOption.html
		// component.componentOption().editSkinOption()

		// @see-http://tusdk.com/docs/android/api/org/lasque/tusdk/impl/components/sticker/TuEditStickerOption.html
		// component.componentOption().editStickerOption()

		// @see-http://tusdk.com/docs/android/api/org/lasque/tusdk/impl/components/filter/TuEditAdjustOption.html
		// component.componentOption().editAdjustOption()

		// @see-http://tusdk.com/docs/android/api/org/lasque/tusdk/impl/components/filter/TuEditSharpnessOption.html
		// component.componentOption().editSharpnessOption()

		// @see-http://tusdk.com/docs/android/api/org/lasque/tusdk/impl/components/filter/TuEditApertureOption.html
		// component.componentOption().editApertureOption()

		// @see-http://tusdk.com/docs/android/api/org/lasque/tusdk/impl/components/filter/TuEditVignetteOption.html
		// component.componentOption().editVignetteOption()
		
		// @see-http://tusdk.com/docs/android/api/org/lasque/tusdk/impl/components/smudge/TuEditSmudgeOption.html
		// component.componentOption().editSmudgeOption()
		
		// @see-http://tusdk.com/docs/android/api/org/lasque/tusdk/impl/components/filter/TuEditWipeAndFilterOption.html
		// component.componentOption().editWipeAndFilterOption()

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