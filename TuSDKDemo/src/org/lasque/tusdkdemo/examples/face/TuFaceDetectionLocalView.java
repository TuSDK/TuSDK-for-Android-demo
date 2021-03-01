/** 
 * TuSdkDemo
 * TuFaceDetectionLocalView.java
 *
 * @author 		Clear
 * @Date 		2016-7-24 下午2:55:10 
 * @Copyright 	(c) 2016 tusdk.com. All rights reserved.
 * 
 */
package org.lasque.tusdkdemo.examples.face;

import java.util.ArrayList;
import java.util.List;

import org.lasque.tusdkpulse.core.TuSdkContext;
import org.lasque.tusdkpulse.core.struct.TuSdkSize;
import org.lasque.tusdkpulse.core.utils.TLog;
import org.lasque.tusdkpulse.core.utils.TuSdkDate;
import org.lasque.tusdkpulse.core.utils.hardware.CameraConfigs.*;
import org.lasque.tusdkpulse.cx.hardware.camera.TuCamera;
import org.lasque.tusdkpulse.impl.components.camera.TuFocusTouchView;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * @TODO 需要底层实现
 * 本地检脸标点
 * @author Clear
 */

@Deprecated
public class TuFaceDetectionLocalView extends TuFocusTouchView
{
	/** 布局ID */
	public static int getLayoutId()
	{
		return TuSdkContext.getLayoutResId("tusdk_impl_component_edit_face_detection_local_view");
	}

	public TuFaceDetectionLocalView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	public TuFaceDetectionLocalView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public TuFaceDetectionLocalView(Context context)
	{
		super(context);
	}

	private TuCamera mCamera;
	//private SelesOffscreen mOutput;
	private final List<View> markViews = new ArrayList<View>();

	private ImageView mImageView;

	@Override
	public void loadView()
	{
		super.loadView();

		mImageView = this.getViewById("imageView1");
		this.showViewIn(this.mImageView, false);
	}

	@Override
	public void setCamera(TuCamera camera)
	{
		mCamera = camera;

		super.setCamera(camera);
		if (camera != null)
		{
//			camera.removeTarget(mOutput);
//			mOutput = new SelesScreenShot();
//			// 使用缩略图，提升运算性能
//			//int scale = Math.min(450, TuSdkContext.getScreenSize().maxSide());
//			int scale = TuSdkContext.getScreenSize().maxSide() / 4;
//			mOutput.forceProcessingAtSizeRespectingAspectRatio(TuSdkSize.create(scale));
//			mOutput.setDelegate(mOutputDelegate);
//			camera.addTarget(mOutput);
			// 本地检脸，直接开启
			//mOutput.resetEnabled();
		}
	}

	@Override
	public void cameraStateChanged(TuCamera camera, CameraState state)
	{
		super.cameraStateChanged(camera, state);
		if (state == CameraState.START)
		{
			//if (mOutput != null) mOutput.resetEnabled();
		}
	}

	/** 创建脸部定位视图 */
	@Override
	public View buildFaceDetectionView()
	{
		View view = this.buildView(this.getFaceDetectionLayoutID(), this);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		view.setLayoutParams(params);
		return view;
	}

	@Override
	protected void hiddenFaceViews()
	{
		super.hiddenFaceViews();

		for (View view : markViews)
			this.showView(view, false);
	}

	/*创建标点视图*/
	private void buildMarkViews(int index, PointF point, RectF rect)
	{

		View mark = null;
		RelativeLayout.LayoutParams params = null;
		if (markViews.size() > index)
		{
			mark = markViews.get(index);
			params = (RelativeLayout.LayoutParams) mark.getLayoutParams();
			this.showView(mark, true);
		}
		else
		{
			mark = new View(this.getContext());
			mark.setBackgroundColor(Color.argb(255, 0, 255, 0));
			params = new RelativeLayout.LayoutParams(8, 8);
			markViews.add(mark);
			this.addView(mark);
		}

		params.leftMargin = (int) (point.x * rect.width() - rect.left) - 4;
		params.topMargin = (int) (point.y * rect.height() - rect.top) - 4;
		mark.setLayoutParams(params);
	}
}
