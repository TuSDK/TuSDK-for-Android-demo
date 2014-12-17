/** 
 * TuSdkDemo
 * SimpleCameraFragment.java
 *
 * @author 		Clear
 * @Date 		2014-11-20 下午1:22:19 
 * @Copyright 	(c) 2014 Lasque. All rights reserved.
 * 
 */
package org.lasque.tusdkdemo;

import java.util.ArrayList;

import org.lasque.tusdk.core.TuSdk;
import org.lasque.tusdk.core.TuSdkResult;
import org.lasque.tusdk.core.utils.hardware.CameraHelper;
import org.lasque.tusdk.core.utils.hardware.TuSdkCamera;
import org.lasque.tusdk.core.utils.hardware.TuSdkCamera.CameraState;
import org.lasque.tusdk.core.utils.hardware.TuSdkCamera.TuSdkCameraListener;
import org.lasque.tusdk.core.view.TuSdkViewHelper;
import org.lasque.tusdk.core.view.TuSdkViewHelper.OnSafeClickListener;
import org.lasque.tusdk.impl.activity.TuFragment;
import org.lasque.tusdk.impl.components.camera.TuFocusTouchView;
import org.lasque.tusdk.impl.components.widget.FilterBar;
import org.lasque.tusdk.impl.components.widget.FilterBar.FilterBarDelegate;
import org.lasque.tusdk.impl.components.widget.FilterTableView;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.os.Handler;
import android.os.Looper;
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
 * 快速相机范例
 * 
 * @author Clear
 */
public class SimpleCameraFragment extends TuFragment
{
	/**
	 * 布局ID
	 */
	public static final int layoutId = R.layout.demo_simple_camera_fragment;

	public SimpleCameraFragment()
	{
		this.setRootViewLayoutId(layoutId);
	}

	// 相机视图
	private RelativeLayout cameraView;
	// 配置栏
	private LinearLayout configBar;
	// 取消按钮
	private TextView cancelButton;
	// 闪光灯栏
	private LinearLayout flashBar;
	// 闪光灯 关闭按钮
	private TextView flashOffButton;
	// 闪光灯 自动按钮
	private TextView flashAutoButton;
	// 闪光灯 开启按钮
	private TextView flashOpenButton;
	// 切换前后摄像头按钮
	private TextView switchCameraButton;
	// 底部栏
	private RelativeLayout bottomBar;
	// 拍摄按钮
	private Button captureButton;
	// 滤镜选择栏
	private FilterBar filterBar;

	// 闪光灯按钮列表
	private ArrayList<TextView> mFlashBtns = new ArrayList<TextView>(3);

	// 相机对象
	private TuSdkCamera mCamera;

	// 默认闪关灯模式
	private String mFlashModel = Parameters.FLASH_MODE_OFF;

	@Override
	protected void loadView(ViewGroup view)
	{
		// 相机视图
		cameraView = this.getViewById(R.id.cameraView);
		// 配置栏
		configBar = this.getViewById(R.id.configBar);
		// 取消按钮
		cancelButton = this.getViewById(R.id.cancelButton);
		cancelButton.setOnClickListener(mClickListener);
		// 闪光灯栏
		flashBar = this.getViewById(R.id.flashBar);
		// 闪光灯 关闭按钮
		flashOffButton = this.getViewById(R.id.flashOffButton);
		flashOffButton.setOnClickListener(mClickListener);
		flashOffButton.setTag(Parameters.FLASH_MODE_OFF);
		mFlashBtns.add(flashOffButton);
		// 闪光灯 自动按钮
		flashAutoButton = this.getViewById(R.id.flashAutoButton);
		flashAutoButton.setOnClickListener(mClickListener);
		flashAutoButton.setTag(Parameters.FLASH_MODE_AUTO);
		mFlashBtns.add(flashAutoButton);
		// 闪光灯 开启按钮
		flashOpenButton = this.getViewById(R.id.flashOpenButton);
		flashOpenButton.setOnClickListener(mClickListener);
		flashOpenButton.setTag(Parameters.FLASH_MODE_ON);
		mFlashBtns.add(flashOpenButton);
		// 切换前后摄像头按钮
		switchCameraButton = this.getViewById(R.id.switchCameraButton);
		switchCameraButton.setOnClickListener(mClickListener);
		// 设置是否显示前后摄像头切换按钮
		this.showViewIn(switchCameraButton, CameraHelper.cameraCounts() > 1);
		// 底部栏
		bottomBar = this.getViewById(R.id.bottomBar);
		// 拍摄按钮
		captureButton = this.getViewById(R.id.captureButton);
		captureButton.setOnClickListener(mClickListener);

		// 滤镜选择栏
		filterBar = this.getViewById(R.id.demo_filter_bar);
		// 绑定选择委托
		filterBar.setDelegate(mFilterBarDelegate);
		// 滤镜选择栏 设置SDK内置滤镜
		filterBar.loadFilters();

		this.setFlashModel(mFlashModel);
	}

	@Override
	protected void viewDidLoad(ViewGroup view)
	{
		// 计算视图位置为4:3
		Rect rect = CameraHelper.computerCameraViewRect(this.getActivity(),
				this.configBar, this.bottomBar, 0.75f);

		TuSdkViewHelper.setViewHeight(this.cameraView, rect.height());
		TuSdkViewHelper.setViewMarginTop(this.cameraView, rect.top);

		// 创建相机对象
		mCamera = TuSdk.camera(this.getActivity(),
				CameraInfo.CAMERA_FACING_BACK, this.cameraView);
		// 相机对象事件监听
		mCamera.setCameraListener(mCameraListener);

		// 可选：设置输出图片分辨率
		// 注意：因为移动设备内存问题，可能会限制部分机型使用最高分辨率
		// 请使用 TuSdkGPU.getGpuType().getSize() 查看当前设备所能够进行处理的图片尺寸
		// 默认使用 1920 * 1440分辨率
		// mCamera.setOutputSize(new TuSdkSize(5312, 2988));

		// 可选，设置相机手动聚焦
		mCamera.setFocusTouchView(TuFocusTouchView.getLayoutId());
		// 启动相机
		mCamera.start();
	}

	@Override
	public void onResume()
	{
		super.onResume();
		if (!this.isFragmentPause() && mCamera != null)
		{
			mCamera.start();
		}
	}

	@Override
	public void onPause()
	{
		super.onPause();
		if (mCamera != null)
		{
			mCamera.release();
		}
	}

	@Override
	public void onDestroyView()
	{
		super.onDestroyView();
		if (mCamera != null)
		{
			mCamera.release();
			mCamera = null;
		}
	}

	/**
	 * 滤镜选择栏委托
	 */
	private FilterBarDelegate mFilterBarDelegate = new FilterBarDelegate()
	{
		@Override
		public void onFilterBarSelectedFilter(String filterName, int position,
				FilterTableView tableView)
		{
			if (handleSwitchFilter(filterName))
			{
				tableView.setSelectedPosition(position);
			}
		}
	};

	// 按钮点击事件
	private OnClickListener mClickListener = new OnSafeClickListener()
	{
		@Override
		public void onSafeClick(View v)
		{
			switch (v.getId())
				{
				// 取消
				case R.id.cancelButton:
					handleCancelAction();
					break;
				// 闪光灯
				case R.id.flashOffButton:
				case R.id.flashAutoButton:
				case R.id.flashOpenButton:
					handleFlashAction(v);
					break;
				// 切换摄像头
				case R.id.switchCameraButton:
					handleSwitchCameraAction();
					break;
				// 拍摄
				case R.id.captureButton:
					handleCaptureAction();
					break;
				default:
					break;
				}
		}
	};

	/**
	 * 取消动作
	 */
	private void handleCancelAction()
	{
		this.dismissActivityWithAnim();
	}

	/**
	 * 闪光灯动作
	 * 
	 * @param resId
	 */
	private void handleFlashAction(View view)
	{
		this.setFlashModel((String) view.getTag());
	}

	/**
	 * 设置闪光灯模式
	 * 
	 * @param flashModel
	 */
	private void setFlashModel(String flashMode)
	{
		mFlashModel = flashMode;
		for (TextView btn : mFlashBtns)
		{
			if (flashMode.equalsIgnoreCase(btn.getTag().toString()))
			{
				btn.setTextColor(this.getResColor(R.color.demo_flash_selected));
			}
			else
			{
				btn.setTextColor(this.getResColor(R.color.demo_flash_normal));
			}
		}
		if (mCamera != null)
		{
			mCamera.setFlashMode(flashMode);
		}
	}

	/**
	 * 切换摄像头
	 */
	private void handleSwitchCameraAction()
	{
		if (mCamera != null)
		{
			mCamera.rotateCamera();
		}
	}

	/**
	 * 拍照
	 */
	private void handleCaptureAction()
	{
		if (mCamera != null)
		{
			mCamera.captureImage();
		}
	}

	/**
	 * 处理滤镜切换
	 * 
	 * @param filterName
	 * @return 是否允许切换
	 */
	private boolean handleSwitchFilter(String filterName)
	{
		if (mCamera != null)
		{
			mCamera.setFilterName(filterName);
			return true;
		}
		return false;
	}

	// 相机监听委托
	private TuSdkCameraListener mCameraListener = new TuSdkCameraListener()
	{
		/**
		 * 相机状态改变 (如需操作UI线程， 请检查当前线程是否为主线程)
		 * 
		 * @param camera
		 *            相机对象
		 * @param state
		 *            相机运行状态
		 */
		@Override
		public void onCameraStateChanged(TuSdkCamera camera, CameraState state)
		{
			if (state != CameraState.StateStarted) return;

			if (camera.canSupportFlash())
			{
				camera.setFlashMode(mFlashModel);
				showViewIn(flashBar, true);
			}
			else
			{
				showViewIn(flashBar, false);
			}
			// 输出相机设置信息
			CameraHelper.logParameters(mCamera.getCameraParameters());
		}

		/**
		 * 获取拍摄图片 (如需操作UI线程， 请检查当前线程是否为主线程)
		 * 
		 * @param camera
		 *            相机对象
		 * @param result
		 *            Sdk执行结果
		 */
		@Override
		public void onCameraTakedPicture(TuSdkCamera camera,
				final TuSdkResult result)
		{
			new Handler(Looper.getMainLooper()).post(new Runnable()
			{
				@Override
				public void run()
				{
					test(result);
				}
			});
		}
	};

	// 测试方法
	private void test(TuSdkResult result)
	{
		result.logInfo();

		Bitmap image = result.image;

		ImageView imageView = new ImageView(this.getActivity());
		imageView.setScaleType(ScaleType.FIT_CENTER);
		imageView.setImageBitmap(image);
		imageView.setOnClickListener(mImageViewClickListener);
		cameraView.addView(imageView, new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	}

	private OnClickListener mImageViewClickListener = new OnSafeClickListener()
	{
		@Override
		public void onSafeClick(View v)
		{
			v.setOnClickListener(null);
			cameraView.removeView(v);
			mCamera.resume();
		}
	};
}
