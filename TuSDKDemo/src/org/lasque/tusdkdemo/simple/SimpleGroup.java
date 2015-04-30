/** 
 * TuSdkDemo
 * SimpleGroup.java
 *
 * @author 		Clear
 * @Date 		2015-4-21 下午1:00:42 
 * @Copyright 	(c) 2015 Lasque. All rights reserved.
 * 
 */
package org.lasque.tusdkdemo.simple;

import java.util.ArrayList;
import java.util.List;

import org.lasque.tusdkdemo.R;

/**
 * 范例分组
 * 
 * @author Clear
 */
public class SimpleGroup
{
	/**
	 * 范例分组头部信息
	 * 
	 * @author Clear
	 */
	public static class GroupHeader
	{
		/**
		 * 分组ID
		 */
		public int groupId;

		/**
		 * 标题资源ID
		 */
		public int titleResId;

		/**
		 * 数据列表
		 */
		public List<SimpleBase> datas;

		/**
		 * 范例分组头部信息
		 * 
		 * @param groupId
		 *            分组ID
		 * @param titleResId
		 *            标题资源ID
		 */
		public GroupHeader(int groupId, int titleResId)
		{
			this.datas = new ArrayList<SimpleBase>();
			this.groupId = groupId;
			this.titleResId = titleResId;
		}
	}

	/**
	 * 范例分组头部信息列表
	 */
	public final List<GroupHeader> headers;

	/**
	 * 范例分组
	 */
	public SimpleGroup()
	{
		// 范例分组头部信息列表
		headers = new ArrayList<GroupHeader>();

		// 基础组件分组
		headers.add(new GroupHeader(1, R.string.simple_group_comp));

		// 高级组件分组
		headers.add(new GroupHeader(2, R.string.simple_group_adv_comp));

		// 自定义组件分组
		headers.add(new GroupHeader(4, R.string.simple_group_define));
	}

	/**
	 * 添加范例
	 * 
	 * @param simple
	 */
	public void appendSimple(SimpleBase simple)
	{
		if (simple == null) return;

		for (GroupHeader header : headers)
		{
			if (header.groupId == simple.groupId)
			{
				header.datas.add(simple);
			}
		}
	}

	/**
	 * 获取范例分组头部信息
	 * 
	 * @param index
	 *            索引
	 * @return
	 */
	public GroupHeader getHeader(int index)
	{
		if (this.headers == null || this.headers.size() <= index) return null;
		return this.headers.get(index);
	}
}
