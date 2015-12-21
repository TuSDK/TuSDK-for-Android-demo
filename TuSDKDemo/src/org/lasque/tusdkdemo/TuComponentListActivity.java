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
import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.secret.StatisticsManger;
import org.lasque.tusdk.core.view.widget.TuSdkNavigatorBar.NavigatorBarButtonInterface;
import org.lasque.tusdk.core.view.widget.TuSdkNavigatorBar.NavigatorBarButtonType;
import org.lasque.tusdk.core.view.widget.TuSdkNavigatorBar.TuSdkNavigatorBarDelegate;
import org.lasque.tusdk.impl.activity.TuFragmentActivity;
import org.lasque.tusdk.impl.view.widget.TuNavigatorBar;
import org.lasque.tusdk.modules.components.ComponentActType;
import org.lasque.tusdkdemo.define.DefineCameraBaseSimple;
import org.lasque.tusdkdemo.extend.ExtendCameraBaseComponentSimple;
import org.lasque.tusdkdemo.extend.ExtendEditComponentSimple;
import org.lasque.tusdkdemo.simple.AlbumComponentSimple;
import org.lasque.tusdkdemo.simple.AlbumMultipleComponentSimple;
import org.lasque.tusdkdemo.simple.CameraComponentSimple;
import org.lasque.tusdkdemo.simple.EditAdvancedComponentSimple;
import org.lasque.tusdkdemo.simple.EditAndCutComponentSimple;
import org.lasque.tusdkdemo.simple.EditAvatarComponentSimple;
import org.lasque.tusdkdemo.simple.EditMultipleComponentSimple;
import org.lasque.tusdkdemo.simple.SimpleBase;
import org.lasque.tusdkdemo.simple.SimpleGroup;
import org.lasque.tusdkdemo.view.DemoListView;
import org.lasque.tusdkdemo.view.DemoListView.DemoListItemAction;
import org.lasque.tusdkdemo.view.DemoListView.DemoListViewDelegate;

/**
 * @author Clear
 */
public class TuComponentListActivity extends TuFragmentActivity implements TuSdkNavigatorBarDelegate
{
	/** 布局ID */
	public static final int layoutId = R.layout.demo_component_list_activity;

	public TuComponentListActivity()
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
	}

	/** 导航栏 实现类 */
	private TuNavigatorBar mNavigatorBar;

	/** 范例列表视图 */
	private DemoListView mListView;

	/**
	 * 初始化视图
	 */
	@Override
	protected void initView()
	{
		super.initView();
		// sdk统计代码，请不要加入您的应用
		StatisticsManger.appendComponent(ComponentActType.sdkComponent);

		// 导航栏 实现类
		mNavigatorBar = this.getViewById(R.id.lsq_navigatorBar);
		mNavigatorBar.setTitle(String.format("%s %s", TuSdkContext.getString(R.string.lsq_sdk_name), TuSdk.SDK_VERSION));
		mNavigatorBar.setBackButtonId(R.id.lsq_backButton);
		mNavigatorBar.showBackButton(true);
		mNavigatorBar.delegate = this;

		// 默认单行列表
		mListView = this.getViewById(R.id.lsq_listView);
		mListView.setSimpleDelegate(mDemoListViewDelegate);

		/**
		 * ！！！！！！！！！！！！！！！！！！！！！！！！！特别提示信息要长！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！
		 * 您可以通过查看 group.appendSimple 的具体类showSimple(Activity
		 * activity)方法，学习使用范例。
		 */

		// 范例分组
		SimpleGroup group = new SimpleGroup();
		// 相册组件范例
		group.appendSimple(new AlbumComponentSimple());
		// 相册组件 (带相机)范例
		group.appendSimple(new AlbumMultipleComponentSimple());
		// 相机组件范例
		group.appendSimple(new CameraComponentSimple());
		// 图片编辑组件 (裁剪)范例
		group.appendSimple(new EditAndCutComponentSimple());
		// 头像设置组件(编辑)范例
		group.appendSimple(new EditAvatarComponentSimple());
		// 高级图片编辑组件范例
		group.appendSimple(new EditAdvancedComponentSimple());
		// 多功能图片编辑组件范例
		group.appendSimple(new EditMultipleComponentSimple());

		// 图片编辑组件范例
		group.appendSimple(new ExtendEditComponentSimple());
		// 相机组件范例 - 修改界面
		group.appendSimple(new ExtendCameraBaseComponentSimple());

		// 自定义相机范例
		group.appendSimple(new DefineCameraBaseSimple());
		
		// 加载范例列表
		mListView.loadSimples(group);
	}

	/** 范例列表视图委托 */
	private DemoListViewDelegate mDemoListViewDelegate = new DemoListViewDelegate()
	{
		@Override
		public void onDemoListViewSelected(DemoListView view, SimpleBase simple, DemoListItemAction action)
		{
			onSelectedSimple(simple, action);
		}
	};

	/** 选中范例 */
	private void onSelectedSimple(SimpleBase simple, DemoListItemAction action)
	{
		if (simple == null || action == null) return;

		switch (action)
			{
			case ActionSelected:
				simple.showSimple(this);
				break;
			default:
				break;
			}
	}
	
	/**
	 * 后退按钮
	 */
	public void onNavigatorBarButtonClicked(NavigatorBarButtonInterface button)
	{
		if (button.getType() == NavigatorBarButtonType.back)
		{
			this.finish();
		}
	}
}