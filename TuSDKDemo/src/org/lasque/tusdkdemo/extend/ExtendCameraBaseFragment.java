/** 
 * TuSdkDemo
 * ExtendCameraBaseFragment.java
 *
 * @author 		Clear
 * @Date 		2015-5-11 下午3:37:04 
 * @Copyright 	(c) 2015 Lasque. All rights reserved.
 * 
 */
package org.lasque.tusdkdemo.extend;

import org.lasque.tusdk.impl.components.camera.TuCameraFragment;
import org.lasque.tusdk.impl.components.edit.TuEditTurnAndCutFilterView;
import org.lasque.tusdk.impl.components.edit.TuEditTurnAndCutFilterView.TuEditTurnAndCutFilterViewDelegate;
import org.lasque.tusdk.impl.components.widget.GroupFilterItem;
import org.lasque.tusdk.impl.components.widget.GroupFilterItem.GroupFilterItemType;
import org.lasque.tusdkdemo.R;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 相机组件范例 - 修改界面控制器
 * 
 * @author Clear
 */
public class ExtendCameraBaseFragment extends TuCameraFragment
{
	/**
	 * 布局ID
	 * 
	 * @return
	 */
	public static int getLayoutId()
	{
		return R.layout.demo_extend_camera_base_fragment;
	}

	/**
	 * 相机组件范例 - 修改界面控制器
	 */
	public ExtendCameraBaseFragment()
	{

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		if (this.getRootViewLayoutId() == 0)
		{
			this.setRootViewLayoutId(getLayoutId());
		}
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	/*************************** view *******************************/

	// 裁剪与缩放控制器滤镜视图
	private TuEditTurnAndCutFilterView mFilterBar;

	/**
	 * 裁剪与缩放控制器滤镜视图
	 * 
	 * @return the mFilterBar
	 */
	public TuEditTurnAndCutFilterView getFilterBar()
	{
		if (mFilterBar == null)
		{
			mFilterBar = this.getViewById(R.id.demo_group_filter_bar);
			if (mFilterBar != null)
			{
				// 行视图宽度
				mFilterBar.setGroupFilterCellWidth(this
						.getGroupFilterCellWidth());
				// 滤镜组选择栏高度
				mFilterBar.setFilterBarHeight(this.getFilterBarHeight());
				// 滤镜分组列表行视图布局资源ID
				mFilterBar.setGroupTableCellLayoutId(this
						.getGroupTableCellLayoutId());
				// 滤镜列表行视图布局资源ID
				mFilterBar.setFilterTableCellLayoutId(this
						.getFilterTableCellLayoutId());
				// 指定显示的滤镜组
				mFilterBar.setFilterGroup(this.getFilterGroup());
				// 裁剪与缩放控制器滤镜视图委托
				mFilterBar.setDelegate(mFilterViewDelegate);

				mFilterBar.setSaveLastFilter(this.isSaveLastFilter());
				mFilterBar.setAutoSelectGroupDefaultFilter(this
						.isAutoSelectGroupDefaultFilter());

				// 设置背景透明
				mFilterBar.getGroupFilterBar().setBackgroundColor(
						Color.TRANSPARENT);
			}
		}
		return mFilterBar;
	}

	/**
	 * 裁剪与缩放控制器滤镜视图委托
	 */
	private TuEditTurnAndCutFilterViewDelegate mFilterViewDelegate = new TuEditTurnAndCutFilterViewDelegate()
	{
		@Override
		public boolean onTuEditTurnAndCutFilterSelected(
				TuEditTurnAndCutFilterView view, GroupFilterItem itemData)
		{
			if (itemData.type == GroupFilterItemType.TypeFilter)
			{
				return handleSwitchFilter(itemData.getFilterCode());
			}
			return true;
		}
	};

	/**
	 * 切换滤镜视图显示
	 */
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
