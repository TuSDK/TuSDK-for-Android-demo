/** 
  * TuSdkDemo
 * DemoEntryActivity.java
 *
 * @author 		Clear
 * @Date 		2014-11-15 下午4:30:52 
 * @Copyright 	(c) 2014 tusdk.com. All rights reserved.
 * @link 		开发文档:http://tusdk.com/docs/android/api/
 */
package org.lasque.tusdkdemo;

import org.lasque.tusdkdemo.utils.Constants;
import org.lasque.tusdkpulse.core.BuildConfig;
import org.lasque.tusdkpulse.core.TuSdk;
import org.lasque.tusdkpulse.core.TuSdkContext;
import org.lasque.tusdkpulse.core.secret.StatisticsManger;
import org.lasque.tusdkpulse.core.utils.ThreadHelper;
import org.lasque.tusdkpulse.impl.activity.TuFragmentActivity;
import org.lasque.tusdkpulse.modules.components.ComponentActType;
import org.lasque.tusdkdemo.examples.suite.CameraComponentSample;
import org.lasque.tusdkdemo.examples.suite.EditMultipleComponentSample;
import org.lasque.tusdkdemo.utils.PermissionUtils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.TextView;

import com.tusdk.pulse.Engine;
import com.tusdk.pulse.utils.AssetsMapper;

import static org.lasque.tusdkdemo.utils.PermissionUtils.getRequiredPermissions;
/**
 * @author Clear
 */
public class DemoEntryActivity extends TuFragmentActivity
{
	/** 布局ID */
	public static final int layoutId = R.layout.demo_entry_activity;

	public DemoEntryActivity()
	{
		/**
		 ************************* TuSDK 集成三部曲 *************************
		 * 
		 * 1. 在官网注册开发者账户
		 * 
		 * 2. 下载SDK和示例代码
		 * 
		 * 3. 创建应用，获取appkey，导出资源包
		 * 
		 ************************* TuSDK 集成三部曲 ************************* 
		 * 
		 * 开发文档:http://tusdk.com/doc
		 * 
		 * 请参见TuApplication类中的SDK初始化代码。 
		 */
	}

	/** 初始化控制器 */
	@Override
	protected void initActivity()
	{
		Engine mEngine = Engine.getInstance();
		mEngine.init(null);
		super.initActivity();
		this.setRootView(layoutId, 0);

		// 设置应用退出信息ID 一旦设置将触发连续点击两次退出应用事件
		this.setAppExitInfoId(R.string.lsq_exit_info);

	}

	/** 相机按钮容器 */
	private View mCameraButtonView;

	/** 编辑器按钮容器 */
	private View mEditorButtonView;

	/** 组件列表按钮容器 */
	private View mComponentListButtonView;

	/**
	 * 初始化视图
	 */
	@Override
	protected void initView()
	{
		super.initView();
		// sdk统计代码，请不要加入您的应用
		StatisticsManger.appendComponent(ComponentActType.sdkComponent);
		
		// 异步方式初始化滤镜管理器 (注意：如果需要一开启应用马上执行SDK组件，需要做该检测，否则可以忽略检测)
		// 需要等待滤镜管理器初始化完成，才能使用所有功能
//		TuSdk.messageHub().setStatus(this, R.string.lsq_initing);

		ThreadHelper.runThread(new Runnable() {
			@Override
			public void run() {
				while (!TuSdk.checkResourceLoaded()){}
				ThreadHelper.post(new Runnable() {
					@Override
					public void run() {
						// 回到主线程
						TuSdk.messageHub().showSuccess(DemoEntryActivity.this, R.string.lsq_inited);
						/** 发布后清注释下面行 */
						//TuSdk.logAuthors();
					}
				});
			}
		});

		mCameraButtonView = this.getViewById(R.id.lsq_entry_camera);
		mEditorButtonView = this.getViewById(R.id.lsq_entry_editor);
		mComponentListButtonView = this.getViewById(R.id.lsq_entry_list);

		mCameraButtonView.setOnClickListener(mButtonClickListener);
		mEditorButtonView.setOnClickListener(mButtonClickListener);
		mComponentListButtonView.setOnClickListener(mButtonClickListener);

		((TextView) this.getViewById(R.id.lsq_register_version)).setText(BuildConfig.TUSDK_REGISTRATION_VERSON);
	}

	/** 按钮点击事件处理 */
	private View.OnClickListener mButtonClickListener = new View.OnClickListener()
	{
		public void onClick(View v)
		{

			if (v == mCameraButtonView)
			{
				showCameraComponent();
			}
			else if (v == mEditorButtonView)
			{
				showEditorComponent();
			}
			else if (v == mComponentListButtonView)
			{
				showComponentList();
			}
		}
	};

	/** 打开相机组件 */
	private void showCameraComponent()
	{
		new CameraComponentSample().showSample(this);
	}

	/** 打开多功能编辑组件 */
	private void showEditorComponent()
	{
		new EditMultipleComponentSample().showSample(this);
	}

	/** 显示组件列表页面 */
	private void showComponentList()
	{
		if (PermissionUtils.hasRequiredPermissions(this,getRequiredPermissions())){
			Intent intent = new Intent(this, TuComponentListActivity.class);
			startActivity(intent);
		} else {
			PermissionUtils.requestRequiredPermissions(this, getRequiredPermissions());
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Engine.getInstance().release();
	}
}