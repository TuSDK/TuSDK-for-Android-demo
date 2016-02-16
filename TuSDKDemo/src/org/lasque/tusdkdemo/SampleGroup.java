/** 
 * TuSdkDemo
 * SimpleGroup.java
 *
 * @author 		Clear
 * @Date 		2015-4-21 下午1:00:42 
 * @Copyright 	(c) 2015 tusdk.com. All rights reserved.
 * 
 */
package org.lasque.tusdkdemo;

import java.util.ArrayList;
import java.util.List;

/**
 * 范例分组
 * 
 * @author Clear
 */
public class SampleGroup
{
	public enum GroupType
	{
		/**
		 * 套件
		 */
		SuiteSample,
		/**
		 * 视图控件类
		 */
		ComponentSample,
		/**
		 * 组件示例
		 */
		FeatureSample,
		/**
		 * 自定义UI示例
		 */
		UISample,
		/**
		 * API示例
		 */
		APISample
	}
	
	/** 范例分组头部信息 */
	public static class GroupHeader
	{
		/** 分组ID */
		public GroupType groupId;
		/** 标题资源ID */
		public int titleResId;
		/** 数据列表 */
		public List<SampleBase> datas;

		/**
		 * 范例分组头部信息
		 * 
		 * @param groupId
		 *            分组ID
		 * @param titleResId
		 *            标题资源ID
		 */
		public GroupHeader(GroupType groupId, int titleResId)
		{
			this.datas = new ArrayList<SampleBase>();
			this.groupId = groupId;
			this.titleResId = titleResId;
		}
	}

	/** 范例分组头部信息列表 */
	public final List<GroupHeader> headers;

	/** 范例分组 */
	public SampleGroup()
	{
		// 范例分组头部信息列表
		headers = new ArrayList<GroupHeader>();
		
		
		// 套件
		headers.add(new GroupHeader(GroupType.SuiteSample, R.string.sample_group_suite));
				
		// 常用组件
		headers.add(new GroupHeader(GroupType.ComponentSample, R.string.sample_group_comp));
		
		// 功能范例
		headers.add(new GroupHeader(GroupType.FeatureSample, R.string.sample_group_examples));

		// 修改界面
		headers.add(new GroupHeader(GroupType.UISample, R.string.sample_group_extend));

		// API
		headers.add(new GroupHeader(GroupType.APISample, R.string.sample_group_define));
	}

	/** 添加范例 */
	public void appendSample(SampleBase sample)
	{
		if (sample == null) return;

		for (GroupHeader header : headers)
		{
			if (header.groupId == sample.groupId)
			{
				header.datas.add(sample);
			}
		}
	}
	
	public void appendSample(GroupType groupId, int titleResId, Class<?> clazz)
	{
		SampleActivityBase sample = new SampleActivityBase(groupId, titleResId);
		sample.activityClazz = clazz;
		
		appendSample(sample);
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