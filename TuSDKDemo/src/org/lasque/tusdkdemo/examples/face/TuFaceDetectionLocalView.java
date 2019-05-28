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

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.face.FaceAligment;
import org.lasque.tusdk.core.face.TuSdkFaceDetector;
import org.lasque.tusdk.core.seles.output.SelesScreenShot;
import org.lasque.tusdk.core.seles.output.SelesScreenShot.SelesScreenShotDelegate;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.ThreadHelper;
import org.lasque.tusdk.core.utils.TuSdkDate;
import org.lasque.tusdk.core.utils.hardware.TuSdkStillCameraAdapter;
import org.lasque.tusdk.core.utils.hardware.TuSdkStillCameraAdapter.CameraState;
import org.lasque.tusdk.core.utils.hardware.TuSdkStillCameraInterface;
import org.lasque.tusdk.impl.components.camera.TuFocusTouchView;

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
 * 本地检脸标点
 * 
 * @author Clear
 */
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

	private TuSdkStillCameraAdapter<?> mAdapter;
	//private SelesOffscreen mOutput;
	private SelesScreenShot mOutput;
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
	public void setCamera(TuSdkStillCameraInterface camera)
	{
		mAdapter = camera.adapter();

		super.setCamera(camera);
		if (camera != null)
		{
			camera.removeTarget(mOutput);
			mOutput = new SelesScreenShot();
			// 使用缩略图，提升运算性能
			//int scale = Math.min(450, TuSdkContext.getScreenSize().maxSide());
			int scale = TuSdkContext.getScreenSize().maxSide() / 4;
			mOutput.forceProcessingAtSizeRespectingAspectRatio(TuSdkSize.create(scale));
			mOutput.setDelegate(mOutputDelegate);
			camera.addTarget(mOutput);
			// 本地检脸，直接开启
			//mOutput.resetEnabled();
		}
	}

	@Override
	public void cameraStateChanged(TuSdkStillCameraInterface camera, CameraState state)
	{
		super.cameraStateChanged(camera, state);
		if (state == CameraState.StateStarted)
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

	/*异步线程离屏输出 (适用于实时获取视频)委托*/
	private SelesScreenShotDelegate mOutputDelegate = new SelesScreenShotDelegate()
	{
		@Override
		public boolean onFrameRendered(SelesScreenShot output)
		{
			TuSdkDate date = TuSdkDate.create();
			
			final TuSdkSize size = output.outputFrameSize();
			IntBuffer buffer = output.renderedBuffer();
			if (!size.isSize() || buffer == null) return true;

			
			long s1 = date.diffOfMillis();
			
			// 获得手机角度 angle
			// 设备方向
			int degrees = -mAdapter.getDeviceAngle();
			double angle = Math.PI / 180 * degrees;

			// final Bitmap mBitmap = Bitmap.createBitmap(size.width,
			// size.height, Bitmap.Config.ARGB_8888);
			// mBitmap.copyPixelsFromBuffer(buffer);

			final FaceAligment[] aligments = TuSdkFaceDetector.markFaceVideo(size.width, size.height, angle, buffer.array());

			long s2 = date.diffOfMillis();
			TLog.d("TuSdkFaceDetector: [%s + %s = %s] | %s | %s", s1, s2 - s1, s2, size.width, size.height);

			// 回到主线程
			ThreadHelper.post(new Runnable()
			{
				@Override
				public void run()
				{
					onFaceAligmented(aligments, size);
				}
			});
			return true;
		}
	};

	/*对人脸标点完成*/
	private void onFaceAligmented(FaceAligment[] faces, TuSdkSize size)
	{
		this.hiddenFaceViews();

		if (faces == null || faces.length == 0) return;
		TLog.d("onFaceAligmented: %s", faces.length);

		if (mFaceViews.size() < faces.length)
		{
			for (int i = 0, j = faces.length - mFaceViews.size(); i < j; i++)
			{
				View view = this.buildFaceDetectionView();
				if (view == null) continue;
				this.showView(view, false);
				this.addView(view);
				mFaceViews.add(view);
			}
		}

		RectF imageRect = this.makeRectRelativeImage(size);

		int m = 0;
		for (int i = 0, j = faces.length; i < j; i++)
		{
			FaceAligment face = faces[i];

			if (face.getMarks() == null || face.rect == null) continue;

			Rect rect = transforRect(face.rect, imageRect);
			if (rect == null) continue;

			View view = mFaceViews.get(i);
			this.setRect(view, rect);
			this.showView(view, true);

			for (PointF point : face.getMarks())
			{
				buildMarkViews(m, point, imageRect);
				m++;
			}
		}
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
