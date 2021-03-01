/** 
 * TuSDK.Face
 * TuEditFaceDetectionImageFragment.java
 *
 * @author 		Clear
 * @Date 		2016-3-7 下午12:22:43 
 * @Copyright 	(c) 2016 tusdk.com. All rights reserved.
 * 
 */
package org.lasque.tusdkdemo.examples.face;

import org.lasque.tusdkpulse.core.TuSdkContext;
import org.lasque.tusdkpulse.core.TuSdkResult;
import org.lasque.tusdkpulse.core.struct.TuSdkSize;
import org.lasque.tusdkpulse.core.utils.RectHelper;
import org.lasque.tusdkpulse.core.utils.TLog;
import org.lasque.tusdkpulse.core.utils.ThreadHelper;
import org.lasque.tusdkpulse.core.view.widget.TuSdkNavigatorBar;
import org.lasque.tusdkpulse.core.view.widget.TuSdkNavigatorBar.NavigatorBarButtonInterface;
import org.lasque.tusdkpulse.impl.activity.TuImageResultFragment;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * 图片头像检测
 * 
 * @author Clear
 */
public class TuEditFaceDetectionImageFragment extends TuImageResultFragment
{
	/** 布局ID */
	public static int getLayoutId()
	{
		return TuSdkContext.getLayoutResId("tusdk_impl_component_edit_face_detection_image_fragment");
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

	@Override
	protected void notifyProcessing(TuSdkResult result)
	{

	}

	@Override
	protected boolean asyncNotifyProcessing(TuSdkResult result)
	{
		return false;
	}

	/*************************** view *******************************/
	/** 图片包装视图 */
	private RelativeLayout mImageWrapView;
	/** 图片视图 */
	private ImageView mImageView;

	/** 图片包装视图 */
	public RelativeLayout getImageWrapView()
	{
		if (mImageWrapView == null)
		{
			mImageWrapView = this.getViewById("lsq_imageWrapView");
		}
		return mImageWrapView;
	}

	/** 图片视图 */
	public ImageView getImageView()
	{
		if (mImageView == null) mImageView = this.getViewById("lsq_imageView");
		return mImageView;
	}

	@Override
	protected void loadView(ViewGroup view)
	{
		getImageWrapView();
		getImageView();
	}

	@Override
	protected void navigatorBarLoaded(TuSdkNavigatorBar navigatorBar)
	{
		super.navigatorBarLoaded(navigatorBar);
		this.setTitle(this.getResString("lsq_face_detection_title"));

		this.setNavLeftButton(this.getResString("lsq_nav_back"));
	}

	/**
	 * 取消按钮
	 */
	@Override
	public void navigatorBarLeftAction(NavigatorBarButtonInterface button)
	{
		this.navigatorBarBackAction(null);
	}

	@Override
	protected void viewDidLoad(ViewGroup view)
	{
		this.loadImageWithThread();
	}

	/** 异步加载图片完成 */
	@Override
	protected void asyncLoadImageCompleted(final Bitmap image)
	{
		super.asyncLoadImageCompleted(image);
		if (image == null) return;

		this.setImage(image);

		if (this.getImageView() != null)
		{
			this.getImageView().setImageBitmap(image);
		}

		ThreadHelper.runThread(new Runnable()
		{
			@Override
			public void run()
			{
				testFaceDetector(image);
			}
		});
	}

	// 测试脸部识别
	private void testFaceDetector(Bitmap image)
	{
	}
}
