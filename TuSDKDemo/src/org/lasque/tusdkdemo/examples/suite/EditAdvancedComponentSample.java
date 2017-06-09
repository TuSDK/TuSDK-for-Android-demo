/** 
 * TuSdkDemo
 * AdvancedComponentSimple.java
 *
 * @author 		Clear
 * @Date 		2015-4-21 下午12:49:54 
 * @Copyright 	(c) 2015 tusdk.com. All rights reserved.
 * 
 */
package org.lasque.tusdkdemo.examples.suite;

import org.lasque.tusdk.TuSdkGeeV1;
import org.lasque.tusdk.core.TuSdkResult;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.impl.activity.TuFragment;
import org.lasque.tusdk.impl.components.TuAlbumMultipleComponent;
import org.lasque.tusdk.impl.components.TuEditComponent;
import org.lasque.tusdk.modules.components.TuSdkComponent.TuSdkComponentDelegate;
import org.lasque.tusdk.modules.components.TuSdkHelperComponent;
import org.lasque.tusdkdemo.R;
import org.lasque.tusdkdemo.SampleBase;
import org.lasque.tusdkdemo.SampleGroup.GroupType;

import android.app.Activity;

/**
 * 裁切+滤镜+贴纸组件范例
 * 
 * @author Clear
 */
public class EditAdvancedComponentSample extends SampleBase
{
	/** 裁切+滤镜+贴纸组件范例 */
	public EditAdvancedComponentSample()
	{
		super(GroupType.SuiteSample, R.string.sample_EditAdvancedComponent);
	}

	/**
	 * 组件显示入口，在本例中，启动编辑器前，先从相册组件选择图片作为输入源，按照开发需求，可以选择多种方式来启动编辑器，比如相机拍照后直接调用编辑器。
	 * 欢迎访问文档中心 http://tusdk.com/doc 查看更多示例。
	 * 
	 * SDK中所有的编辑组件都支持三种格式的输入源： Bitmap | File | ImageSqlInfo
	 * 
	 * // 设置图片
	 * component.setImage(result.image)
	 *  		// 设置系统照片
	 *  		.setImageSqlInfo(result.imageSqlInfo)
	 *  		// 设置临时文件
	 *  		.setTempFilePath(result.imageFile)
	 * 
	 * 处理优先级: Image > TempFilePath > ImageSqlInfo
	 * 
	 */
	@Override
	public void showSample(Activity activity)
	{
		if (activity == null) return;
		// see-http://tusdk.com/docs/android/api/org/lasque/tusdk/impl/components/base/TuSdkHelperComponent.html
		this.componentHelper = new TuSdkHelperComponent(activity);

		TuAlbumMultipleComponent component = TuSdkGeeV1.albumMultipleCommponent(activity, new TuSdkComponentDelegate()
		{
			@Override
			public void onComponentFinished(TuSdkResult result, Error error, TuFragment lastFragment)
			{
				openEditAdvanced(result, error, lastFragment);
			}
		});
		
		// 设置相册照片排序方式
		// component.componentOption().albumListOption().setPhotosSortDescriptor(PhotoSortDescriptor.Date_Modified);
		// 设置最大支持的图片尺寸 默认：8000 * 8000
		// component.componentOption().albumListOption().setMaxSelectionImageSize(new TuSdkSize(8000, 8000));
		
		component.showComponent();
	}

	/** 开启裁切+滤镜+贴纸组件 */
	private void openEditAdvanced(TuSdkResult result, Error error, TuFragment lastFragment)
	{
		if (result == null || error != null) return;

		// 组件委托
		TuSdkComponentDelegate delegate = new TuSdkComponentDelegate()
		{
			@Override
			public void onComponentFinished(TuSdkResult result, Error error, TuFragment lastFragment)
			{
				TLog.d("onEditAdvancedComponentReaded: %s | %s", result, error);
				
				// 默认输出为 Bitmap  -> result.image
				
				// 如果保存到临时文件 (默认不保存, 当设置为true时, TuSdkResult.imageFile, 处理完成后将自动清理原始图片)
				// option.setSaveToTemp(true);  ->  result.imageFile

				// 保存到系统相册 (默认不保存, 当设置为true时, TuSdkResult.sqlInfo, 处理完成后将自动清理原始图片)
				// option.setSaveToAlbum(true);  -> result.image
			}
		};

		// 组件选项配置
		// @see-http://tusdk.com/docs/android/api/org/lasque/tusdk/impl/components/TuEditComponent.html
		TuEditComponent component = null;

		if (lastFragment == null)
		{
			component = TuSdkGeeV1.editCommponent(this.componentHelper.activity(), delegate);
		}
		else
		{
			component = TuSdkGeeV1.editCommponent(lastFragment, delegate);
		}

		// @see-http://tusdk.com/docs/android/api/org/lasque/tusdk/impl/components/TuEditComponentOption.html
		// component.componentOption()

		// @see-http://tusdk.com/docs/android/api/org/lasque/tusdk/impl/components/edit/TuEditEntryOption.html
		// component.componentOption().editEntryOption()

		// @see-http://tusdk.com/docs/android/api/org/lasque/tusdk/impl/components/edit/TuEditCuterOption.html
		// component.componentOption().editCuterOption()

		// @see-http://tusdk.com/docs/android/api/org/lasque/tusdk/impl/components/filter/TuEditFilterOption.html
		// component.componentOption().editFilterOption()

		// @see-http://tusdk.com/docs/android/api/org/lasque/tusdk/impl/components/sticker/TuStickerChooseOption.html
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