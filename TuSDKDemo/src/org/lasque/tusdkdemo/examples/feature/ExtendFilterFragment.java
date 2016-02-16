/** 
 * TuSdkDemo
 * ExtendFilterFragment.java
 *
 * @author 		Yanlin
 * @Date 		2016-2-2 下午5:10:30 
 * @Copyright 	(c) 2015 tusdk.com. All rights reserved.
 * 
 */
package org.lasque.tusdkdemo.examples.feature;

import org.lasque.tusdk.impl.components.filter.TuEditFilterFragment;

/**
 * 扩展滤镜 Fragment
 * 
 * ExtendFilterFragment.java
 *
 * @author Yanlin
 *
 */
public class ExtendFilterFragment extends TuEditFilterFragment
{
	/**
	 * 重写底部关闭按钮的行为
	 */
	@Override
	protected void handleBackButton() 
	{
		this.getActivity().finish();
	}
}
