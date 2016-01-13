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

import org.lasque.tusdk.core.TuSdk;
import org.lasque.tusdk.core.secret.StatisticsManger;
import org.lasque.tusdk.core.seles.tusdk.FilterManager;
import org.lasque.tusdk.core.seles.tusdk.FilterManager.FilterManagerDelegate;
import org.lasque.tusdk.impl.activity.TuFragmentActivity;
import org.lasque.tusdk.modules.components.ComponentActType;
import org.lasque.tusdkdemo.simple.CameraComponentSimple;
import org.lasque.tusdkdemo.simple.EditMultipleComponentSimple;

import android.content.Intent;
import android.view.View;

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
		 * ！！！！！！！！！！！！！！！！！！！！！！！！！特别提示信息要长！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！
		 * 关于TuSDK体积（SDK编译后仅为0.9MB）：
		 * 1,如果您不需要使用本地贴纸功能，或仅需要使用在线贴纸功能，请删除/app/assets/TuSDK.bundle/stickers文件夹
		 * 2,如果您仅需要几款滤镜，您可以删除/app/assets/TuSDK.bundle/textures下的*.gsce文件
		 * 3,如果您不需要使用滤镜功能，请删除/app/assets/TuSDK.bundle/textures文件夹
		 * 4,TuSDK在线管理功能请访问：http://tusdk.com/
		 * 开发文档:http://tusdk.com/docs/android/api/
		 */
	}

	/** 初始化控制器 */
	@Override
	protected void initActivity()
	{
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

		/**
		 * ！！！！！！！！！！！！！！！！！！！！！！！！！特别提示信息要长！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！
		 * 关于TuSDK体积（SDK编译后仅为0.9MB）：
		 * 1,如果您不需要使用本地贴纸功能，或仅需要使用在线贴纸功能，请删除/app/assets/TuSDK.bundle/stickers文件夹
		 * 2,如果您仅需要几款滤镜，您可以删除/app/assets/TuSDK.bundle/textures下的*.gsce文件
		 * 3,如果您不需要使用滤镜功能，请删除/app/assets/TuSDK.bundle/textures文件夹
		 * 4,TuSDK在线管理功能请访问：http://tusdk.com/
		 * 开发文档:http://tusdk.com/doc
		 */

		// 异步方式初始化滤镜管理器 (注意：如果需要一开启应用马上执行SDK组件，需要做该检测，否则可以忽略检测)
		// 需要等待滤镜管理器初始化完成，才能使用所有功能
		TuSdk.messageHub().setStatus(this, R.string.lsq_initing);
		TuSdk.checkFilterManager(mFilterManagerDelegate);

		mCameraButtonView = this.getViewById(R.id.lsq_entry_camera);
		mEditorButtonView = this.getViewById(R.id.lsq_entry_editor);
		mComponentListButtonView = this.getViewById(R.id.lsq_entry_list);

		mCameraButtonView.setOnClickListener(mButtonClickListener);
		mEditorButtonView.setOnClickListener(mButtonClickListener);
		mComponentListButtonView.setOnClickListener(mButtonClickListener);
	}

	/** 滤镜管理器委托 */
	private FilterManagerDelegate mFilterManagerDelegate = new FilterManagerDelegate()
	{
		@Override
		public void onFilterManagerInited(FilterManager manager)
		{
			TuSdk.messageHub().showSuccess(DemoEntryActivity.this, R.string.lsq_inited);
		}
	};

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
		new CameraComponentSimple().showSimple(this);
	}

	/** 打开多功能编辑组件 */
	private void showEditorComponent()
	{
		new EditMultipleComponentSimple().showSimple(this);
	}

	/** 显示组件列表页面 */
	private void showComponentList()
	{
		Intent intent = new Intent(this, TuComponentListActivity.class);
		startActivity(intent);
	}
}