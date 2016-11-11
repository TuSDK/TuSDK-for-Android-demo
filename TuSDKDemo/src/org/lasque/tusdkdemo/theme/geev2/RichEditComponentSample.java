/** 
 * TuSDK.Gee.V2
 * RichEditComponentSample.java
 *
 * @author 		gh.li
 * @Date 		2016-10-26 上午09:20:29 
 * @Copyright 	(c) 2015 tusdk.com. All rights reserved.
 * 
 */
package org.lasque.tusdkdemo.theme.geev2;

import org.lasque.tusdk.core.TuSdkResult;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.geev2.TuSdkGeeV2;
import org.lasque.tusdk.geev2.impl.components.TuRichEditComponent;
import org.lasque.tusdk.impl.activity.TuFragment;
import org.lasque.tusdk.modules.components.TuSdkComponent.TuSdkComponentDelegate;
import org.lasque.tusdkdemo.R;
import org.lasque.tusdkdemo.SampleBase;
import org.lasque.tusdkdemo.SampleGroup.GroupType;

import android.app.Activity;

/**
 * GeeV2主题范例
 * 
 * @author gh.li
 *
 */
public class RichEditComponentSample extends SampleBase implements TuSdkComponentDelegate
{
	/** GeeV2主题范例 */
	public RichEditComponentSample()
	{
		super(GroupType.GeeTheme, R.string.sample_GeeV2Theme);
	}

	@Override
	public void showSample(Activity activity)
	{
		TuRichEditComponent comp =	TuSdkGeeV2.richEditCommponent(activity, this);

		// 组件选项配置
		// 设置是否启用图片编辑 默认 true
		// comp.componentOption().setEnableEditMultiple(true);
		
		// 相机组件配置
		// 设置拍照后是否预览图片 默认 true
		// comp.componentOption().cameraOption().setEnablePreview(true);

		// 多选相册组件配置
		// 设置相册最大选择数量
		comp.componentOption().albumMultipleComponentOption().albumListOption().setMaxSelection(9);
		
		// 多功能编辑组件配置项
		// 设置最大编辑数量
		comp.componentOption().editMultipleComponentOption().setMaxEditImageCount(9);
		
		// 设置没有改变的图片是否保存(默认 false)
		// comp.componentOption().editMultipleComponentOption().setEnableAlwaysSaveEditResult(false);
		
		// 设置编辑时是否支持追加图片 默认 true
		// comp.componentOption().editMultipleComponentOption().setEnableAppendImage(true);


		// 操作完成后是否自动关闭页面
		comp.setAutoDismissWhenCompleted(true)
		// 显示组件
			.showComponent();
	}
	
	/**
	 * 获取编辑结果
	 */
	@Override
	public void onComponentFinished(TuSdkResult result, Error error,TuFragment lastFragment) 
	{
		TLog.d("PackageComponentSample onComponentFinished: %s | %s", result.images, error);
		
//		for (ImageSqlInfo info : result.images)
//		{
//			// 1.通过ImageSqlInfo生成Bitmap
//			Bitmap mImage = BitmapHelper.getBitmap(info, true, new TuSdkSize(100, 100)); 
//
//			// 2.通过文件获取Bitmap
//			File file = new File(info.path);
//		}
	}
}
