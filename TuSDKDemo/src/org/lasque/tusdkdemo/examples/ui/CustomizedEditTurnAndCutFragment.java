/** 
 * TuSdkDemo
 * ExtendEditTurnAndCutFragment.java
 *
 * @author 		Yanlin
 * @Date 		2015-10-30 下午4:33:49 
 * @Copyright 	(c) 2015 tusdk.com. All rights reserved.
 * 
 */
package org.lasque.tusdkdemo.examples.ui;

import org.lasque.tusdk.core.view.TuSdkTouchImageViewInterface.LsqImageChangeType;
import org.lasque.tusdk.core.view.TuSdkViewHelper;
import org.lasque.tusdk.core.view.widget.button.TuSdkImageButton;
import org.lasque.tusdk.impl.components.edit.TuEditTurnAndCutFragment;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * 裁切组件自定义范例
 * 
 * @author Yanlin
 */
public class CustomizedEditTurnAndCutFragment extends TuEditTurnAndCutFragment 
{
	/** 右旋转按钮 */
	private TuSdkImageButton mTrunRightButton;
	
	/** 右旋转按钮 */
	public ImageView getTrunRightButton()
	{
		if (mTrunRightButton == null)
		{
			mTrunRightButton = this.getViewById("lsq_trunRightButton");
			if (mTrunRightButton != null)
			{
				mTrunRightButton.setOnClickListener(mButtonClickListener);
			}
		}
		return mTrunRightButton;
	}
	
	/** 视图被加载时调用，可在此处对视图作修改 */
	@Override
	protected void loadView(ViewGroup view)
	{
		super.loadView(view);
		
		this.getTrunRightButton();
	}
	
	/** 分发视图点击事件 */
	@Override
	protected void dispatcherViewClick(View v)
	{
		super.dispatcherViewClick(v);
		
		if (this.equalViewIds(v, this.getTrunRightButton()))
		{
			this.handleTrunRightButton();
		}
	}
	
	/** 右旋转动作 */
	protected void handleTrunRightButton()
	{
		if (this.getImageView() == null || this.getImageView().isInAnimation()) return;

		this.getImageView().changeImageAnimation(LsqImageChangeType.TypeImageChangeTurnRight);
	}
	
	/** 按钮点击事件 */
	private OnClickListener mButtonClickListener = new TuSdkViewHelper.OnSafeClickListener()
	{
		@Override
		public void onSafeClick(View v)
		{
			// 分发视图点击事件
			dispatcherViewClick(v);
		}
	};
}
