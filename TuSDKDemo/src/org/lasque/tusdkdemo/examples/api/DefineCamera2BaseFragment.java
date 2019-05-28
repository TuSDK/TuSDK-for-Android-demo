/** 
 * TuSdkDemo
 * DefineCamera2BaseFragment.java
 *
 * @author 		Clear
 * @Date 		2015-12-8 上午11:37:38 
 * @Copyright 	(c) 2015 tusdk.com. All rights reserved.
 * 
 */
package org.lasque.tusdkdemo.examples.api;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lasque.tusdk.core.TuSdkResult;
import org.lasque.tusdk.core.seles.output.SelesSmartView;
import org.lasque.tusdk.core.seles.sources.SelesOutInput;
import org.lasque.tusdk.core.seles.sources.SelesVideoCamera2;
import org.lasque.tusdk.core.seles.sources.SelesVideoCamera2Processor;
import org.lasque.tusdk.core.seles.sources.SelesVideoCamera2Processor.SelesVideoCamera2ProcessorEngine;
import org.lasque.tusdk.core.seles.tusdk.FilterLocalPackage;
import org.lasque.tusdk.core.seles.tusdk.FilterOption;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.ContextUtils;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.ThreadHelper;
import org.lasque.tusdk.core.utils.hardware.Camera2Helper;
import org.lasque.tusdk.core.utils.hardware.InterfaceOrientation;
import org.lasque.tusdk.core.utils.image.BitmapHelper;
import org.lasque.tusdk.core.utils.image.ImageOrientation;
import org.lasque.tusdk.core.view.TuSdkViewHelper;
import org.lasque.tusdk.core.view.TuSdkViewHelper.OnSafeClickListener;
import org.lasque.tusdk.impl.activity.TuFragment;
import org.lasque.tusdk.impl.components.camera.TuCameraFilterView;
import org.lasque.tusdk.impl.components.camera.TuCameraFilterView.TuCameraFilterViewDelegate;
import org.lasque.tusdk.modules.view.widget.filter.GroupFilterItem;
import org.lasque.tusdkdemo.R;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.media.ImageReader.OnImageAvailableListener;
import android.media.MediaActionSound;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Range;
import android.util.Size;
import android.view.Surface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

/**
 * Camera2 快速相机范例
 * 
 * @author Clear
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class DefineCamera2BaseFragment extends TuFragment
{
	/** 布局ID */
	public static final int layoutId = R.layout.demo_define_camera_base_fragment;

	public DefineCamera2BaseFragment()
	{
		this.setRootViewLayoutId(layoutId);
	}

	/** 相机视图 */
	private RelativeLayout cameraView;
	/** 取消按钮 */
	private TextView cancelButton;
	/** 闪光灯栏 */
	private LinearLayout flashBar;
	/** 切换前后摄像头按钮 */
	private TextView switchCameraButton;
	/** 拍摄按钮 */
	private Button captureButton;
	/** 滤镜选择栏 */
	private TuCameraFilterView filterBar;
	/** 滤镜开关按钮 */
	private TextView filterToggleButton;

	@Override
	protected void loadView(ViewGroup view)
	{
		// 相机视图
		cameraView = this.getViewById(R.id.cameraView);
		// 配置栏
		// configBar = this.getViewById(R.id.configBar);
		// 取消按钮
		cancelButton = this.getViewById(R.id.cancelButton);
		cancelButton.setOnClickListener(mClickListener);
		// 闪光灯栏
		flashBar = this.getViewById(R.id.flashBar);
		this.showViewIn(flashBar, false);
		// 切换前后摄像头按钮
		switchCameraButton = this.getViewById(R.id.switchCameraButton);
		switchCameraButton.setOnClickListener(mClickListener);
		// 设置是否显示前后摄像头切换按钮
		this.showViewIn(switchCameraButton, Camera2Helper.cameraCounts(this.getActivity()) > 1);
		// 底部栏
		// bottomBar = this.getViewById(R.id.bottomBar);
		// 拍摄按钮
		captureButton = this.getViewById(R.id.captureButton);
		captureButton.setOnClickListener(mClickListener);

		// 滤镜开关按钮
		filterToggleButton = this.getViewById(R.id.filterButton);
		filterToggleButton.setOnClickListener(mClickListener);
		// 滤镜选择栏
		filterBar = this.getViewById(R.id.lsq_group_filter_view);
		// 设置控制器
		filterBar.setActivity(this.getActivity());
		// 绑定选择委托
		filterBar.setDelegate(mFilterBarDelegate);

		// 设置默认是否显示
		filterBar.setDefaultShowState(true);
		// 显示滤镜标题视图
		filterBar.setDisplaySubtitles(true);

		// 滤镜选择栏 设置SDK内置滤镜
		filterBar.loadFilters();
	}

	@Override
	protected void viewDidLoad(ViewGroup view)
	{
		// 获取相机管理对象
		mCameraManager = Camera2Helper.cameraManager(getActivity());
		// 优先开启后置相机
		mCameraId = Camera2Helper.firstBackCameraId(getActivity());
		// 获取相机信息
		mCameraCharacter = Camera2Helper.cameraCharacter(mCameraManager, mCameraId);
		// 初始化子线程和handler
		this.initHandler();
		// 创建GL视图
		this.buildSelesView();
		// 设置显示区域和位置
		mCameraView.setDisplayRect(new RectF(0, 0, 1.0f, 1.0f));
		// 创建相机处理器
		mProcessor = new SelesVideoCamera2Processor(this.getActivity(), mCameraView);
		mProcessor.setCameraEngine(mCamera2ProcessorEngine);
		// 设置默认相机方向
		mProcessor.setOutputImageOrientation(InterfaceOrientation.Portrait);
		// 水平镜像前置摄像头
		mProcessor.setHorizontallyMirrorFrontFacingCamera(true);
		// 启动相机
		this.startCameraCapture();
	}

	@Override
	public void onResume()
	{
		super.onResume();
		if (!this.isFragmentPause() && mProcessor != null)
		{
			this.startCameraCapture();
		}
	}

	@Override
	public void onPause()
	{
		super.onPause();
		this.stopCameraCapture();
	}

	@Override
	public void onDestroyView()
	{
		super.onDestroyView();
		this.destroyCamera();
	}

	/** 滤镜选择栏委托 */
	private TuCameraFilterViewDelegate mFilterBarDelegate = new TuCameraFilterViewDelegate()
	{
		/**
		 * @param view
		 *            滤镜分组视图
		 * @param itemData
		 *            滤镜分组元素
		 * @param canCapture
		 *            是否允许拍摄
		 * @return 是否允许继续执行
		 */
		@Override
		public boolean onGroupFilterSelected(TuCameraFilterView view, GroupFilterItem itemData, boolean canCapture)
		{
			// 直接拍照
			if (canCapture)
			{
				captureImage();
				return true;
			}

			switch (itemData.type)
				{
				case TypeFilter:
					// 设置滤镜
					return handleSwitchFilter(itemData.filterOption);
				default:
					break;
				}
			return true;
		}

		/**
		 *  滤镜栏状态已改变通知
		 */
		@Override
		public void onGroupFilterShowStateChanged(TuCameraFilterView view, boolean isShow)
		{

		}

		/**
		 * 滤镜栏状态将要改变通知
		 */
		@Override
		public void onGroupFilterShowStateWillChanged(TuCameraFilterView view,boolean isShow)
		{
			
		}
	};

	/** 按钮点击事件 */
	private OnClickListener mClickListener = new OnSafeClickListener()
	{
		@Override
		public void onSafeClick(View v)
		{
			switch (v.getId())
				{
				// 取消
				case R.id.cancelButton:
					dismissActivityWithAnim();
					break;
				// 切换摄像头
				case R.id.switchCameraButton:
					rotateCamera();
					break;
				// 拍摄
				case R.id.captureButton:
					captureImage();
					break;
				// 滤镜开关切换按钮
				case R.id.filterButton:
					filterBar.showGroupView();
					break;
				default:
					break;
				}
		}
	};

	/**
	 * 处理滤镜切换
	 * 
	 * @param opt
	 * @return 是否允许切换
	 */
	private boolean handleSwitchFilter(FilterOption opt)
	{
		if (mProcessor == null) return false;

		String code = FilterLocalPackage.NormalFilterCode;
		if (opt != null)
		{
			code = opt.code;
		}

		mProcessor.switchFilter(code);
		return true;
	}

	/** 测试方法 */
	private void test(TuSdkResult result)
	{
		result.logInfo();

		Bitmap image = result.image;

		ImageView imageView = new ImageView(this.getActivity());
		imageView.setBackgroundColor(Color.GRAY);
		imageView.setScaleType(ScaleType.FIT_CENTER);
		imageView.setImageBitmap(image);
		imageView.setOnClickListener(mImageViewClickListener);
		cameraView.addView(imageView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	}

	private OnClickListener mImageViewClickListener = new OnSafeClickListener()
	{
		@Override
		public void onSafeClick(View v)
		{
			v.setOnClickListener(null);
			cameraView.removeView(v);
			resumeCameraCapture();
		}
	};

	/******************************************************************************************/
	/** Camera Manager */
	private CameraManager mCameraManager;
	/** Camera Device ID */
	private String mCameraId;
	/** Camera Characteristics */
	private CameraCharacteristics mCameraCharacter;
	/** Camera Device */
	private CameraDevice mCameraDevice;
	/** 相机的会话 */
	private CameraCaptureSession mCameraCaptureSession;
	/** 获取自己looper的handler */
	private Handler mHandler;
	/** 线程池 */
	private HandlerThread mHandlerThread;

	/** preview surface */
	private Surface mPreviewSurface;
	/** 显示的Builder */
	private CaptureRequest.Builder mPreviewBuilder;
	/** 拍照的Builder */
	private CaptureRequest.Builder mCaptureBuilder;
	/** 用来保存图片的ImageReader */
	private ImageReader mImageReader;
	/** Video Camera 2 Processor */
	private SelesVideoCamera2Processor mProcessor;
	/** Seles Smart View */
	private SelesSmartView mCameraView;
	/** Is Captur Image */
	private boolean mIsCapturImage;
	/** Media Action Sound */
	private MediaActionSound mMediaActionSound;

	/** 初始化子线程和handler */
	private void initHandler()
	{
		this.initShutter();
		mHandlerThread = new HandlerThread("TuSDK_L_Camera");
		mHandlerThread.start();
		mHandler = new Handler(mHandlerThread.getLooper());
	}

	/** 释放子线程和handler */
	private void releaseHandler()
	{
		this.releaseShutter();
		if (mHandlerThread == null) return;

		try
		{
			// 关闭线程
			mHandlerThread.quitSafely();
			mHandlerThread.join();
			mHandlerThread = null;
		}
		catch (InterruptedException e)
		{
			TLog.e(e, "release Handler error");
		}
	}

	/****************************** CaptureSound *****************************************/

	/** init Shutter */
	private void initShutter()
	{
		mMediaActionSound = new MediaActionSound();
		mMediaActionSound.load(MediaActionSound.SHUTTER_CLICK);
	}

	/** play System Shutter */
	protected final void playSystemShutter()
	{
		if (mMediaActionSound == null) return;
		mMediaActionSound.play(MediaActionSound.SHUTTER_CLICK);
	}

	/** release Shutter */
	private void releaseShutter()
	{
		if (mMediaActionSound != null)
		{
			mMediaActionSound.release();
			mMediaActionSound = null;
		}
	}

	/** 创建GL视图 */
	private SelesSmartView buildSelesView()
	{
		if (this.cameraView == null)
		{
			TLog.e("Can not find cameraView");
			return mCameraView;
		}

		if (mCameraView == null)
		{
			mCameraView = new SelesSmartView(getActivity());

			// mCameraView = new SelesView(getActivity());
			// mCameraView.setFillMode(SelesFillModeType.PreserveAspectRatioAndFill);

			/***************************************************
			 * SelesView 与 SelesSmartView 区别
			 * SelesView 对于图像显示方式只能设置setFillMode(SelesFillModeType);
			 * SelesSmartView
			 * 可以设置mCameraView.setDisplayRect(RectF)，可以调整在GLView上显示任意大小和位置
			 * displayRect 为相对mCameraView的百分比参数
			 ***************************************************/
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			this.cameraView.addView(mCameraView, 0, params);
		}

		return mCameraView;
	}

	/** Get Image Reader */
	private ImageReader getImageReader()
	{
		if (mImageReader == null) mImageReader = this.buildJpegImageReader();
		return mImageReader;
	}

	/** Build Jpeg Image Reader */
	private ImageReader buildJpegImageReader()
	{
		if (mCameraCharacter == null) return mImageReader;
		StreamConfigurationMap map = Camera2Helper.streamConfigurationMap(mCameraCharacter);

		// 获取屏幕大小
		Size outputSize = Camera2Helper.pictureOptimalSize(this.getActivity(), map.getOutputSizes(ImageFormat.JPEG),
				ContextUtils.getScreenSize(this.getActivity()));

		if (outputSize == null) return null;

		ImageReader reader = ImageReader.newInstance(outputSize.getWidth(), outputSize.getHeight(), ImageFormat.JPEG, 1);
		return reader;
	}

	/******************************** Camera Controller ***************************************/

	/** Start camera capturing */
	private void startCameraCapture()
	{
		if (mProcessor == null) return;
		this.stopCameraCapture();

		mProcessor.startCameraCapture();
	}

	/** Stop camera capturing */
	private void stopCameraCapture()
	{
		if (mProcessor == null) return;

		mIsCapturImage = false;
		mProcessor.stopCameraCapture();

		mPreviewBuilder = null;
		mCaptureBuilder = null;

		if (mCameraCaptureSession != null)
		{
			mCameraCaptureSession.close();
			mCameraCaptureSession = null;
		}
		if (mCameraDevice != null)
		{
			mCameraDevice.close();
			mCameraDevice = null;
		}

		if (mImageReader != null)
		{
			mImageReader.close();
			mImageReader = null;
		}
	}

	/** Pause camera capturing */
	private void pauseCameraCapture()
	{
		if (mProcessor == null) return;
		mProcessor.pauseCameraCapture();
		if (mCameraCaptureSession != null)
		{
			try
			{
				mCameraCaptureSession.stopRepeating();
			}
			catch (CameraAccessException e)
			{
				TLog.e(e, "pauseCameraCapture");
			}
		}
	}

	/** Resume camera capturing */
	private void resumeCameraCapture()
	{
		if (mProcessor == null) return;
		this.updatePreview();
		mProcessor.resumeCameraCapture();
		mIsCapturImage = false;
	}

	/** This flips between the front and rear cameras */
	public void rotateCamera()
	{
		if (mProcessor == null || !mProcessor.isCapturing() || !mProcessor.hasCreateSurface()) return;

		int cameraCounts = Camera2Helper.cameraCounts(this.getActivity());
		if (cameraCounts < 2) return;

		if (mCameraCharacter.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_BACK)
		{

			mCameraId = Camera2Helper.firstFrontCameraId(getActivity());
		}
		else mCameraId = Camera2Helper.firstBackCameraId(getActivity());

		// 获取相机信息
		mCameraCharacter = Camera2Helper.cameraCharacter(mCameraManager, mCameraId);

		this.startCameraCapture();
	}

	/** Destroy camera */
	private void destroyCamera()
	{
		this.stopCameraCapture();
		this.releaseHandler();
		if (mProcessor != null)
		{
			mProcessor.destroy();
			mProcessor = null;
		}
	}

	/******************************* Camera2 Processor Engine ****************************************/
	/** Video Camera2 Processor Engine */
	private SelesVideoCamera2ProcessorEngine mCamera2ProcessorEngine = new SelesVideoCamera2ProcessorEngine()
	{
		/** 是否允许初始化相机 */
		@Override
		public boolean canInitCamera()
		{
			if (mCameraId != null) return true;
			TLog.e("The device can not find any camera2 info: %s", mCameraCharacter.get(CameraCharacteristics.LENS_FACING));
			return false;
		}

		/** 初始化相机 */
		@Override
		public boolean onInitCamera()
		{
			if (mCameraCharacter != null) return true;
			TLog.e("The device can not find init camera2: %s", mCameraId);
			return false;
		}

		/** 获取最佳预览视图大小 */
		@Override
		public TuSdkSize previewOptimalSize()
		{
			if (mCameraCharacter == null) return null;
			// Camera2Helper.logCameraCharacter(mCameraCharacter);
			StreamConfigurationMap map = Camera2Helper.streamConfigurationMap(mCameraCharacter);
			// 获取最佳预览视图大小
			Size previeSize = Camera2Helper.previewOptimalSize(getActivity(), map.getOutputSizes(SurfaceTexture.class),
					ContextUtils.getScreenSize(getActivity()).maxSide(), 0.75f);

			return Camera2Helper.createSize(previeSize);
		}

		/** 即将开启相机 */
		@Override
		public void onCameraWillOpen(SurfaceTexture texture)
		{
			if (texture == null) return;
			// 显示的Surface
			mPreviewSurface = new Surface(texture);
			try
			{
				mCameraManager.openCamera(mCameraId, mCameraStateCallback, mHandler);
			}
			catch (CameraAccessException e)
			{
				TLog.e(e, "SelesVideoCamera2 asyncInitCamera");
			}
		}

		/** 相机已启动 */
		@Override
		public void onCameraStarted()
		{
			resumeCameraCapture();
		}

		/** 获取预览视图方向 */
		@Override
		public ImageOrientation previewOrientation()
		{
			return SelesVideoCamera2.computerOutputOrientation(getActivity(), mCameraCharacter, mProcessor.isHorizontallyMirrorRearFacingCamera(),
					mProcessor.isHorizontallyMirrorFrontFacingCamera(), mProcessor.getOutputImageOrientation());
		}

		/** 获取设备方向 */
		@Override
		public InterfaceOrientation deviceOrientation()
		{
			/**********************************
			 * 需要实现物理感应器设备方向
			 * InterfaceOrientation.getWithDegrees(int degrees);
			 **********************************/
			return InterfaceOrientation.Portrait;
		}

		/** 滤镜已经选中 */
		@Override
		public void onFilterSwitched(SelesOutInput filter)
		{

		}
	};

	/*************************** CameraDeviceStateCallback *****************************/
	/** Camera State Callback */
	private CameraDevice.StateCallback mCameraStateCallback = new CameraDevice.StateCallback()
	{
		@Override
		public void onOpened(CameraDevice camera)
		{
			TLog.d("mCameraStateCallback : %s [Thread: %s]", "onOpened", Thread.currentThread().getName());
			mCameraDevice = camera;
			startPreview();
		}

		@Override
		public void onDisconnected(CameraDevice camera)
		{
			TLog.d("mCameraStateCallback : %s", "onDisconnected");
		}

		@Override
		public void onError(CameraDevice camera, int error)
		{
			TLog.d("mCameraStateCallback : %s [%s]", "onError", error);
		}

		@Override
		public void onClosed(CameraDevice camera)
		{
			super.onClosed(camera);
			TLog.d("mCameraStateCallback : %s", "onClosed");
		}
	};

	/** start Preview */
	private void startPreview()
	{
		try
		{
			mPreviewBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
			// 创建预览视图
			mPreviewBuilder.addTarget(mPreviewSurface);
			// 3A auto
			mPreviewBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
			// 3A
			mPreviewBuilder.set(CaptureRequest.CONTROL_AF_MODE, CameraMetadata.CONTROL_AF_MODE_AUTO);
			mPreviewBuilder.set(CaptureRequest.CONTROL_AE_MODE, CameraMetadata.CONTROL_AE_MODE_ON);
			mPreviewBuilder.set(CaptureRequest.CONTROL_AWB_MODE, CameraMetadata.CONTROL_AWB_MODE_AUTO);

			// 设置显示视图FPS
			Range<Integer> range = mPreviewBuilder.get(CaptureRequest.CONTROL_AE_TARGET_FPS_RANGE);
			if (range != null) mProcessor.setRendererFPS(range.getUpper());

			// 创建相机会话
			mCameraDevice.createCaptureSession(this.getSurfaces(), mSessionPreviewStateCallback, mHandler);
		}
		catch (CameraAccessException e)
		{
			TLog.e(e, "startPreview Error");
		}
	}

	/** Get Surfaces */
	private List<Surface> getSurfaces()
	{
		List<Surface> list = new ArrayList<Surface>();
		if (mPreviewSurface != null) list.add(mPreviewSurface);
		if (this.getImageReader() != null) list.add(this.getImageReader().getSurface());
		return list;
	}

	/******************************** Session Preview State Callback *************************************/
	/** Session Preview State Callback */
	private CameraCaptureSession.StateCallback mSessionPreviewStateCallback = new CameraCaptureSession.StateCallback()
	{
		@Override
		public void onConfigured(CameraCaptureSession session)
		{
			TLog.d("mSessionPreviewStateCallback : %s [Thread: %s]", "onConfigured", Thread.currentThread().getName());
			try
			{
				mCameraCaptureSession = session;
				session.setRepeatingRequest(mPreviewBuilder.build(), null, mHandler);
			}
			catch (CameraAccessException e)
			{
				TLog.e(e, "mSessionPreviewStateCallback onConfigured error");
			}
		}

		@Override
		public void onConfigureFailed(CameraCaptureSession session)
		{
			TLog.d("mSessionPreviewStateCallback : %s", "onConfigureFailed");
		}
	};

	/** 更新预览 */
	protected void updatePreview()
	{
		if (mCameraCaptureSession == null) return;
		try
		{
			mCameraCaptureSession.setRepeatingRequest(mPreviewBuilder.build(), null, mHandler);
		}
		catch (Exception e)
		{
			TLog.e(e, "updatePreview error");
		}
	}

	/** Capture Image */
	private void captureImage()
	{
		if (mProcessor == null || mIsCapturImage || this.getImageReader() == null || mCameraDevice == null || mCameraCaptureSession == null) return;
		mIsCapturImage = true;

		this.getImageReader().setOnImageAvailableListener(new OnImageAvailableListener()
		{
			@Override
			public void onImageAvailable(final ImageReader reader)
			{
				ThreadHelper.runThread(new Runnable()
				{
					@Override
					public void run()
					{
						onImageCaptured(reader);
					}
				});
			}
		}, mHandler);

		try
		{
			// 创建构建者，配置参数
			mCaptureBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);

			mCaptureBuilder.addTarget(this.getImageReader().getSurface());
			mCaptureBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);

			Camera2Helper.mergerBuilder(mPreviewBuilder, mCaptureBuilder, CaptureRequest.CONTROL_AE_TARGET_FPS_RANGE);

			mCameraCaptureSession.stopRepeating();
			mCameraCaptureSession.capture(mCaptureBuilder.build(), mStillCaptureCallback, mHandler);
		}
		catch (Exception e)
		{
			TLog.e(e, "captureImage");
			mIsCapturImage = false;
			this.startCameraCapture();
		}
	}

	private CameraCaptureSession.CaptureCallback mStillCaptureCallback = new CameraCaptureSession.CaptureCallback()
	{
		@Override
		public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, final TotalCaptureResult result)
		{
			super.onCaptureCompleted(session, request, result);

			playSystemShutter();
			updatePreview();
		}
	};

	/** on Image Captured */
	private void onImageCaptured(ImageReader reader)
	{
		Image image = reader.acquireLatestImage();
		ByteBuffer buffer = image.getPlanes()[0].getBuffer();
		final byte[] bytes = new byte[buffer.remaining()];
		buffer.get(bytes);

		image.close();

		// 暂停拍摄
		this.pauseCameraCapture();

		Bitmap bitmap = BitmapHelper.imageDecode(bytes, true);
		bitmap = BitmapHelper.imageCorpResize(bitmap, ContextUtils.getScreenSize(this.getActivity()), 0, mCamera2ProcessorEngine.previewOrientation(), false);
		bitmap = mProcessor.processCaptureImage(bitmap);

		final TuSdkResult result = new TuSdkResult();
		result.image = bitmap;

		ThreadHelper.post(new Runnable()
		{
			@Override
			public void run()
			{
				test(result);
			}
		});
	}
}