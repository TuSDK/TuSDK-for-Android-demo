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

import org.lasque.tusdkpulse.core.TuSdk;
import org.lasque.tusdkpulse.core.TuSdkResult;
import org.lasque.tusdkpulse.core.seles.SelesParameters;
import org.lasque.tusdkpulse.core.seles.tusdk.FilterOption;
import org.lasque.tusdkpulse.core.utils.TLog;
import org.lasque.tusdkpulse.core.utils.ThreadHelper;
import org.lasque.tusdkpulse.core.utils.hardware.Camera2Helper;
import org.lasque.tusdkpulse.core.utils.hardware.CameraConfigs;
import org.lasque.tusdkpulse.core.utils.hardware.InterfaceOrientation;
import org.lasque.tusdkpulse.core.view.TuSdkViewHelper.OnSafeClickListener;
import org.lasque.tusdkpulse.cx.api.TuCamera2Shower;
import org.lasque.tusdkpulse.cx.api.impl.TuCamera2ShowerImpl;
import org.lasque.tusdkpulse.cx.hardware.camera2.TuCamera2;
import org.lasque.tusdkpulse.cx.hardware.camera2.TuCamera2Shot;
import org.lasque.tusdkpulse.cx.seles.view.TuEGLContextFactory;
import org.lasque.tusdkpulse.impl.activity.TuFragment;
import org.lasque.tusdkpulse.impl.components.camera.TuCameraFilterView;
import org.lasque.tusdkpulse.impl.components.camera.TuCameraFilterView.TuCameraFilterViewDelegate;
import org.lasque.tusdkpulse.impl.components.widget.filter.FilterParameterConfigView;
import org.lasque.tusdkpulse.modules.view.widget.filter.GroupFilterItem;
import org.lasque.tusdkdemo.R;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.SurfaceTexture;

import com.tusdk.pulse.Engine;
import com.tusdk.pulse.filter.FilterDisplayView;
import com.tusdk.pulse.filter.Image;

import android.os.Build;
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

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import androidx.core.view.ViewCompat;

/**
 * Camera2 快速相机范例
 * 
 * @author Clear
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class DefineCamera2BaseFragment extends TuFragment
{
	private final static String TAG = "DefineCamera2BaseFragment";
	/** 布局ID */
	public static final int layoutId = R.layout.demo_define_camera_base_fragment;

	public DefineCamera2BaseFragment()
	{
		this.setRootViewLayoutId(layoutId);
	}

	/** 相机视图 */
	private RelativeLayout cameraView;
	/** GL视图 */
	private FilterDisplayView surfaceView;
	/** 相机渲染接口 */
	private TuCamera2Shower mCameraShower;
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
	/** 滤镜参数调节 */
	private FilterParameterConfigView mFilterParamsView;
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

		// 滤镜参数调节
		mFilterParamsView = this.getViewById("lsq_filter_parameter_config_view");
		mFilterParamsView.setVisibility(View.GONE);

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

		// GL视图
		surfaceView = this.getViewById(R.id.surfaceView);
		if (surfaceView == null) return;
		/** step.1: 相机渲染 */
		mCameraShower = new TuCamera2ShowerImpl();
		/** step.2: 请求初始化 */
		mCameraShower.requestInit();

		/** step.5: 创建相机数据监听，并通知刷新到界面 */
		mCameraShower.setFrameAvailableListener(new SurfaceTexture.OnFrameAvailableListener(){
			@Override
			public void onFrameAvailable(SurfaceTexture surfaceTexture) {
				Image res = mCameraShower.onDrawFrame();
				surfaceView.updateImage(res);
			}
		});

		surfaceView.init(Engine.getInstance().getMainGLContext());
		/** step.6: 准备初始化相机 */
		if (!mCameraShower.prepare()) return;
		// 设置默认开启相机方向
		mCameraShower.camera().cameraBuilder().setDefaultFacing(CameraConfigs.CameraFacing.Front);
		// 设置默认相机方向
		mCameraShower.camera().cameraOrient().setOutputImageOrientation(InterfaceOrientation.Portrait);
		// 水平镜像前置摄像头
		mCameraShower.camera().cameraOrient().setHorizontallyMirrorFrontFacingCamera(true);
		// 显示选区百分比
		// mCameraShower.setDisplayRect(new RectF(0, 0, 1.0f, 1.0f));
		/** step.7: 设置相机状态监听 */
		mCameraShower.camera().setCameraListener(new TuCamera2.TuCamera2Listener() {
			@Override
			public void onStatusChanged(CameraConfigs.CameraState status, TuCamera2 camera) {

			}
		});

		/** step.8: 设置相机拍照监听 */
		mCameraShower.camera().cameraShot().setShotListener(new TuCamera2Shot.TuCamera2ShotListener() {
			/** 拍摄照片失败 */
			@Override
			public void onCameraShotFailed(TuSdkResult data) {

			}

			/** 直接Bitmap TuSdkResult.image */
			@Override
			public void onCameraShotBitmap(TuSdkResult data) {
				if (mCameraShower == null) return;
				final Bitmap img = mCameraShower.filterImage(data.image);
				ThreadHelper.post(new Runnable()
				{
					@Override
					public void run()
					{
						test(img);
					}
				});
			}
		});

		/** step.9: 启动相机 [优先开启后置相机] */
		// 自动通过 onResume()调用
		// mCameraShower.camera().startPreview(CameraConfigs.CameraFacing.Back);
	}

	@Override
	protected void viewDidLoad(ViewGroup view)
	{

	}

	@Override
	public void onResume()
	{
		super.onResume();
		if (!this.isFragmentPause())
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

	/******************************** Camera Controller ***************************************/

	/** Start camera capturing */
	private void startCameraCapture()
	{
		if (mCameraShower == null) return;
		mCameraShower.camera().startPreview();
	}

	/** Stop camera capturing */
	private void stopCameraCapture()
	{
		if (mCameraShower == null) return;
		mCameraShower.camera().stopPreview();
	}

	/** Pause camera capturing */
	private void pauseCameraCapture()
	{
		if (mCameraShower == null) return;
		mCameraShower.camera().pausePreview();
	}

	/** Resume camera capturing */
	private void resumeCameraCapture()
	{
		mCameraShower.camera().resumePreview();
	}

	/** This flips between the front and rear cameras */
	public void rotateCamera()
	{
		if (mCameraShower == null) return;
		mCameraShower.camera().rotateCamera();
	}

	/** Capture Image */
	private void captureImage() {
		if (mCameraShower == null) return;
		mCameraShower.camera().shotPhoto();
	}

	/** Destroy camera */
	private void destroyCamera()
	{
		if (mCameraShower == null) return;
		mCameraShower.destroy();
		mCameraShower = null;
	}

	/** 滤镜选择栏委托 */
	private TuCameraFilterViewDelegate mFilterBarDelegate = new TuCameraFilterViewDelegate()
	{
		/**
		 * @param view 滤镜分组视图
		 * @param itemData 滤镜分组元素
		 * @param canCapture 是否允许拍摄
		 * @return 是否允许继续执行
		 */
		@Override
		public boolean onGroupFilterSelected(TuCameraFilterView view, GroupFilterItem itemData, boolean canCapture)
		{
			// 直接拍照
			if (canCapture) {
				captureImage();
				return true;
			}

			switch (itemData.type)
			{
				case TypeFilter:// 设置滤镜
				{
					if (mCameraShower == null) return false;
					SelesParameters params = mCameraShower.changeFilter(itemData.filterOption == null ? null : itemData.filterOption.code);
					mFilterParamsView.setFilterParams(params);
				}
				default: return true;
			}
		}

		/** 滤镜栏状态已改变通知 */
		@Override
		public void onGroupFilterShowStateChanged(TuCameraFilterView view, boolean isShow)
		{
			if (isShow) return;
			mFilterParamsView.setVisibility(View.GONE);
		}

		/** 滤镜栏状态将要改变通知 */
		@Override
		public void onGroupFilterShowStateWillChanged(TuCameraFilterView view,boolean isShow)
		{
			if (mFilterParamsView == null) return;
			if (!isShow || !mFilterParamsView.hasFilterParams()) mFilterParamsView.setVisibility(View.GONE);
				// 第一次显示时执行动画再次切换滤镜时不执行
			else if (mFilterParamsView.getVisibility() == View.GONE) {
				mFilterParamsView.setVisibility(View.VISIBLE);
				mFilterParamsView.setAlpha(0);
				ViewCompat.animate(mFilterParamsView).alpha(1).setDuration(400);
			}
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

	/** 测试方法 */
	private void test(Bitmap image)
	{
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
}