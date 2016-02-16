/** 
 * TuSdkDemo
 * GifImageViewSimple.java
 *
 * @author 		Yanlin
 * @Date 		2015-12-29 下午3:38:04 
 * @Copyright 	(c) 2015 tusdk.com. All rights reserved.
 * 
 */
package org.lasque.tusdkdemo.examples.component;

import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.view.widget.button.TuSdkImageButton;
import org.lasque.tusdk.impl.view.widget.TuGifView;
import org.lasque.tusdkdemo.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class GifImageViewActivity extends Activity implements TuGifView.TuGifViewDelegate
{	
	
	private TuGifView gifView;
	
	private TuSdkImageButton backButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
	    super.onCreate(savedInstanceState); 
	   
	    this.setContentView(R.layout.demo_view_gif);
	    
	    initView();
	}
	
	private void initView()
	{
		gifView = (TuGifView) this.findViewById(R.id.gifView);
		gifView.setDelegate(this);
		gifView.setOnClickListener(mClickListener);
		
		backButton = (TuSdkImageButton) this.findViewById(R.id.backButton);
		backButton.setOnClickListener(mClickListener);
		
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
	
	/** 按钮点击事件 */
	private OnClickListener mClickListener = new View.OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			switch (v.getId())
			{
				// 图片
				case R.id.gifView:
					
					pauseOrStart();
					break;
				case R.id.backButton:
					onBackButton();
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
	
	private void onBackButton()
	{
		finish();
	}

	@Override
	public void onGifAnimationComplete(int loopNumber)
	{
		TLog.d("Gif 动画已播放次数：%d", loopNumber);
	}

}
