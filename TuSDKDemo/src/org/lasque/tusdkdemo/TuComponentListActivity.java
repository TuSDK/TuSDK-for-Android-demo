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

import org.lasque.tusdkdemo.examples.feature.BubbleSample;
import org.lasque.tusdkdemo.examples.feature.jigsaw.JigsawSample;
import org.lasque.tusdkpulse.core.TuSdk;
import org.lasque.tusdkpulse.core.TuSdkContext;
import org.lasque.tusdkpulse.core.secret.StatisticsManger;
import org.lasque.tusdkpulse.core.utils.ContextUtils;
import org.lasque.tusdkpulse.core.view.widget.TuSdkNavigatorBar.NavigatorBarButtonInterface;
import org.lasque.tusdkpulse.core.view.widget.TuSdkNavigatorBar.NavigatorBarButtonType;
import org.lasque.tusdkpulse.core.view.widget.TuSdkNavigatorBar.TuSdkNavigatorBarDelegate;
import org.lasque.tusdkpulse.impl.activity.TuFragmentActivity;
import org.lasque.tusdkpulse.impl.view.widget.TuNavigatorBar;
import org.lasque.tusdkpulse.modules.components.ComponentActType;
import org.lasque.tusdkdemo.SampleGroup.GroupType;
import org.lasque.tusdkdemo.examples.api.DefineCamera1BaseSample;
import org.lasque.tusdkdemo.examples.api.DefineCamera2BaseSample;
import org.lasque.tusdkdemo.examples.api.EffectEngineCameraSample;
import org.lasque.tusdkdemo.examples.component.AlbumComponentSample;
import org.lasque.tusdkdemo.examples.component.AlbumMultipleComponentSample;
import org.lasque.tusdkdemo.examples.component.GifImageViewActivity;
import org.lasque.tusdkdemo.examples.face.FaceDetectionImageSample;
import org.lasque.tusdkdemo.examples.face.FaceDetectionVideoSample;
import org.lasque.tusdkdemo.examples.feature.CameraAndEditorSample;
import org.lasque.tusdkdemo.examples.feature.FilterEditorSampleActivity;
import org.lasque.tusdkdemo.examples.feature.PaintEditorSample;
import org.lasque.tusdkdemo.examples.feature.SelfishCameraSample;
import org.lasque.tusdkdemo.examples.feature.StickerEditorSample;
import org.lasque.tusdkdemo.examples.feature.WipeAndFilterEditorSample;
import org.lasque.tusdkdemo.examples.suite.CameraComponentSample;
import org.lasque.tusdkdemo.examples.suite.EditAdvancedComponentSample;
import org.lasque.tusdkdemo.examples.suite.EditAndCutComponentSample;
import org.lasque.tusdkdemo.examples.suite.EditAvatarComponentSample;
import org.lasque.tusdkdemo.examples.suite.EditMultipleComponentSample;
import org.lasque.tusdkdemo.examples.ui.CustomizedCameraComponentSample;
import org.lasque.tusdkdemo.examples.ui.CustomizedEditComponentSample;
import org.lasque.tusdkdemo.theme.geev2.RichEditComponentSample;
import org.lasque.tusdkdemo.view.DemoListView;
import org.lasque.tusdkdemo.view.DemoListView.DemoListItemAction;
import org.lasque.tusdkdemo.view.DemoListView.DemoListViewDelegate;

import android.content.Intent;
import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * @author Clear
 */
public class TuComponentListActivity extends TuFragmentActivity implements TuSdkNavigatorBarDelegate
{
	/** 布局ID */
	public static final int layoutId = R.layout.demo_component_list_activity;

	public TuComponentListActivity()
	{

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

	// 滑动后退手势
	GestureDetector gdDetector;

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
		mNavigatorBar.setTitle(String.format("%s %s", TuSdkContext.getString(R.string.lsq_sdk_name), BuildConfig.VERSION_NAME));
		mNavigatorBar.setBackButtonId(R.id.lsq_backButton);
		mNavigatorBar.showBackButton(true);
		mNavigatorBar.delegate = this;

		// 默认单行列表
		mListView = this.getViewById(R.id.lsq_listView);
		mListView.setSimpleDelegate(mDemoListViewDelegate);

		// 滑动后退手势
		gdDetector = new GestureDetector(this, gestureListener);

		/**
		 * ！！！！！！！！！！！！！！！！！！！！！！！！！特别提示信息要长！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！
		 * 您可以通过查看 group.appendSimple 的具体类showSimple(Activity activity)方法，学习使用范例。
		 */

		// 范例分组
		SampleGroup group = new SampleGroup();
		
		// Gee主题范例
		group.appendSample(new RichEditComponentSample());
		
		// 相机组件范例
		group.appendSample(new CameraComponentSample());
		// 多功能图片编辑组件范例
		group.appendSample(new EditMultipleComponentSample());
		// 裁剪组件范例
		group.appendSample(new EditAndCutComponentSample());
		// 头像设置组件范例
		group.appendSample(new EditAvatarComponentSample());
		// 裁切 + 滤镜 + 贴纸 编辑组件范例
		group.appendSample(new EditAdvancedComponentSample());

		// 相册组件范例
		group.appendSample(new AlbumComponentSample());
		// 多选相册组件范例
		group.appendSample(new AlbumMultipleComponentSample());

		// 拍照并编辑示例
		group.appendSample(new CameraAndEditorSample());
		// 自拍相机示例
		group.appendSample(new SelfishCameraSample());
		// 滤镜编辑器示例
		group.appendSample(GroupType.FeatureSample, R.string.sample_comp_FilterComponent, FilterEditorSampleActivity.class);
		// 添置编辑器示例
		group.appendSample(new StickerEditorSample());

		group.appendSample(new BubbleSample());

		group.appendSample(new JigsawSample());



		// 涂抹编辑器示例
		group.appendSample(new WipeAndFilterEditorSample());
		// 涂鸦编辑器示例
		group.appendSample(new PaintEditorSample());

		// 裁切组件自定义界面范例
		group.appendSample(new CustomizedEditComponentSample());
		// 相机界面自定义
		group.appendSample(new CustomizedCameraComponentSample());
		// 底层相机范例 (camera)
		group.appendSample(new DefineCamera1BaseSample());
		// 底层相机范例 (camera2)
		group.appendSample(new DefineCamera2BaseSample());


		// 加载范例列表
		mListView.loadSimples(group);
	}

	/** 范例列表视图委托 */
	private DemoListViewDelegate mDemoListViewDelegate = new DemoListViewDelegate()
	{
		@Override
		public void onDemoListViewSelected(DemoListView view, SampleBase simple, DemoListItemAction action)
		{
			onSelectedSimple(simple, action);
		}
	};

	/** 滑动后退手势监听 */
	GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener()
	{
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
		{
			// 移动距离
			if (e2.getRawX() - e1.getRawX() < ContextUtils.getScreenSize(TuComponentListActivity.this).width * MAX_SLIDE_DISTANCE) return false;

			// 滑动位置
			if (e1.getRawX() > ContextUtils.getScreenSize(TuComponentListActivity.this).width * 0.2) return false;

			// 滑动速度
			if (Math.abs(velocityX) < Math.abs(velocityY) || velocityX < MAX_SLIDE_SPEED) return false;

			// 关闭界面
			TuComponentListActivity.this.finish();

			return true;
		}
	};

	/** 选中范例 */
	private void onSelectedSimple(SampleBase sample, DemoListItemAction action)
	{
		if (sample == null || action == null) return;

		switch (action)
			{
			case ActionSelected:
				// 直接启动 指定Activity
				if (sample.getClass() == SampleActivityBase.class)
				{
					Intent intent = new Intent(this, ((SampleActivityBase) sample).activityClazz);
					this.startActivity(intent);
				}
				else
				{
					sample.showSample(this);
				}
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

	/**
	 * 分发触摸事件
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev)
	{
		gdDetector.onTouchEvent(ev);
		return super.dispatchTouchEvent(ev);
	}
}