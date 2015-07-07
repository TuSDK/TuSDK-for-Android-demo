/** 
 * TuSdkDemo
 * EditAndCutComponentSimple.java
 *
 * @author 		Clear
 * @Date 		2015-4-21 下午1:35:50 
 * @Copyright 	(c) 2015 Lasque. All rights reserved.
 * 
 */
package org.lasque.tusdkdemo.simple;

import org.lasque.tusdk.core.TuSdk;
import org.lasque.tusdk.core.TuSdkResult;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.impl.activity.TuFragment;
import org.lasque.tusdk.impl.components.base.TuSdkComponent.TuSdkComponentDelegate;
import org.lasque.tusdk.impl.components.base.TuSdkHelperComponent;
import org.lasque.tusdk.impl.components.edit.TuEditTurnAndCutFragment;
import org.lasque.tusdk.impl.components.edit.TuEditTurnAndCutFragment.TuEditTurnAndCutFragmentDelegate;
import org.lasque.tusdk.impl.components.edit.TuEditTurnAndCutOption;
import org.lasque.tusdkdemo.R;

import android.app.Activity;

/**
 * 图片编辑组件 (裁剪)范例
 * 
 * @author Clear
 */
public class EditAndCutComponentSimple extends SimpleBase implements
		TuEditTurnAndCutFragmentDelegate
{
	/**
	 * 高级图片编辑组件范例
	 */
	public EditAndCutComponentSimple()
	{
		super(2, R.string.simple_EditAndCutComponent);
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
		// see-http://tusdk.com/docs/android/api/org/lasque/tusdk/impl/components/base/TuSdkHelperComponent.html
		this.componentHelper = new TuSdkHelperComponent(activity);

		// 开启相册选择照片
		TuSdk.albumCommponent(activity, new TuSdkComponentDelegate()
		{
			@Override
			public void onComponentFinished(TuSdkResult result, Error error,
					TuFragment lastFragment)
			{
				openTuEditTurnAndCut(result, error, lastFragment);
			}
		}).showComponent();
	}

	/**
	 * 开启图片编辑组件 (裁剪)
	 * 
	 * @param result
	 *            返回结果
	 * @param error
	 *            异常信息
	 * @param lastFragment
	 *            最后显示的控制器
	 */
	private void openTuEditTurnAndCut(TuSdkResult result, Error error,
			TuFragment lastFragment)
	{
		if (result == null || error != null) return;

		// 组件选项配置
		// @see-http://tusdk.com/docs/android/api/org/lasque/tusdk/impl/components/edit/TuEditTurnAndCutOption.html
		TuEditTurnAndCutOption option = new TuEditTurnAndCutOption();

		// 是否开启滤镜支持 (默认: 关闭)
		option.setEnableFilters(true);
		// 开启用户滤镜历史记录
		option.setEnableFiltersHistory(true);

		// 需要裁剪的长宽
		option.setCutSize(new TuSdkSize(640, 640));

		// 是否在控制器结束后自动删除临时文件
		option.setAutoRemoveTemp(true);

		// 是否显示处理结果预览图 (默认：关闭，调试时可以开启)
		option.setShowResultPreview(true);

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
			// 开启图片编辑组件 (裁剪)
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
	public void onTuEditTurnAndCutFragmentEdited(
			TuEditTurnAndCutFragment fragment, TuSdkResult result)
	{
		if (!fragment.isShowResultPreview())
		{
			fragment.hubDismissRightNow();
			fragment.dismissActivityWithAnim();
		}
		TLog.d("onTuEditTurnAndCutFragmentEdited: %s", result);
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
	public boolean onTuEditTurnAndCutFragmentEditedAsync(
			TuEditTurnAndCutFragment fragment, TuSdkResult result)
	{
		TLog.d("onTuEditTurnAndCutFragmentEditedAsync: %s", result);
		return false;
	}

	@Override
	public void onComponentError(TuFragment fragment, TuSdkResult result,
			Error error)
	{
		TLog.d("onComponentError: fragment - %s, result - %s, error - %s",
				fragment, result, error);
	}
}
