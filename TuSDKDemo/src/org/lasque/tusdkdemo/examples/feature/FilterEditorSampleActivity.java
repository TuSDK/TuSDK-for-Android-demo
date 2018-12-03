/** 
 * TuSdkDemo
 * FilterEditorSampleActivity.java
 *
 * @author 		Yanlin
 * @Date 		2016-1-29 下午2:40:10 
 * @Copyright 	(c) 2015 tusdk.com. All rights reserved.
 * 
 */
package org.lasque.tusdkdemo.examples.feature;

import org.lasque.tusdk.core.TuSdkResult;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.image.BitmapHelper;
import org.lasque.tusdk.impl.activity.TuFragment;
import org.lasque.tusdk.impl.components.filter.TuEditFilterFragment;
import org.lasque.tusdk.impl.components.filter.TuEditFilterFragment.TuEditFilterFragmentDelegate;
import org.lasque.tusdk.impl.components.filter.TuEditFilterOption;
import org.lasque.tusdkdemo.R;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * 滤镜组件示例
 * 
 * @author Yanlin
 */
public class FilterEditorSampleActivity extends FragmentActivity implements TuEditFilterFragmentDelegate
{
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
        setContentView(R.layout.demo_filter_editor);

	    // 组件选项配置
	    // @see-http://tusdk.com/docs/android/image/api/org/lasque/tusdk/impl/components/filter/TuEditFilterOption.html
		TuEditFilterOption option = new TuEditFilterOption();
		
		// 控制器类型，这里指定为扩展类 ExtendFilterFragment
		option.setComponentClazz(ExtendFilterFragment.class);
		// 设置根视图布局资源ID
		// option.setRootViewLayoutId(TuEditFilterFragment.getLayoutId());
		
		// 是否在控制器结束后自动删除临时文件
		option.setAutoRemoveTemp(true);
		// 显示滤镜标题视图
		option.setDisplayFiltersSubtitles(true);
		// 开启在线滤镜
		option.setEnableOnlineFilter(true);
		// 开启用户滤镜历史记录
		option.setEnableFiltersHistory(true);
		
		// 需要显示的滤镜名称列表 (如果为空将显示所有自定义滤镜)
		// 访问文档查看详细用法  @see-http://tusdk.com/docs/android/customize-filter
		// option.setFilterGroup(Arrays.asList(filters));

		// 是否渲染滤镜封面 (使用设置的滤镜直接渲染，需要拥有滤镜列表封面设置权限，请访问TuSDK.com控制台)
		// option.setRenderFilterThumb(true)
		
		// 是否显示处理结果预览图 (默认：关闭，调试时可以开启)
		option.setShowResultPreview(true);
		
		TuEditFilterFragment fragment = option.fragment();
				
		// 输入的图片对象 (处理优先级: Image > TempFilePath > ImageSqlInfo)
		fragment.setImage(BitmapHelper.getRawBitmap(this, R.raw.sample_photo));

		fragment.setDelegate(this);
		
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction =
                fragmentManager.beginTransaction();

        // 将滤镜fragment加入Activity
        transaction.add(R.id.frameLayout, fragment);
        transaction.commit();
	}

	/**
	 * 图片编辑完成
	 * 
	 * @param fragment
	 *            滤镜编辑视图控制器
	 * @param result
	 *            滤镜编辑视图控制器处理结果
	 */
	@Override
	public void onTuEditFilterFragmentEdited(TuEditFilterFragment fragment,
			TuSdkResult result) 
	{
		TLog.d("onTuEditFilterFragmentEdited: %s", result);
		
		fragment.hubDismissRightNow();
		fragment.getActivity().finish();
		// 默认输出为 Bitmap  -> result.image
		
		// 如果保存到临时文件 (默认不保存, 当设置为true时, TuSdkResult.imageFile, 处理完成后将自动清理原始图片)
		// option.setSaveToTemp(true);  ->  result.imageFile

		// 保存到系统相册 (默认不保存, 当设置为true时, TuSdkResult.sqlInfo, 处理完成后将自动清理原始图片)
		// option.setSaveToAlbum(true);  -> result.image
	}

	/**
	 * 图片编辑完成 (异步方法)
	 * 
	 * @param fragment
	 *            滤镜编辑视图控制器
	 * @param result
	 *            滤镜编辑视图控制器处理结果
	 * @return 是否截断默认处理逻辑 (默认: false, 设置为true时使用自定义处理逻辑)
	 */
	@Override
	public boolean onTuEditFilterFragmentEditedAsync(TuEditFilterFragment fragment,
			TuSdkResult result) 
	{
		TLog.d("onTuEditFilterFragmentEditedAsync: %s", result);
		return false;
	}

	@Override
	public void onComponentError(TuFragment fragment, TuSdkResult result,
			Error error) 
	{
		TLog.d("onComponentError: fragment - %s, result - %s, error - %s", fragment, result, error);
	}
}
