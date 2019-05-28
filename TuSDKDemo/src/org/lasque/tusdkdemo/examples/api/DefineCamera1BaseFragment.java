/** 
 * TuSdkDemo
 * DefineCamera1BaseFragment.java
 *
 * @author 		Clear
 * @Date 		2015-12-27 下午1:57:12 
 * @Copyright 	(c) 2015 tusdk.com. All rights reserved.
 * 
 */
package org.lasque.tusdkdemo.examples.api;

import java.io.IOException;

import org.lasque.tusdk.core.TuSdkResult;
import org.lasque.tusdk.core.media.camera.TuSdkCameraOrientationImpl;
import org.lasque.tusdk.core.seles.output.SelesSmartView;
import org.lasque.tusdk.core.seles.sources.SelesOutInput;
import org.lasque.tusdk.core.seles.sources.SelesVideoCameraProcessor;
import org.lasque.tusdk.core.seles.sources.SelesVideoCameraProcessor.SelesVideoCameraProcessorEngine;
import org.lasque.tusdk.core.seles.tusdk.FilterLocalPackage;
import org.lasque.tusdk.core.seles.tusdk.FilterOption;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.ContextUtils;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.ThreadHelper;
import org.lasque.tusdk.core.utils.hardware.CameraHelper;
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

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
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
 * Camera1 快速相机范例
 * 
 * @author Clear
 */
@SuppressWarnings("deprecation")
public class DefineCamera1BaseFragment extends TuFragment
{
	/** 布局ID */
	public static final int layoutId = R.layout.demo_define_camera_base_fragment;

	public DefineCamera1BaseFragment()
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
		this.showViewIn(switchCameraButton, CameraHelper.cameraCounts() > 1);
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
		// 优先开启后置相机
		mCameraId = CameraInfo.CAMERA_FACING_BACK;
		// 创建GL视图
		this.buildSelesView();
		// 设置显示区域和位置
		mCameraView.setDisplayRect(new RectF(0, 0, 1.0f, 1.0f));
		// 创建相机处理器
		mProcessor = new SelesVideoCameraProcessor(this.getActivity(), mCameraView);
		mProcessor.setCameraEngine(mCameraProcessorEngine);
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
		if (opt != null) code = opt.code;

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
	/** Camera Device ID */
	private int mCameraId;
	/** 设备相机 */
	private Camera mCamera;
	/** 摄像头信息 */
	private CameraInfo mCameraInfo;
	/** Video Camera Processor */
	private SelesVideoCameraProcessor mProcessor;
	/** Seles Smart View */
	private SelesSmartView mCameraView;
	/** Is Captur Image */
	private boolean mIsCapturImage;

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
	}

	/** Pause camera capturing */
	private void pauseCameraCapture()
	{
		if (mProcessor == null) return;
		mProcessor.pauseCameraCapture();
	}

	/** Resume camera capturing */
	private void resumeCameraCapture()
	{
		if (mProcessor == null) return;
		mProcessor.resumeCameraCapture();
		mIsCapturImage = false;
	}

	/** This flips between the front and rear cameras */
	public void rotateCamera()
	{
		if (mProcessor == null || !mProcessor.isCapturing() || !mProcessor.hasCreateSurface()) return;

		int cameraCounts = CameraHelper.cameraCounts();
		if (cameraCounts < 2) return;

		mCameraId = (mCameraId == CameraInfo.CAMERA_FACING_BACK ? CameraInfo.CAMERA_FACING_FRONT : CameraInfo.CAMERA_FACING_BACK);

		this.startCameraCapture();
	}

	/** Destroy camera */
	private void destroyCamera()
	{
		this.stopCameraCapture();
		if (mProcessor != null)
		{
			mProcessor.destroy();
			mProcessor = null;
		}
	}

	/******************************* Camera Processor Engine ****************************************/
	/** Video Camera2 Processor Engine */
	private SelesVideoCameraProcessorEngine mCameraProcessorEngine = new SelesVideoCameraProcessorEngine()
	{
		/** 是否允许初始化相机 */
		@Override
		public boolean canInitCamera()
		{
			// 摄像头信息
			mCameraInfo = CameraHelper.firstCameraInfo(mCameraId);
			if (mCameraInfo == null)
			{
				TLog.e("The device can not find any camera info: %s", mCameraInfo);
				return false;
			}
			return true;
		}

		/** 初始化相机 */
		@Override
		public Camera onInitCamera()
		{
			if (mCameraInfo == null) return null;
			// 设备相机
			mCamera = CameraHelper.getCamera(mCameraInfo);
			if (mCamera == null)
			{
				TLog.e("The device can not find init camera: %s", mCameraInfo);
				return null;
			}

			// 初始化配置
			onInitConfig(mCamera);
			return mCamera;
		}

		/** 获取最佳预览视图大小 */
		@Override
		public TuSdkSize previewOptimalSize()
		{
			if (mCamera == null) return null;
			return CameraHelper.createSize(mCamera.getParameters().getPreviewSize());
		}

		/** 即将开启相机 */
		@Override
		public void onCameraWillOpen(SurfaceTexture texture)
		{
			if (mCamera == null) return;
			try
			{
				mCamera.setPreviewTexture(texture);
			}
			catch (IOException e)
			{
				TLog.e(e, "onCameraWillOpen");
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
			return TuSdkCameraOrientationImpl.computerOutputOrientation(getActivity(), mCameraInfo, mProcessor.isHorizontallyMirrorRearFacingCamera(),
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

	/*************************** initConfig *****************************/
	/** initConfig */
	private void onInitConfig(Camera camera)
	{
		if (camera == null) return;

		// 相机参数
		Parameters mParams = camera.getParameters();
		if (mParams == null) return;

		// 设置预览视图大小
		CameraHelper.setPreviewSize(getActivity(), mParams, ContextUtils.getScreenSize(getActivity()).maxSide(), 0.75f);
		// 设置输出视图大小
		TuSdkSize size = ContextUtils.getScreenSize(this.getActivity()).limitSize();
		CameraHelper.setPictureSize(getActivity(), mParams, size);

		camera.setParameters(mParams);

		int range[] = new int[2];
		mParams.getPreviewFpsRange(range);
		// 设置显示视图刷新速度，节约用电，以及内存
		mProcessor.setRendererFPS(Math.max(range[0] / 1000, range[1] / 1000));
		// CameraHelper.logParameters(mParams);
	}

	/** Capture Image */
	private void captureImage()
	{
		if (mProcessor == null || mIsCapturImage || mCamera == null) return;
		mIsCapturImage = true;

		try
		{
			// 不设置onShutter就不会播放快门声音
			mCamera.takePicture(new ShutterCallback()
			{
				@Override
				public void onShutter()
				{

				}
			}, null, new PictureCallback()
			{
				@Override
				public void onPictureTaken(final byte[] data, Camera camera)
				{
					ThreadHelper.runThread(new Runnable()
					{
						@Override
						public void run()
						{
							onImageCaptured(data);
						}
					});
				}
			});
		}
		catch (RuntimeException e)
		{
			TLog.e(e, "captureImage");
			mIsCapturImage = false;
			this.startCameraCapture();
		}
	}

	/** on Image Captured */
	private void onImageCaptured(byte[] data)
	{
		// 暂停拍摄
		this.pauseCameraCapture();

		Bitmap bitmap = BitmapHelper.imageDecode(data, true);
		bitmap = BitmapHelper.imageCorpResize(bitmap, ContextUtils.getScreenSize(this.getActivity()), 0, mCameraProcessorEngine.previewOrientation(), false);
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