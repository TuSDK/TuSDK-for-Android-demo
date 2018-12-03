/** 
 * TuSdkDemo
 * EditAndCutComponentSimple.java
 *
 * @author 		Clear
 * @Date 		2015-4-21 下午1:35:50 
 * @Copyright 	(c) 2015 tusdk.com. All rights reserved.
 * 
 */
package org.lasque.tusdkdemo.examples.suite;

import org.lasque.tusdk.TuSdkGeeV1;
import org.lasque.tusdk.core.TuSdkResult;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.impl.activity.TuFragment;
import org.lasque.tusdk.impl.components.TuAlbumComponent;
import org.lasque.tusdk.impl.components.edit.TuEditTurnAndCutFragment;
import org.lasque.tusdk.impl.components.edit.TuEditTurnAndCutFragment.TuEditTurnAndCutFragmentDelegate;
import org.lasque.tusdk.impl.components.edit.TuEditTurnAndCutOption;
import org.lasque.tusdk.modules.components.TuSdkComponent.TuSdkComponentDelegate;
import org.lasque.tusdk.modules.components.TuSdkHelperComponent;
import org.lasque.tusdkdemo.R;
import org.lasque.tusdkdemo.SampleBase;
import org.lasque.tusdkdemo.SampleGroup.GroupType;

import android.app.Activity;

/**
 * 裁切+滤镜组件范例
 * 
 * @author Clear
 */
public class EditAndCutComponentSample extends SampleBase implements TuEditTurnAndCutFragmentDelegate
{
	/** 裁切+滤镜组件范例 */
	public EditAndCutComponentSample()
	{
		super(GroupType.SuiteSample, R.string.sample_EditAndCutComponent);
	}

	/**
	 * 组件显示入口，在本例中，启动编辑器前，先从相册组件选择图片作为输入源，按照开发需求，可以选择多种方式来启动编辑器，比如相机拍照后直接调用编辑器。
	 * 欢迎访问文档中心 http://tusdk.com/doc 查看更多示例。
	 * 
	 * SDK中所有的编辑组件都支持三种格式的输入源： Bitmap | File | ImageSqlInfo
	 * 
	 * // 设置图片
	 * fragment.setImage(image)
	 *  		// 设置系统照片
	 *  		.setImageSqlInfo(imageSqlInfo)
	 *  		// 设置临时文件
	 *  		.setTempFilePath(imageFile)
	 * 
	 * 处理优先级: Image > TempFilePath > ImageSqlInfo
	 * 
	 */
	@Override
	public void showSample(Activity activity)
	{
		if (activity == null) return;
		// see-http://tusdk.com/docs/android/image/api/org/lasque/tusdk/modules/components/TuSdkHelperComponent.html
		this.componentHelper = new TuSdkHelperComponent(activity);

		// 开启相册选择照片
		TuAlbumComponent component = TuSdkGeeV1.albumCommponent(activity, new TuSdkComponentDelegate()
		{
			@Override
			public void onComponentFinished(TuSdkResult result, Error error, TuFragment lastFragment)
			{
				openTuEditTurnAndCut(result, error, lastFragment);
			}
		});
		
		
		// 设置最大支持的图片尺寸 默认：8000 * 8000
//		 component.componentOption().photoListOption().setMaxSelectionImageSize(new TuSdkSize(8000, 8000));
		
		 component.showComponent();
	}

	/**
	 * 开启裁切+滤镜组件
	 * 
	 * @param result
	 *            返回结果
	 * @param error
	 *            异常信息
	 * @param lastFragment
	 *            最后显示的控制器
	 */
	private void openTuEditTurnAndCut(TuSdkResult result, Error error, TuFragment lastFragment)
	{
		if (result == null || error != null) return;

		// 组件选项配置
		// @see-http://tusdk.com/docs/android/image/api/org/lasque/tusdk/impl/components/edit/TuEditTurnAndCutOption.html
		TuEditTurnAndCutOption option = new TuEditTurnAndCutOption();

		// 是否开启滤镜支持 (默认: 关闭)
		option.setEnableFilters(true);
		// 开启用户滤镜历史记录
		option.setEnableFiltersHistory(true);
		// 开启在线滤镜
		option.setEnableOnlineFilter(true);

		// 显示滤镜标题视图
		option.setDisplayFiltersSubtitles(true);

		// 需要裁剪的长宽
		option.setCutSize(new TuSdkSize(640, 640));

		// 是否在控制器结束后自动删除临时文件
		option.setAutoRemoveTemp(true);

		// 是否显示处理结果预览图 (默认：关闭，调试时可以开启)
		option.setShowResultPreview(true);
		// 是否渲染滤镜封面 (使用设置的滤镜直接渲染，需要拥有滤镜列表封面设置权限，请访问TuSDK.com控制台)
		// option.setRenderFilterThumb(true);

		TuEditTurnAndCutFragment fragment = option.fragment();

		// 输入的图片对象 (处理优先级: Image > TempFilePath > ImageSqlInfo)
		fragment.setImageSqlInfo(result.imageSqlInfo);

		fragment.setDelegate(this);

		// 如果lastFragment不存在，您可以使用如下方法开启fragment
		if (lastFragment == null)
		{
			this.componentHelper.presentModalNavigationActivity(fragment);
		}
		else
		{
			// 开启裁切+滤镜组件
			lastFragment.pushFragment(fragment);
		}
	}

	/**
	 * 图片编辑完成
	 * 
	 * @param fragment
	 *            旋转和裁剪视图控制器
	 * @param result
	 *            旋转和裁剪视图控制器处理结果
	 */
	@Override
	public void onTuEditTurnAndCutFragmentEdited(TuEditTurnAndCutFragment fragment, TuSdkResult result)
	{
		if (!fragment.isShowResultPreview())
		{
			fragment.hubDismissRightNow();
			fragment.dismissActivityWithAnim();
		}
		TLog.d("onTuEditTurnAndCutFragmentEdited: %s", result);
		
		// 默认输出为 Bitmap  -> result.image
		
		// 如果保存到临时文件 (默认不保存, 当设置为true时, TuSdkResult.imageFile, 处理完成后将自动清理原始图片)
		// option.setSaveToTemp(true);  ->  result.imageFile

		// 保存到系统相册 (默认不保存, 当设置为true时, TuSdkResult.sqlInfo, 处理完成后将自动清理原始图片)
		// option.setSaveToAlbum(true);  -> result.image
	}

	/**
	 * 图片编辑完成 (异步方法)
	 * 
	 * @param fragment
	 *            旋转和裁剪视图控制器
	 * @param result
	 *            旋转和裁剪视图控制器处理结果
	 * @return 是否截断默认处理逻辑 (默认: false, 设置为True时使用自定义处理逻辑)
	 */
	@Override
	public boolean onTuEditTurnAndCutFragmentEditedAsync(TuEditTurnAndCutFragment fragment, TuSdkResult result)
	{
		TLog.d("onTuEditTurnAndCutFragmentEditedAsync: %s", result);
		return false;
	}

	@Override
	public void onComponentError(TuFragment fragment, TuSdkResult result, Error error)
	{
		TLog.d("onComponentError: fragment - %s, result - %s, error - %s", fragment, result, error);
	}
}