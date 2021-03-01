/** 
 * TuSdkDemo
 * ExtendCameraBaseFragment.java
 *
 * @author 		Clear
 * @Date 		2015-5-11 下午3:37:04 
 * @Copyright 	(c) 2015 tusdk.com. All rights reserved.
 * 
 */
package org.lasque.tusdkdemo.examples.ui;

import org.lasque.tusdkpulse.impl.components.camera.TuCameraFragment;
import org.lasque.tusdkpulse.impl.components.edit.TuNormalFilterView;
import org.lasque.tusdkpulse.impl.components.edit.TuNormalFilterView.TuNormalFilterViewDelegate;
import org.lasque.tusdkpulse.modules.view.widget.filter.GroupFilterItem;
import org.lasque.tusdkpulse.modules.view.widget.filter.GroupFilterItem.GroupFilterItemType;
import org.lasque.tusdkdemo.R;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 相机界面自定义组件控制器
 * 
 * @author Clear
 */
public class CustomizedCameraFragment extends TuCameraFragment
{
	/** 布局ID */
	public static int getLayoutId()
	{
		return R.layout.demo_extend_camera_base_fragment;
	}

	/** 相机界面自定义组件控制器 */
	public CustomizedCameraFragment()
	{

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		if (this.getRootViewLayoutId() == 0)
		{
			this.setRootViewLayoutId(getLayoutId());
		}
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	/*************************** view *******************************/

	/** 裁剪与缩放控制器滤镜视图 */
	private TuNormalFilterView mFilterBar;

	/** 裁剪与缩放控制器滤镜视图 */
	public TuNormalFilterView getFilterBar()
	{
		if (mFilterBar == null)
		{
			mFilterBar = this.getViewById(R.id.demo_group_filter_bar);
			if (mFilterBar != null)
			{
				this.configGroupFilterView(mFilterBar);
				// 设置背景透明
				mFilterBar.getGroupFilterBar().setBackgroundColor(Color.TRANSPARENT);
				// 裁剪与缩放控制器滤镜视图委托
				mFilterBar.setDelegate(mFilterViewDelegate);
			}
		}
		return mFilterBar;
	}

	/** 裁剪与缩放控制器滤镜视图委托 */
	private TuNormalFilterViewDelegate mFilterViewDelegate = new TuNormalFilterViewDelegate()
	{
		@Override
		public boolean onTuNormalFilterViewSelected(TuNormalFilterView view, GroupFilterItem itemData)
		{
			if (itemData.type == GroupFilterItemType.TypeFilter)
			{
				handleSwitchFilter(itemData.getFilterCode());
			}
			return true;
		}
	};

	/** 切换滤镜视图显示 */
	@Override
	protected void handleFilterButton()
	{
		if (this.getFilterBar() != null)
		{
			// 切换滤镜栏显示状态
			this.getFilterBar().toggleBarShowState();
		}
	}

	/************************** loadView *****************************/
	@Override
	protected void loadView(ViewGroup view)
	{
		super.loadView(view);
		// 裁剪与缩放控制器滤镜视图
		this.getFilterBar();
	}

	@Override
	protected void viewDidLoad(ViewGroup view)
	{
		super.viewDidLoad(view);

		// 加载滤镜
		if (this.getFilterBar() != null && this.isEnableFilters())
		{
			this.getFilterBar().setDefaultShowState(this.isShowFilterDefault());
			this.getFilterBar().loadFilters();
		}
	}
}