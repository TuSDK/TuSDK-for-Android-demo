/** 
 * TuSdkDemo
 * DemoListView.java
 *
 * @author 		Clear
 * @Date 		2015-4-21 下午2:11:42 
 * @Copyright 	(c) 2015 tusdk.com. All rights reserved.
 * 
 */
package org.lasque.tusdkdemo.view;

import java.util.ArrayList;
import java.util.List;

import org.lasque.tusdkpulse.core.view.listview.TuSdkCellViewInterface;
import org.lasque.tusdkpulse.core.view.listview.TuSdkIndexPath;
import org.lasque.tusdkpulse.core.view.listview.TuSdkIndexPath.TuSdkDataSource;
import org.lasque.tusdkpulse.impl.view.widget.listview.TuGroupListView;
import org.lasque.tusdkdemo.SampleBase;
import org.lasque.tusdkdemo.SampleGroup;
import org.lasque.tusdkdemo.SampleGroup.GroupHeader;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * 范例列表视图
 * 
 * @author Clear
 */
public class DemoListView extends TuGroupListView<SampleBase, DemoListCell, GroupHeader, DemoListHeader>
{
	/** 范例列表行点击动作 */
	public enum DemoListItemAction
	{
		/** 选中 */
		ActionSelected,
		/** 配置 */
		ActionConfig,
	}

	/** 范例列表视图委托 */
	public interface DemoListViewDelegate
	{
		/**
		 * 选中范例
		 * 
		 * @param view
		 *            范例列表视图
		 * @param simple
		 *            范例
		 * @param action
		 *            范例列表行点击动作
		 */
		void onDemoListViewSelected(DemoListView view, SampleBase simple, DemoListItemAction action);
	}

	public DemoListView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	public DemoListView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public DemoListView(Context context)
	{
		super(context);
	}

	/** 范例列表视图委托 */
	private DemoListViewDelegate mSimpleDelegate;

	/** 范例列表视图委托 */
	public DemoListViewDelegate getSimpleDelegate()
	{
		return mSimpleDelegate;
	}

	/** 范例列表视图委托 */
	public void setSimpleDelegate(DemoListViewDelegate mDelegate)
	{
		this.mSimpleDelegate = mDelegate;
	}

	@Override
	protected void initView()
	{
		super.initView();
		// 设置行视图布局ID
		this.setCellLayoutId(DemoListCell.getLayoutId());
		// 分组头部视图布局ID
		this.setHeaderLayoutId(DemoListHeader.getLayoutId());
		// 设置点击动作
		this.setItemClickListener(new DemoListViewItemClick());
	}

	@Override
	protected void onGroupListViewCreated(DemoListCell view, TuSdkIndexPath indexPath)
	{

	}

	@Override
	protected void onGroupListHeaderCreated(DemoListHeader view, TuSdkIndexPath indexPath)
	{

	}

	/** 加载范例列表 */
	public void loadSimples(SampleGroup group)
	{
		this.setDataSource(new SimpleGroupDataSource(group));
	}

	/** 选中一个示例 */
	private void onSelectedItem(SampleBase itemData)
	{
		if (this.getSimpleDelegate() == null || itemData == null) return;
		this.getSimpleDelegate().onDemoListViewSelected(this, itemData, DemoListItemAction.ActionSelected);
	}

	/** 范例列表行点击动作 */
	private class DemoListViewItemClick implements GroupListViewItemClickListener<SampleBase, DemoListCell>
	{
		@Override
		public void onGroupItemClick(SampleBase itemData, DemoListCell itemView, TuSdkIndexPath indexPath)
		{
			onSelectedItem(itemData);
		}
	}

	/** 数组列表数据源 */
	private class SimpleGroupDataSource implements TuSdkDataSource
	{
		/** 索引列表 */
		private List<TuSdkIndexPath> mIndexPaths;

		/** 范例分组 */
		private SampleGroup mGroup;

		private int mTotal;

		/**
		 * 数组列表数据源
		 * 
		 * @param group
		 *            范例分组
		 */
		public SimpleGroupDataSource(SampleGroup group)
		{
			this.splitDatas(group);
		}

		/** 分组数据 */
		private void splitDatas(SampleGroup group)
		{
			if (group == null || group.headers == null) return;
			mGroup = group;

			List<TuSdkIndexPath> indexPaths = new ArrayList<TuSdkIndexPath>();

			mTotal = 0;
			int sectionIndex = 0;
			for (GroupHeader header : group.headers)
			{
				if (header.datas == null) continue;

				indexPaths.add(new TuSdkIndexPath(sectionIndex, -1, 1));
				mTotal += header.datas.size();

				for (int i = 0, j = header.datas.size(); i < j; i++)
				{
					indexPaths.add(new TuSdkIndexPath(sectionIndex, i, 0));
				}

				sectionIndex++;
			}

			this.mIndexPaths = indexPaths;
		}

		/** 列表视图分组索引 */
		public List<TuSdkIndexPath> getIndexPaths()
		{
			if (mIndexPaths == null)
			{
				mIndexPaths = new ArrayList<TuSdkIndexPath>(0);
			}
			return mIndexPaths;
		}

		/** 索引 */
		public TuSdkIndexPath getIndexPath(int index)
		{
			if (mIndexPaths == null || index >= mIndexPaths.size()) return null;
			return mIndexPaths.get(index);
		}

		/** 视图类型总数 */
		public int viewTypes()
		{
			return 2;
		}

		/** 分组总数 */
		public int sectionCount()
		{
			if (mGroup != null && mGroup.headers != null)
			{
				return mGroup.headers.size();
			}
			return 1;
		}

		/**
		 * 行总数
		 * 
		 * @param section
		 *            分组索引
		 * @return
		 */
		public int rowCount(int section)
		{
			if (mGroup == null) return 0;

			GroupHeader header = mGroup.getHeader(section);
			if (header == null || header.datas == null) return 0;
			return header.datas.size();
		}

		/** 数据总数 */
		public int count()
		{
			return mTotal;
		}

		/** 视图创建 */
		public void onViewBinded(TuSdkIndexPath indexPath, View view)
		{
			if (!(view instanceof TuSdkCellViewInterface)) return;

			Object mode = this.getItem(indexPath);

			if (view instanceof DemoListCell)
			{
				((DemoListCell) view).setModel((SampleBase) mode);
			}
			else if (view instanceof DemoListHeader)
			{
				((DemoListHeader) view).setModel((GroupHeader) mode);
			}
		}

		/** 获取数据 */
		public Object getItem(TuSdkIndexPath indexPath)
		{
			if (indexPath == null || mGroup == null) return 0;

			GroupHeader header = mGroup.headers.get(indexPath.section);

			if (header == null) return null;

			if (indexPath.viewType == 1)
			{
				return header;
			}
			else if (indexPath.viewType == 0 && header.datas != null && header.datas.size() > indexPath.row)
			{
				return header.datas.get(indexPath.row);
			}
			return null;
		}
	}
}