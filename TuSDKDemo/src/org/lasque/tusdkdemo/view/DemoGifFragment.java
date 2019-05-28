/** 
 * TuSdkDemo
 * GifImageViewSimple.java
 *
 * @author 		Yanlin
 * @Date 		2015-12-29 下午3:38:04 
 * @Copyright 	(c) 2015 tusdk.com. All rights reserved.
 * 
 */
package org.lasque.tusdkdemo.view;

import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.view.TuSdkViewHelper;
import org.lasque.tusdk.core.view.TuSdkViewHelper.OnSafeClickListener;
import org.lasque.tusdk.core.view.widget.button.TuSdkImageButton;
import org.lasque.tusdk.impl.activity.TuFragment;
import org.lasque.tusdk.impl.view.widget.TuGifView;
import org.lasque.tusdkdemo.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

/**
 * Gif组件示例视图
 * 
 * @author Yanlin
 */
public class DemoGifFragment extends TuFragment implements TuGifView.TuGifViewDelegate
{	
	
	private TuGifView gifView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		this.setRootViewLayoutId(R.layout.demo_view_gif);

		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	@Override
	protected void loadView(ViewGroup view)
	{
		if (gifView == null)
		{
			gifView = this.getViewById(R.id.gifView);
			gifView.setDelegate(this);
			gifView.setOnClickListener(mClickListener);
		}
		
		// 返回按钮
		TuSdkImageButton backBtn = this.getViewById(R.id.lsq_backButton);
		backBtn.setOnClickListener(mClickListener);
	}
	
	@Override
	protected void viewDidLoad(ViewGroup view) 
	{
		// 通过代码创建Gif视图
		
		/*
		TuGifView gif = new TuGifView(this.getActivity());
		
		// 自动播放 (默认: true) 
		gif.setAutoPlay(true);
		
		gif.setImageResource(R.raw.sample_gif_editor);
		// 侦听播放事件
		gif.setDelegate(this);
		gif.setOnClickListener(mClickListener);
		
		TuSdkGifDrawable drawable = (TuSdkGifDrawable)gif.getDrawable();
		
		if (drawable != null)
		{
			TLog.d("Gif size: {%d, %d}", drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
			
			// 设置播放次数
			drawable.setLoopCount(3);
		}
		
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.CENTER_IN_PARENT);
		gif.setLayoutParams(params);

		view.addView(gif);
		*/
	}
	
	@Override
	public void onGifAnimationComplete(int loopNumber)
	{
		TLog.d("Gif 动画已播放次数：%d", loopNumber);
	}
	
	@Override
	public void onDestroyView()
	{
		super.onDestroyView();
		
		if (gifView != null)
		{
			gifView.dispose();
		}
	}
	
	/** 按钮点击事件 */
	private OnClickListener mClickListener = new OnSafeClickListener()
	{
		@Override
		public void onSafeClick(View v)
		{
			switch (v.getId())
			{
				// 图片
				case R.id.gifView:
					
					pauseOrStart();
					break;
				case R.id.lsq_backButton:
					dismissActivityWithAnim();
					break;
			}
		}
	};
	
	/** 暂停或继续 */
	private void pauseOrStart()
	{
		if (gifView == null) return;
		
		if (gifView.isRunning())
		{
			gifView.pause();
		}
		else
		{
			gifView.start();
		}
	}
}
