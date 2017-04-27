/** 
 * TuSdkDemo
 * CameraComponentSimple.java
 *
 * @author 		Clear
 * @Date 		2015-4-21 下午1:40:10 
 * @Copyright 	(c) 2015 tusdk.com. All rights reserved.
 * 
 */
package org.lasque.tusdkdemo.examples.suite;

import org.lasque.tusdk.core.TuSdkResult;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.TuSdkWaterMarkOption;
import org.lasque.tusdk.core.utils.TuSdkWaterMarkOption.TextPosition;
import org.lasque.tusdk.core.utils.TuSdkWaterMarkOption.WaterMarkPosition;
import org.lasque.tusdk.core.utils.hardware.CameraHelper;
import org.lasque.tusdk.core.utils.image.BitmapHelper;
import org.lasque.tusdk.impl.activity.TuFragment;
import org.lasque.tusdk.impl.components.camera.TuCameraFragment;
import org.lasque.tusdk.impl.components.camera.TuCameraFragment.TuCameraFragmentDelegate;
import org.lasque.tusdk.impl.components.camera.TuCameraOption;
import org.lasque.tusdk.modules.components.TuSdkHelperComponent;
import org.lasque.tusdkdemo.R;
import org.lasque.tusdkdemo.SampleBase;
import org.lasque.tusdkdemo.SampleGroup.GroupType;

import android.app.Activity;

/**
 * 相机组件范例
 * 
 * @author Clear
 */
public class CameraComponentSample extends SampleBase implements TuCameraFragmentDelegate
{
	/** 相机组件范例 */
	public CameraComponentSample()
	{
		super(GroupType.SuiteSample, R.string.sample_CameraComponent);
	}

	/** 显示范例 */
	@Override
	public void showSample(Activity activity)
	{
		if (activity == null) return;

		// 如果不支持摄像头显示警告信息
		if (CameraHelper.showAlertIfNotSupportCamera(activity)) return;
		// 组件选项配置
		// @see-http://tusdk.com/docs/android/api/org/lasque/tusdk/impl/components/camera/TuCameraOption.html
		TuCameraOption option = new TuCameraOption();

		// 控制器类型
		// option.setComponentClazz(TuCameraFragment.class);

		// 设置根视图布局资源ID
		// option.setRootViewLayoutId(TuCameraFragment.getLayoutId());

		// 保存到临时文件 (默认不保存, 当设置为true时, TuSdkResult.imageFile, 处理完成后将自动清理原始图片)
		// option.setSaveToTemp(false);

		// 保存到系统相册 (默认不保存, 当设置为true时, TuSdkResult.sqlInfo, 处理完成后将自动清理原始图片)
		option.setSaveToAlbum(true);

		// 保存到系统相册的相册名称
		// option.setSaveToAlbumName("TuSdk");

		// 照片输出压缩率 (默认:90，0-100 如果设置为0 将保存为PNG格式)
		// option.setOutputCompress(90);

		// 相机方向 (默认:CameraFacing.Back)
		// option.setAvPostion(CameraFacing.Front);

		// 照片输出图片长宽 (默认：全屏)
		// option.setOutputSize(new TuSdkSize(1440, 1920));
		// 闪关灯模式
		// option.setDefaultFlashMode(CameraFlash.Off);

		// 是否开启滤镜支持 (默认: 关闭)
		option.setEnableFilters(true);

		// 默认是否显示滤镜视图 (默认: 不显示, 如果mEnableFilters = false, mShowFilterDefault将失效)
		option.setShowFilterDefault(true);

		// 滤镜组行视图宽度 (单位:DP)
		// option.setGroupFilterCellWidthDP(60);

		// 滤镜组选择栏高度 (单位:DP)
		// option.setFilterBarHeightDP(80);

		// 滤镜分组列表行视图布局资源ID (默认:
		// tusdk_impl_component_widget_group_filter_group_view，如需自定义请继承自
		// GroupFilterGroupView)
		// option.setGroupTableCellLayoutId(GroupFilterGroupView.getLayoutId());

		// 滤镜列表行视图布局资源ID (默认:
		// tusdk_impl_component_widget_group_filter_item_view，如需自定义请继承自
		// GroupFilterItemView)
		// option.setFilterTableCellLayoutId(GroupFilterItemView.getLayoutId());

		// 开启滤镜配置选项
		option.setEnableFilterConfig(false);

		// 需要显示的滤镜名称列表 (如果为空将显示所有自定义滤镜)
		// 滤镜名称参考 TuSDK.bundle/others/lsq_tusdk_configs.json
		// filterGroups[]->filters[]->name lsq_filter_%{Brilliant}
		// String[] filters = { "SkinNature", "SkinPink", "SkinJelly",
		// "SkinNoir",
		// "SkinRuddy", "SkinPowder", "SkinSugar" };
		// option.setFilterGroup(Arrays.asList(filters));

		// 是否保存最后一次使用的滤镜
		option.setSaveLastFilter(true);

		// 自动选择分组滤镜指定的默认滤镜
		option.setAutoSelectGroupDefaultFilter(true);

		// 开启用户滤镜历史记录
		option.setEnableFiltersHistory(true);

		// 开启在线滤镜
		option.setEnableOnlineFilter(true);

		// 显示滤镜标题视图
		option.setDisplayFiltersSubtitles(true);

		// 触摸聚焦视图ID (默认: tusdk_impl_component_camera_focus_touch_view)
		// option.setFocusTouchViewId(TuFocusTouchView.getLayoutId());

		// 视频视图显示比例 (默认: 0, 全屏)
		// option.setCameraViewRatio(0);

		// 是否直接输出图片数据 (默认:false，输出已经处理好的图片Bitmap)
		// 设置为true都需使用 TuSdkResult.imageData获取一个byte[]数组
		// option.setOutputImageData(false);

		// 禁用持续自动对焦 (默认：false)
		// option.setDisableContinueFoucs(true);

		// 禁用系统拍照声音 (默认:false)
		// option.setDisableCaptureSound(true);

		// 自定义拍照声音RAW ID，默认关闭系统发声
		// option.setCaptureSoundRawId(R.raw.lsq_camera_focus_beep);

		// 是否开启音量键拍照功能，默认关闭
		option.setEnableCaptureWithVolumeKeys(true);
		// 自动释放相机在拍摄后 (节省手机内存, 需要手动再次启动)
		// option.setAutoReleaseAfterCaptured(false);

		// 开启长按拍摄 (默认：false)
		option.setEnableLongTouchCapture(true);

		// 开启调节焦距 (默认：true)
		// option.setEnableFocalDistance(false);
		// 设置焦距初始值(默认：0, 0-getMaxZoom())
		// option.setFocalDistanceScale(0);
		
		// 禁用聚焦声音 (默认:false)
		// option.setDisableFocusBeep(true);

		// 是否需要统一配置参数 (默认false, 取消三星默认降噪，锐化)
		// option.setUnifiedParameters(false);

		// 预览视图实时缩放比例 (默认:0.75f, 实时预览时，缩小到全屏大小比例，提升预览效率， 0 < mPreviewEffectScale
		// <= 1)
		// option.setPreviewEffectScale(0.7f);

		// 视频覆盖区域颜色 (默认：0xFF000000)
		// option.setRegionViewColor(0xFF000000);

		// 禁用前置摄像头自动水平镜像 (默认: false，前置摄像头拍摄结果自动进行水平镜像)
		// option.setDisableMirrorFrontFacing(true);

		// 是否开启脸部追踪 (需要相机人脸追踪权限，请访问tusdk.com 控制台开启权限)
		option.enableFaceDetection = true;
		
		// 设置水印选项 (默认为空，如果设置不为空，则输出的图片上将带有水印)
		// option.setWaterMarkOption(getWaterMarkOption(activity));

		TuCameraFragment fragment = option.fragment();
		fragment.setDelegate(this);

		// see-http://tusdk.com/docs/android/api/org/lasque/tusdk/impl/components/base/TuSdkHelperComponent.html
		this.componentHelper = new TuSdkHelperComponent(activity);
		// 开启相机
		this.componentHelper.presentModalNavigationActivity(fragment, true);
	}
	
	/**
	 * 水印设置
	 * 
	 * @return
	 */
	public TuSdkWaterMarkOption getWaterMarkOption(Activity activity)
	{
		TuSdkWaterMarkOption option = new TuSdkWaterMarkOption();
		
		// 文字或者图片需要至少设置一个
	    // 设置水印文字, 支持图文混排、图片或文字
	    option.setMarkText("");
	    
	    // 设置文字颜色 (默认:#FFFFFF)
	    option.setMarkTextColor("#FFFFFF");
	    
	    // 文字大小 (默认: 24 SP)
	    option.setMarkTextSize(24);
	    
	    // 文字阴影颜色 (默认:#000000)
	    option.setMarkTextShadowColor("#000000");
	    
	    // 设置水印图片, 支持图文混排、图片或文字
	    option.setMarkImage(BitmapHelper.getRawBitmap(activity, R.raw.sample_watermark));
	    
	    // 文字和图片顺序 (仅当图片和文字都非空时生效，默认: 文字在右)
	    option.setMarkTextPosition(TextPosition.Right);

	    // 设置水印位置 (默认: WaterMarkPosition.BottomRight)
	    option.setMarkPosition(WaterMarkPosition.BottomRight);
	    
	    // 设置水印距离图片边距 (默认: 6dp)
	    option.setMarkMargin(6);
	    
	    // 文字和图片间距 (默认: 2dp)
	    option.setMarkTextPadding(5);
	    
	    return option;
	}

	/**
	 * 获取一个拍摄结果。
	 * 
	 * 相机的拍摄结果是TuSdkResult对象，依照设置，输出结果可能是 Bitmap、File或者ImageSqlInfo。
	 * 在本例中，拍摄结束后直接关闭了相机界面，依照需求，还可以将拍摄结果作为输入源传给编辑组件，从而实现拍摄编辑一体操作。
	 * 欢迎访问文档中心 http://tusdk.com/doc 查看更多示例。
	 * 
	 * @param fragment
	 *            默认相机视图控制器
	 * @param result
	 *            拍摄结果
	 */
	@Override
	public void onTuCameraFragmentCaptured(TuCameraFragment fragment, TuSdkResult result)
	{
		fragment.hubDismissRightNow();
		fragment.dismissActivityWithAnim();
		TLog.d("onTuCameraFragmentCaptured: %s", result);
		
		// 默认输出为 Bitmap  -> result.image
		
		// 如果保存到临时文件 (默认不保存, 当设置为true时, TuSdkResult.imageFile, 处理完成后将自动清理原始图片)
		// option.setSaveToTemp(true);  ->  result.imageFile

		// 保存到系统相册 (默认不保存, 当设置为true时, TuSdkResult.sqlInfo, 处理完成后将自动清理原始图片)
		// option.setSaveToAlbum(true);  -> result.image
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
	public boolean onTuCameraFragmentCapturedAsync(TuCameraFragment fragment, TuSdkResult result)
	{
		TLog.d("onTuCameraFragmentCapturedAsync: %s", result);
		return false;
	}

	/**
	 * 请求从相机界面跳转到相册界面。只有 设置mDisplayAlbumPoster为true (默认:false) 才会发生该事件
	 * 
	 * @param fragment
	 *            系统相册控制器
	 */
	@Override
	public void onTuAlbumDemand(TuCameraFragment fragment)
	{

	}

	@Override
	public void onComponentError(TuFragment fragment, TuSdkResult result, Error error)
	{
		TLog.d("onComponentError: fragment - %s, result - %s, error - %s", fragment, result, error);
	}

}