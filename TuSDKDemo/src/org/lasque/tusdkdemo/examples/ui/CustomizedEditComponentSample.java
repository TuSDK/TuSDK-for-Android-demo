/** 
 * TuSdkDemo
 * EditComponentSimple.java
 *
 * @author 		Clear
 * @Date 		2015-4-21 下午1:33:49 
 * @Copyright 	(c) 2015 tusdk.com. All rights reserved.
 * 
 */
package org.lasque.tusdkdemo.examples.ui;

import org.lasque.tusdkpulse.core.TuSdkResult;
import org.lasque.tusdkpulse.core.utils.TLog;
import org.lasque.tusdkpulse.core.utils.image.BitmapHelper;
import org.lasque.tusdkpulse.impl.activity.TuFragment;
import org.lasque.tusdkpulse.impl.components.edit.TuEditTurnAndCutFragment;
import org.lasque.tusdkpulse.impl.components.edit.TuEditTurnAndCutFragment.TuEditTurnAndCutFragmentDelegate;
import org.lasque.tusdkpulse.impl.components.edit.TuEditTurnAndCutOption;
import org.lasque.tusdkpulse.modules.components.TuSdkHelperComponent;
import org.lasque.tusdkdemo.R;
import org.lasque.tusdkdemo.SampleBase;
import org.lasque.tusdkdemo.SampleGroup.GroupType;

import android.app.Activity;

/**
 * 裁切组件自定义界面范例
 * 
 * @author Clear
 */
public class CustomizedEditComponentSample extends SampleBase implements TuEditTurnAndCutFragmentDelegate
{
	/** 裁切组件自定义界面范例 */
	public CustomizedEditComponentSample()
	{
		super(GroupType.UISample, R.string.sample_ui_EditComponent);
	}

	/**
	 * 裁切组件自定义界面范例说明
	 * 
	 * 在SDK中，每个界面组件对应一个Fragment对象，Fragment对象在运行期间加载layout文件， 每个Fragment对象的layout文件是可以修改的。
	 * 
	 * 示例代码：
	 * 
	 * TuEditTurnAndCutOption option = new TuEditTurnAndCutOption();
	 * // 修改默认的Fragment类型
	 * option.setComponentClazz(ExtendEditTurnAndCutFragment.class);
	 * // 设置根视图布局资源ID
	 * option.setRootViewLayoutId(R.layout.demo_extend_edit_and_cut_fragment);	
	 * 
	 * 依靠这两点，可以在layout文件中修改界面，然后在自定义的Fragment中处理界面交互，从而实现修改界面的目的，所有的组件都可以用类似的方法来修改界面。
	 * 
	 */
	@Override
	public void showSample(Activity activity)
	{
		if (activity == null) return;

		// 组件选项配置
		// @see-http://tusdk.com/docs/android/image/api/org/lasque/tusdk/impl/components/edit/TuEditTurnAndCutOption.html
		TuEditTurnAndCutOption option = new TuEditTurnAndCutOption();
		// 控制器类型
		option.setComponentClazz(CustomizedEditTurnAndCutFragment.class);

		// 设置根视图布局资源ID
		option.setRootViewLayoutId(R.layout.demo_extend_edit_and_cut_fragment);

		// 保存到临时文件 (默认不保存, 当设置为true时, TuSdkResult.imageFile, 处理完成后将自动清理原始图片)
		// option.setSaveToTemp(false);

		// 保存到系统相册 (默认不保存, 当设置为true时, TuSdkResult.sqlInfo, 处理完成后将自动清理原始图片)
		// option.setSaveToAlbum(true);

		// 保存到系统相册的相册名称
		// option.setSaveToAlbumName("TuSdk");

		// 照片输出压缩率 (默认:90，0-100 如果设置为0 将保存为PNG格式)
		// option.setOutputCompress(90);

		// 是否开启滤镜支持 (默认: 关闭)
		option.setEnableFilters(true);

		// 开启用户滤镜历史记录
		option.setEnableFiltersHistory(true);

		// 开启在线滤镜
		option.setEnableOnlineFilter(true);

		// 显示滤镜标题视图
		option.setDisplayFiltersSubtitles(true);

		// 需要显示的滤镜名称列表 (如果为空将显示所有自定义滤镜)
		// option.setFilterGroup(new ArrayList<String>());

		// 需要裁剪的长宽
		// option.setCutSize(new TuSdkSize(640, 640));

		// 是否显示处理结果预览图 (默认：关闭，调试时可以开启)
		// option.setShowResultPreview(false);

		// 滤镜组行视图宽度 (单位:DP)
		// option.setGroupFilterCellWidthDP(60);

		// 滤镜组选择栏高度 (单位:DP)
		// option.setFilterBarHeightDP(80);

		// 滤镜分组列表行视图布局资源ID (默认:
		// tusdk_impl_component_widget_group_filter_group_view，如需自定义请继承自
		// GroupFilterGroupView)
		// option.setGroupTableCellLayoutId(GroupFilterGroupView.getLayoutId());

		// 滤镜列表行视图布局资源ID (默认:
		// tusdk_impl_component_widget_group_filter_item_view，如需自定义请继承自
		// GroupFilterItemView)
		// option.setFilterTableCellLayoutId(GroupFilterItemView.getLayoutId());

		// 是否在控制器结束后自动删除临时文件
		option.setAutoRemoveTemp(true);

		// 是否渲染滤镜封面 (使用设置的滤镜直接渲染，需要拥有滤镜列表封面设置权限，请访问TuSDK.com控制台)
		// option.setRenderFilterThumb(true);

		TuEditTurnAndCutFragment fragment = option.fragment();

		// 输入的图片对象 (处理优先级: Image > TempFilePath > ImageSqlInfo)
		fragment.setImage(BitmapHelper.getRawBitmap(activity, R.raw.sample_photo));

		// 输入的临时文件目录 (处理优先级: Image > TempFilePath > ImageSqlInfo)
		// editFragment.setTempFilePath(result.imageFile);

		// 输入的相册图片对象 (处理优先级: Image > TempFilePath > ImageSqlInfo)
		// editFragment.setImageSqlInfo(result.imageSqlInfo);

		fragment.setDelegate(this);

		// see-http://tusdk.com/docs/android/image/api/org/lasque/tusdk/modules/components/TuSdkHelperComponent.html
		this.componentHelper = new TuSdkHelperComponent(activity);
		// 开启相机
		this.componentHelper.presentModalNavigationActivity(fragment);
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