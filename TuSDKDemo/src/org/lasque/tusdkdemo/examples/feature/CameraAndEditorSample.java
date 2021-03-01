/** 
 * TuSdkDemo
 * CameraAndEditorSample.java
 *
 * @author 		Yanlin
 * @Date 		2016-1-26 下午1:40:10 
 * @Copyright 	(c) 2015 tusdk.com. All rights reserved.
 * 
 */
package org.lasque.tusdkdemo.examples.feature;

import org.lasque.tusdkpulse.TuSdkGeeV1;
import org.lasque.tusdkpulse.core.TuSdkResult;
import org.lasque.tusdkpulse.core.utils.TLog;
import org.lasque.tusdkpulse.core.utils.hardware.CameraHelper;
import org.lasque.tusdkpulse.impl.activity.TuFragment;
import org.lasque.tusdkpulse.impl.components.TuEditMultipleComponent;
import org.lasque.tusdkpulse.impl.components.camera.TuCameraFragment;
import org.lasque.tusdkpulse.impl.components.camera.TuCameraFragment.TuCameraFragmentDelegate;
import org.lasque.tusdkpulse.impl.components.camera.TuCameraOption;
import org.lasque.tusdkpulse.modules.components.TuSdkComponent.TuSdkComponentDelegate;
import org.lasque.tusdkpulse.modules.components.TuSdkHelperComponent;
import org.lasque.tusdkdemo.R;
import org.lasque.tusdkdemo.SampleBase;
import org.lasque.tusdkdemo.SampleGroup.GroupType;

import android.app.Activity;

/**
 * 拍照 + 编辑示例
 * 
 * @author Yanlin
 */
public class CameraAndEditorSample extends SampleBase implements TuCameraFragmentDelegate
{
	public CameraAndEditorSample()
	{
		super(GroupType.FeatureSample, R.string.sample_comp_CameraAndEditor);
	}

	@Override
	public void showSample(Activity activity)
	{
		showCamera(activity);
	}
	
	private void showCamera(Activity activity)
	{
		if (activity == null) return;

		// 如果不支持摄像头显示警告信息
		if (CameraHelper.showAlertIfNotSupportCamera(activity)) return;
		// 组件选项配置
		// @see-http://tusdk.com/docs/android/image/api/org/lasque/tusdk/impl/components/camera/TuCameraOption.html
		TuCameraOption option = new TuCameraOption();

		// 保存到临时文件 (默认不保存, 当设置为true时, TuSdkResult.imageFile, 处理完成后将自动清理原始图片)
		option.setSaveToTemp(false);

		// 保存到系统相册 (默认不保存, 当设置为true时, TuSdkResult.sqlInfo, 处理完成后将自动清理原始图片)
		option.setSaveToAlbum(false);
		
		// 照片输出压缩率 (默认:90，0-100 如果设置为0 将保存为PNG格式)
		option.setOutputCompress(100);

		// 是否开启滤镜支持 (默认: 关闭)
		option.setEnableFilters(true);

		// 默认是否显示滤镜视图 (默认: 不显示, 如果mEnableFilters = false, mShowFilterDefault将失效)
		option.setShowFilterDefault(true);

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
		
		// 是否开启脸部追踪 (需要相机人脸追踪权限，请访问tusdk.com 控制台开启权限)
		option.enableFaceDetection = true;

		TuCameraFragment fragment = option.fragment();
		fragment.setDelegate(this);

		// see-http://tusdk.com/docs/android/image/api/org/lasque/tusdk/modules/components/TuSdkHelperComponent.html
		this.componentHelper = new TuSdkHelperComponent(activity);
		// 开启相机
		this.componentHelper.presentModalNavigationActivity(fragment, true);
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
		
		
		// 组件委托
		TuSdkComponentDelegate delegate = new TuSdkComponentDelegate()
		{
			@Override
			public void onComponentFinished(TuSdkResult result, Error error, TuFragment lastFragment)
			{
				TLog.d("onEditMultipleComponentReaded: %s | %s", result, error);
				
				// 默认输出为 Bitmap  -> result.image
				
				// 如果保存到临时文件 (默认不保存, 当设置为true时, TuSdkResult.imageFile, 处理完成后将自动清理原始图片)
				// option.setSaveToTemp(true);  ->  result.imageFile

				// 保存到系统相册 (默认不保存, 当设置为true时, TuSdkResult.sqlInfo, 处理完成后将自动清理原始图片)
				// option.setSaveToAlbum(true);  -> result.image
			}
		};
				
		TuEditMultipleComponent component = TuSdkGeeV1.editMultipleCommponent(fragment, delegate);
		
		component.componentOption().editFilterOption().setEnableFilterConfig(true);
		
		component.setImage(result.image)
		// 设置系统照片
				.setImageSqlInfo(result.imageSqlInfo)
				// 设置临时文件
				.setTempFilePath(result.imageFile)
				// 在组件执行完成后自动关闭组件
				.setAutoDismissWhenCompleted(true)
				// 开启组件
				.showComponent();
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
