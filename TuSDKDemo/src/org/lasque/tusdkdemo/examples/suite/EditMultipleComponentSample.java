/** 
 * TuSdkDemo
 * EditMultipleComponentSimple.java
 *
 * @author 		Clear
 * @Date 		2015-4-21 下午1:38:04 
 * @Copyright 	(c) 2015 tusdk.com. All rights reserved.
 * 
 */
package org.lasque.tusdkdemo.examples.suite;

import org.lasque.tusdk.TuSdkGeeV1;
import org.lasque.tusdk.core.TuSdkResult;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.TuSdkWaterMarkOption;
import org.lasque.tusdk.core.utils.TuSdkWaterMarkOption.TextPosition;
import org.lasque.tusdk.core.utils.TuSdkWaterMarkOption.WaterMarkPosition;
import org.lasque.tusdk.core.utils.image.BitmapHelper;
import org.lasque.tusdk.impl.activity.TuFragment;
import org.lasque.tusdk.impl.components.TuEditMultipleComponent;
import org.lasque.tusdk.modules.components.TuSdkComponent.TuSdkComponentDelegate;
import org.lasque.tusdk.modules.components.TuSdkHelperComponent;
import org.lasque.tusdkdemo.R;
import org.lasque.tusdkdemo.SampleBase;
import org.lasque.tusdkdemo.SampleGroup.GroupType;

import android.app.Activity;

/**
 * 照片美化组件范例
 * 
 * @author Clear
 */
public class EditMultipleComponentSample extends SampleBase
{
	/** 照片美化组件范例 */
	public EditMultipleComponentSample()
	{
		super(GroupType.SuiteSample, R.string.sample_EditMultipleComponent);
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

		TuSdkGeeV1.albumCommponent(activity, new TuSdkComponentDelegate()
		{
			@Override
			public void onComponentFinished(TuSdkResult result, Error error, TuFragment lastFragment)
			{
				openEditMultiple(result, error, lastFragment);
			}
		}).showComponent();
	}

	/** 开启照片美化组件 */
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
				
				// 默认输出为 Bitmap  -> result.image
				
				// 如果保存到临时文件 (默认不保存, 当设置为true时, TuSdkResult.imageFile, 处理完成后将自动清理原始图片)
				// option.setSaveToTemp(true);  ->  result.imageFile

				// 保存到系统相册 (默认不保存, 当设置为true时, TuSdkResult.sqlInfo, 处理完成后将自动清理原始图片)
				// option.setSaveToAlbum(true);  -> result.image
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
		
		// 设置水印选项 (默认为空，如果设置不为空，则输出的图片上将带有水印)
		component.componentOption().editMultipleOption().setWaterMarkOption(getWaterMarkOption(this.componentHelper.activity()));

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
	
	/**
	 * 水印设置
	 * 
	 * @return
	 */
	private TuSdkWaterMarkOption getWaterMarkOption(Activity activity)
	{
		TuSdkWaterMarkOption option = new TuSdkWaterMarkOption();
		
		// 文字或者图片需要至少设置一个
	    // 设置水印文字, 支持图文混排、图片或文字
	    option.setMarkText("");
	    
	    // 设置文字颜色 (默认:#FFFFFF)
	    option.setMarkTextColor("#FFFFFF");
	    
	    // 文字大小 (默认: 24 SP)
	    option.setMarkTextSize(24);
	    
	    // 文字阴影颜色 (默认:#000000)
	    option.setMarkTextShadowColor("#000000");
	    
	    // 设置水印图片, 支持图文混排、图片或文字
	    option.setMarkImage(BitmapHelper.getRawBitmap(activity, R.raw.sample_watermark));
	    
	    // 文字和图片顺序 (仅当图片和文字都非空时生效，默认: 文字在右)
	    option.setMarkTextPosition(TextPosition.Right);

	    // 设置水印位置 (默认: WaterMarkPosition.BottomRight)
	    option.setMarkPosition(WaterMarkPosition.BottomRight);
	    
	    // 设置水印距离图片边距 (默认: 6dp)
	    option.setMarkMargin(6);
	    
	    // 文字和图片间距 (默认: 2dp)
	    option.setMarkTextPadding(5);
	    
	    return option;
	}
}