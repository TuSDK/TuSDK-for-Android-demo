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

import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.TuSdkResult;
import org.lasque.tusdk.core.face.FaceAligment;
import org.lasque.tusdk.core.face.TuSdkFaceDetector;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.RectHelper;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.ThreadHelper;
import org.lasque.tusdk.core.view.widget.TuSdkNavigatorBar;
import org.lasque.tusdk.core.view.widget.TuSdkNavigatorBar.NavigatorBarButtonInterface;
import org.lasque.tusdk.impl.activity.TuImageResultFragment;

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
		final FaceAligment[] faces = TuSdkFaceDetector.markFace(image, 1);

		if (faces != null)
		{
			TuSdkSize imgSize = TuSdkSize.create(image);
			Rect midRect = RectHelper.makeRectWithAspectRatioInsideRect(imgSize, new Rect(0, 0, this.getImageWrapView().getWidth(), this.getImageWrapView()
					.getHeight()));

			// 对齐到图片
			for (FaceAligment faceAligment : faces)
			{
				// faceAligment.scale(scale);
				faceAligment.rect.left = faceAligment.rect.left * midRect.width() + midRect.left;
				faceAligment.rect.top = faceAligment.rect.top * midRect.height() + midRect.top;
				faceAligment.rect.right = faceAligment.rect.right * midRect.width() + midRect.left;
				faceAligment.rect.bottom = faceAligment.rect.bottom * midRect.height() + midRect.top;

				if (faceAligment.getMarks() != null)
				{
					for (PointF point : faceAligment.getMarks())
					{
						point.x = point.x * midRect.width() + midRect.left;
						point.y = point.y * midRect.height() + midRect.top;
					}
				}
			}
		}

		ThreadHelper.post(new Runnable()
		{
			@Override
			public void run()
			{
				marks(faces);
			}
		});
	}

	private void marks(FaceAligment[] faces)
	{
		if (faces == null) return;

		for (FaceAligment face : faces)
		{
			View rectView = new View(getActivity());
			rectView.setBackgroundColor(Color.argb(128, 128, 128, 128));

			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int) face.rect.width(), (int) face.rect.height());
			params.leftMargin = (int) face.rect.left;
			params.topMargin = (int) face.rect.top;

			RelativeLayout rootView = getImageWrapView();
			rootView.addView(rectView, params);

			if (face.getMarks() == null) continue;
			for (PointF point : face.getMarks())
			{
				View mark = new View(getActivity());
				mark.setBackgroundColor(Color.argb(255, 0, 255, 0));
				params = new RelativeLayout.LayoutParams(8, 8);
				params.leftMargin = (int) point.x - 4;
				params.topMargin = (int) point.y - 4;
				rootView.addView(mark, params);
			}
		}

		TLog.d("numberOfFaceDetected is %s", faces.length);
	}
}
