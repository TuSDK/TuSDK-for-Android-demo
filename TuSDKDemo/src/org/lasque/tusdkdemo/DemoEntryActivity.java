/** 
 * TuSdkDemo
 * DemoEntryActivity.java
 *
 * @author 		Clear
 * @Date 		2014-11-15 下午4:30:52 
 * @Copyright 	(c) 2014 Lasque. All rights reserved.
 * 
 */
package org.lasque.tusdkdemo;

import java.util.ArrayList;
import java.util.Arrays;

import org.lasque.tusdk.core.TuSdk;
import org.lasque.tusdk.core.TuSdkResult;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.hardware.CameraHelper;
import org.lasque.tusdk.core.utils.image.BitmapHelper;
import org.lasque.tusdk.core.view.listview.TuSdkArrayListView.ArrayListViewItemClickListener;
import org.lasque.tusdk.core.view.listview.TuSdkIndexPath;
import org.lasque.tusdk.impl.activity.TuFragment;
import org.lasque.tusdk.impl.activity.TuFragmentActivity;
import org.lasque.tusdk.impl.components.TuSdkComponent.TuSdkComponentDelegate;
import org.lasque.tusdk.impl.components.TuSdkHelperComponent;
import org.lasque.tusdk.impl.components.camera.TuCameraFragment;
import org.lasque.tusdk.impl.components.camera.TuCameraFragment.TuCameraFragmentDelegate;
import org.lasque.tusdk.impl.components.camera.TuCameraOption;
import org.lasque.tusdk.impl.components.edit.TuEditTurnAndCutFragment;
import org.lasque.tusdk.impl.components.edit.TuEditTurnAndCutFragment.TuEditTurnAndCutFragmentDelegate;
import org.lasque.tusdk.impl.components.edit.TuEditTurnAndCutOption;
import org.lasque.tusdk.impl.view.widget.TuNavigatorBar;
import org.lasque.tusdk.impl.view.widget.listview.TuDefaultLineListCellView;
import org.lasque.tusdk.impl.view.widget.listview.TuDefaultLineListView;

/**
 * @author Clear
 */
public class DemoEntryActivity extends TuFragmentActivity implements
		TuCameraFragmentDelegate, TuEditTurnAndCutFragmentDelegate
{
	/**
	 * 布局ID
	 */
	public static final int layoutId = R.layout.demo_entry_activity;

	public DemoEntryActivity()
	{

	}

	/**
	 * 初始化控制器
	 */
	@Override
	protected void initActivity()
	{
		super.initActivity();
		this.setRootView(layoutId, 0);

		// 设置应用退出信息ID 一旦设置将触发连续点击两次退出应用事件
		this.setAppExitInfoId(R.string.lsq_exit_info);
	}

	// 导航栏 实现类
	private TuNavigatorBar mNavigatorBar;

	// 默认单行列表
	private TuDefaultLineListView mListView;

	/**
	 * 组件帮助方法
	 */
	private TuSdkHelperComponent mComponent = new TuSdkHelperComponent(this);

	/**
	 * 初始化视图
	 */
	@Override
	protected void initView()
	{
		super.initView();

		// 导航栏 实现类
		mNavigatorBar = this.getViewById(R.id.lsq_navigatorBar);
		mNavigatorBar.setTitle(R.string.lsq_sdk_name);
		mNavigatorBar.setBackButtonId(R.id.lsq_backButton);
		mNavigatorBar.showBackButton(false);

		// 默认单行列表
		mListView = this.getViewById(R.id.lsq_listView);
		mListView.setItemClickListener(new ListItemClickDelegate());

		// 创建快速列表
		String elements[] = { "1-1 快速自定义相机", "2-1 相册组件", "2-2 相机组件",
				"2-3 图片编辑组件", "2-4 图片编辑组件 (裁剪)", "3-1 头像设置组件", "4-1 高级图片编辑组件" };
		mListView.setModeList(new ArrayList<String>(Arrays.asList(elements)));
	}

	private class ListItemClickDelegate implements
			ArrayListViewItemClickListener<String, TuDefaultLineListCellView>
	{
		/**
		 * 列表项点击事件
		 * 
		 * @param itemData
		 *            数据
		 * @param itemView
		 *            视图
		 * @param indexPath
		 *            索引
		 */
		@Override
		public void onArrayListViewItemClick(String itemData,
				TuDefaultLineListCellView itemView, TuSdkIndexPath indexPath)
		{
			switch (indexPath.row)
				{
				case 0:
					// 1-1 快速自定义相机
					simpleCustomHandler();
					break;
				case 1:
					// 2-1 相册组件
					albumComponentHandler();
					break;
				case 2:
					// 2-2 相机组件
					cameraComponentHandler();
					break;
				case 3:
					// 2-3 图片编辑组件
					editComponentHandler();
					break;
				case 4:
					// 2-4 图片编辑组件 (裁剪)
					editAndCutComponentHandler();
					break;
				case 5:
					// 3-1 头像设置组件
					avatarComponentHandler();
					break;
				case 6:
					// 4-1 高级图片编辑组件
					editAdvancedComponentHandler();
					break;
				default:
					break;
				}
		}
	}

	/*************************** simpleCustomHandler ****************************/
	/**
	 * 快速自定义相机
	 */
	private void simpleCustomHandler()
	{
		mComponent.presentModalNavigationActivity(new SimpleCameraFragment(),
				true);
	}

	/*************************** albumComponentHandler ****************************/
	/**
	 * 2-1 相册组件
	 */
	private void albumComponentHandler()
	{
		// TuAlbumComponent cp = TuAlbumComponent.component(activity, delegate);

		// TuAlbumListOption option = cp.componentOption().albumListOption();
		// 控制器类型
		// option.setComponentClazz(TuAlbumListFragment.class);

		// 设置根视图布局资源ID
		// option.setRootViewLayoutId(TuAlbumListFragment.getLayoutId());

		// 行视图布局ID
		// option.setCellLayoutId(TuAlbumListCell.getLayoutId());

		// 空视图布局ID
		// option.setEmptyViewLayouId(TuAlbumEmptyView.getLayoutId());

		// 需要自动跳转到相册组名称 (需要设定 autoSkipToPhotoList = true)
		// option.setSkipAlbumName("Camera");

		// 是否自动选择相册组 (默认: true, 如果没有设定相册组名称，自动跳转到系统相册组)
		// option.setAutoSkipToPhotoList(true);

		// TuPhotoListOption option = cp.componentOption().photoListOption();
		// 控制器类型
		// option.setComponentClazz(TuAlbumListFragment.class);

		// 设置根视图布局资源ID
		// option.setRootViewLayoutId(TuPhotoListFragment.getLayoutId());

		// 行视图布局ID
		// option.setCellLayoutId(TuPhotoListCell.getLayoutId());

		// 分组头部视图布局ID
		// option.setHeaderLayoutId(TuPhotoListHeader.getLayoutId());

		// 统计格式化字符
		// option.setTotalFooterFormater(TuSdkContext
		// .getString("lsq_album_total_format"));

		// 空视图布局ID
		// option.setEmptyViewLayouId(TuAlbumEmptyView.getLayoutId());

		TuSdk.albumCommponent(this, new TuSdkComponentDelegate()
		{
			@Override
			public void onComponentFinished(TuSdkResult result, Error error,
					TuFragment lastFragment)
			{
				// if (lastFragment != null)
				// lastFragment.dismissActivityWithAnim();
				TLog.d("onAlbumCommponentReaded: %s | %s", result, error);
			}
		})
		// 在组件执行完成后自动关闭组件
				.setAutoDismissWhenCompleted(true)
				// 显示组件
				.showComponent();
	}

	/*************************** cameraComponentHandler ****************************/
	/**
	 * 2-2 相机组件
	 */
	private void cameraComponentHandler()
	{
		// 如果不支持摄像头显示警告信息
		if (CameraHelper.showAlertIfNotSupportCamera(this)) return;

		TuCameraOption option = new TuCameraOption();

		// 控制器类型
		// option.setComponentClazz(TuCameraFragment.class);

		// 设置根视图布局资源ID
		// option.setRootViewLayoutId(TuCameraFragment.getLayoutId());

		// 保存到临时文件 (默认不保存, 当设置为true时, TuSdkResult.imageFile, 处理完成后将自动清理原始图片)
		// option.setSaveToTemp(false);

		// 保存到系统相册 (默认不保存, 当设置为true时, TuSdkResult.sqlInfo, 处理完成后将自动清理原始图片)
		// option.setSaveToAlbum(true);

		// 保存到系统相册的相册名称
		// option.setSaveToAlbumName("TuSdk");

		// 照片输出压缩率 (默认:90，0-100 如果设置为0 将保存为PNG格式)
		// option.setOutputCompress(90);

		// 相机方向 (默认:CameraInfo.CAMERA_FACING_BACK){@link
		// android.hardware.Camera.CameraInfo}
		// option.setAvPostion(CameraInfo.CAMERA_FACING_BACK);

		// 照片输出图片长宽 (默认：全屏)
		// option.setOutputSize(new TuSdkSize(1440, 1920));

		// 闪关灯模式
		// option.setDefaultFlashMode(Camera.Parameters.FLASH_MODE_OFF);

		// 是否开启滤镜支持 (默认: 关闭)
		option.setEnableFilters(true);

		// 默认是否显示滤镜视图 (默认: 不显示, 如果mEnableFilters = false, mShowFilterDefault将失效)
		option.setShowFilterDefault(true);

		// 需要显示的滤镜名称列表 (如果为空将显示所有自定义滤镜)
		// option.setFilterGroup(new ArrayList<String>());

		// 触摸聚焦视图ID (默认: tusdk_impl_component_camera_focus_touch_view)
		// option.setFocusTouchViewId(TuFocusTouchView.getLayoutId());

		// 视频视图显示比例 (默认: 0, 全屏)
		// option.setCameraViewRatio(0);

		// 是否直接输出图片数据 (默认:false，输出已经处理好的图片Bitmap)
		// option.setOutputImageData(false);

		// 开启系统拍照声音 (默认:true)
		// option.setEnableCaptureSound(true);

		// 自定义拍照声音RAW ID，默认关闭系统发声
		// option.setCaptureSoundRawId(R.raw.lsq_camera_focus_beep);

		// 自动释放相机在拍摄后 (节省手机内存, 需要手动再次启动)
		// option.setAutoReleaseAfterCaptured(false);

		// 开启长按拍摄 (默认：false)
		option.setEnableLongTouchCapture(true);

		// 开启聚焦声音 (默认:true)
		// option.setEnableFocusBeep(true);

		// 是否需要统一配置参数 (默认false, 取消三星默认降噪，锐化)
		// option.setUnifiedParameters(false);

		// 预览视图实时缩放比例 (默认:0.7f, 实时预览时，缩小到全屏大小比例，提升预览效率， 0 < mPreviewEffectScale
		// <= 1)
		// option.setPreviewEffectScale(0.7f);

		// 视频覆盖区域颜色 (默认：0xFF000000)
		// option.setRegionViewColor(0xFF000000);

		// 开启用户手动设置屏幕比例
		// option.setEnableManualRatio(true);

		TuCameraFragment fragment = option.fragment();
		fragment.setDelegate(this);
		// 开启相机
		mComponent.presentModalNavigationActivity(fragment, true);
	}

	/**
	 * 获取一个拍摄结果
	 * 
	 * @param fragment
	 *            默认相机视图控制器
	 * @param result
	 *            拍摄结果
	 */
	@Override
	public void onTuCameraFragmentCaptured(TuCameraFragment fragment,
			TuSdkResult result)
	{
		fragment.hubDismissRightNow();
		fragment.dismissActivityWithAnim();
		TLog.d("onTuCameraFragmentCaptured: %s", result);
	}

	/**
	 * 获取一个拍摄结果 (异步方法)
	 * 
	 * @param fragment
	 *            默认相机视图控制器
	 * @param result
	 *            拍摄结果
	 * @return 是否截断默认处理逻辑 (默认: false, 设置为True时使用自定义处理逻辑)
	 */
	@Override
	public boolean onTuCameraFragmentCapturedAsync(TuCameraFragment fragment,
			TuSdkResult result)
	{
		TLog.d("onTuCameraFragmentCapturedAsync: %s", result);
		return false;
	}

	/*************************** editComponentHandler ****************************/
	/**
	 * 2-3 图片编辑组件
	 */
	private void editComponentHandler()
	{
		TuEditTurnAndCutOption option = new TuEditTurnAndCutOption();
		// 控制器类型
		// option.setComponentClazz(TuEditTurnAndCutFragment.class);

		// 设置根视图布局资源ID
		// option.setRootViewLayoutId(TuEditTurnAndCutFragment.getLayoutId());

		// 保存到临时文件 (默认不保存, 当设置为true时, TuSdkResult.imageFile, 处理完成后将自动清理原始图片)
		// option.setSaveToTemp(false);

		// 保存到系统相册 (默认不保存, 当设置为true时, TuSdkResult.sqlInfo, 处理完成后将自动清理原始图片)
		// option.setSaveToAlbum(true);

		// 保存到系统相册的相册名称
		// option.setSaveToAlbumName("TuSdk");

		// 照片输出压缩率 (默认:90，0-100 如果设置为0 将保存为PNG格式)
		// option.setOutputCompress(90);

		// 是否开启滤镜支持 (默认: 关闭)
		option.setEnableFilters(true);

		// 需要显示的滤镜名称列表 (如果为空将显示所有自定义滤镜)
		// option.setFilterGroup(new ArrayList<String>());

		// 需要裁剪的长宽
		// option.setCutSize(new TuSdkSize(640, 640));

		// 是否显示处理结果预览图 (默认：关闭，调试时可以开启)
		// option.setShowResultPreview(false);

		TuEditTurnAndCutFragment fragment = option.fragment();

		// 输入的图片对象 (处理优先级: Image > TempFilePath > ImageSqlInfo)
		fragment.setImage(BitmapHelper.getRawBitmap(this, R.raw.sample_photo));

		// 输入的临时文件目录 (处理优先级: Image > TempFilePath > ImageSqlInfo)
		// editFragment.setTempFilePath(result.imageFile);

		// 输入的相册图片对象 (处理优先级: Image > TempFilePath > ImageSqlInfo)
		// editFragment.setImageSqlInfo(result.imageSqlInfo);

		fragment.setDelegate(this);
		// 开启相机
		mComponent.presentModalNavigationActivity(fragment);
	}

	/*************************** editAndCutComponentHandler ****************************/
	/**
	 * 2-4 图片编辑组件 (裁剪)
	 */
	private void editAndCutComponentHandler()
	{
		TuSdk.albumCommponent(this, new TuSdkComponentDelegate()
		{
			@Override
			public void onComponentFinished(TuSdkResult result, Error error,
					TuFragment lastFragment)
			{
				openTuEditTurnAndCut(result, error, lastFragment);
			}
		}).showComponent();
	}

	/**
	 * 开启图片编辑组件 (裁剪)
	 * 
	 * @param result
	 *            返回结果
	 * @param error
	 *            异常信息
	 * @param lastFragment
	 *            最后显示的控制器
	 */
	private void openTuEditTurnAndCut(TuSdkResult result, Error error,
			TuFragment lastFragment)
	{
		if (result == null || lastFragment == null || error != null) return;

		TuEditTurnAndCutOption option = new TuEditTurnAndCutOption();

		// 是否开启滤镜支持 (默认: 关闭)
		option.setEnableFilters(true);

		// 需要裁剪的长宽
		option.setCutSize(new TuSdkSize(640, 640));

		// 是否显示处理结果预览图 (默认：关闭，调试时可以开启)
		option.setShowResultPreview(true);

		TuEditTurnAndCutFragment fragment = option.fragment();

		// 输入的图片对象 (处理优先级: Image > TempFilePath > ImageSqlInfo)
		fragment.setImageSqlInfo(result.imageSqlInfo);

		fragment.setDelegate(this);
		// 开启图片编辑组件 (裁剪)
		lastFragment.pushFragment(fragment);
	}

	/**
	 * 图片编辑完成
	 * 
	 * @param fragment
	 *            旋转和裁剪视图控制器
	 * @param result
	 *            旋转和裁剪视图控制器处理结果
	 */
	@Override
	public void onTuEditTurnAndCutFragmentEdited(
			TuEditTurnAndCutFragment fragment, TuSdkResult result)
	{
		if (!fragment.isShowResultPreview())
		{
			fragment.hubDismissRightNow();
			fragment.dismissActivityWithAnim();
		}
		TLog.d("onTuEditTurnAndCutFragmentEdited: %s", result);
	}

	/**
	 * 图片编辑完成 (异步方法)
	 * 
	 * @param fragment
	 *            旋转和裁剪视图控制器
	 * @param result
	 *            旋转和裁剪视图控制器处理结果
	 * @return 是否截断默认处理逻辑 (默认: false, 设置为True时使用自定义处理逻辑)
	 */
	@Override
	public boolean onTuEditTurnAndCutFragmentEditedAsync(
			TuEditTurnAndCutFragment fragment, TuSdkResult result)
	{
		TLog.d("onTuEditTurnAndCutFragmentEditedAsync: %s", result);
		return false;
	}

	/*************************** avatarComponentHandler ****************************/
	/**
	 * 头像设置组件
	 */
	private void avatarComponentHandler()
	{
		TuSdk.avatarCommponent(this, new TuSdkComponentDelegate()
		{
			@Override
			public void onComponentFinished(TuSdkResult result, Error error,
					TuFragment lastFragment)
			{
				TLog.d("onAvatarComponentReaded: %s | %s", result, error);
			}
		})
		// 在组件执行完成后自动关闭组件
				.setAutoDismissWhenCompleted(true)
				// 显示组件
				.showComponent();
	}

	/*************************** editAdvancedComponentHandler ****************************/
	/**
	 * 4-1 高级图片编辑组件
	 */
	public void editAdvancedComponentHandler()
	{
		TuSdk.albumCommponent(this, new TuSdkComponentDelegate()
		{
			@Override
			public void onComponentFinished(TuSdkResult result, Error error,
					TuFragment lastFragment)
			{
				openEditAdvanced(result, error, lastFragment);
			}
		}).showComponent();
	}

	/**
	 * 开启图片高级编辑
	 * 
	 * @param result
	 * @param error
	 * @param lastFragment
	 */
	private void openEditAdvanced(TuSdkResult result, Error error,
			TuFragment lastFragment)
	{
		if (result == null || lastFragment == null || error != null) return;
		TuSdk.editCommponent(lastFragment, new TuSdkComponentDelegate()
		{
			@Override
			public void onComponentFinished(TuSdkResult result, Error error,
					TuFragment lastFragment)
			{
				TLog.d("onEditAdvancedComponentReaded: %s | %s", result, error);
			}
		})
		// 设置图片
				.setImage(result.image)
				// 设置系统照片
				.setImageSqlInfo(result.imageSqlInfo)
				// 设置临时文件
				.setTempFilePath(result.imageFile)
				// 在组件执行完成后自动关闭组件
				.setAutoDismissWhenCompleted(true)
				// 开启组件
				.showComponent();
	}

	/*************************** avatarComponentHandler ****************************/
	@Override
	public void onComponentError(TuFragment fragment, TuSdkResult result,
			Error error)
	{
		TLog.d("onComponentError: fragment - %s, result - %s, error - %s",
				fragment, result, error);
	}
}