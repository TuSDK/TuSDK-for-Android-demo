/** 
 * TuSdkDemo
 * WipeAndFilterEditorSample.java
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
import org.lasque.tusdk.impl.components.filter.TuEditWipeAndFilterFragment;
import org.lasque.tusdk.impl.components.filter.TuEditWipeAndFilterFragment.TuEditWipeAndFilterFragmentDelegate;
import org.lasque.tusdk.impl.components.filter.TuEditWipeAndFilterOption;
import org.lasque.tusdk.modules.components.TuSdkHelperComponent;
import org.lasque.tusdkdemo.R;
import org.lasque.tusdkdemo.SampleBase;
import org.lasque.tusdkdemo.SampleGroup.GroupType;

import android.app.Activity;

/**
 * 模糊涂抹功能示例, 模糊功能需要权限验证，使用前请确认已获取相应权限。
 *
 * 更多服务细节请参考: http://tusdk.com/services
 *   
 * @author Yanlin
 */
public class WipeAndFilterEditorSample extends SampleBase implements TuEditWipeAndFilterFragmentDelegate
{
	/**
	 * 模糊涂抹功能示例
	 */
	public WipeAndFilterEditorSample()
	{
		super(GroupType.FeatureSample, R.string.sample_comp_BlurComponent);
	}

	/* 
	 * 显示视图
	 */
	@Override
	public void showSample(Activity activity)
	{
	    if (activity == null) return;
	    // see-http://tusdk.com/docs/android/api/org/lasque/tusdk/impl/components/base/TuSdkHelperComponent.html
	    this.componentHelper = new TuSdkHelperComponent(activity);
	    
	    // 组件选项配置
	    // @see-http://tusdk.com/docs/android/api/org/lasque/tusdk/impl/components/filter/TuEditWipeAndFilterOption.html
	    TuEditWipeAndFilterOption option = new TuEditWipeAndFilterOption();
	    
	    // 是否在控制器结束后自动删除临时文件
	    option.setAutoRemoveTemp(true);
	    
	    // 设置笔刷效果强度 (默认: 0.2, 范围为0 ~ 1，值为1时强度最高)
	    // option.setBrushStrength(0.2f);
	    
	    // 设置默认的笔刷大小 (默认: BrushSize.SizeType.MediumBrush，中等粗细)
	    // option.setDefaultBrushSize(BrushSize.SizeType.MediumBrush);
	    
	    // 是否显示处理结果预览图 (默认：关闭，调试时可以开启)
	    option.setShowResultPreview(true);
	    
	    // 显示放大镜 (默认: true)
	    // option.setDisplayMagnifier(true);
	    
	    // 允许撤销的次数 (默认: 5)
	    // option.setMaxUndoCount(5);
	    
	    TuEditWipeAndFilterFragment fragment = option.fragment();
	    
		// 输入的图片对象 (处理优先级: Image > TempFilePath > ImageSqlInfo)
		fragment.setImage(BitmapHelper.getRawBitmap(activity, R.raw.sample_photo));
	    
	    fragment.setDelegate(this);
	    
	    // 开启模糊涂抹界面
	    componentHelper.pushModalNavigationActivity(fragment, true);
	}

	/**
	 * 图片编辑完成
	 * 
	 * @param fragment
	 *            模糊涂抹视图控制器
	 * @param result
	 *            模糊涂抹视图控制器处理结果
	 */
	@Override
	public void onTuEditWipeAndFilterFragmentEdited(
			TuEditWipeAndFilterFragment fragment, TuSdkResult result) 
	{
		fragment.hubDismissRightNow();
		fragment.dismissActivityWithAnim();
	    TLog.d("onTuEditWipeAndFilterFragmentEdited: %s", result);
	    
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
	 *           模糊涂抹视图控制器
	 * @param result
	 *            模糊涂抹视图控制器处理结果
	 * @return 是否截断默认处理逻辑 (默认: false, 设置为true时使用自定义处理逻辑)
	 */
	@Override
	public boolean onTuEditWipeAndFilterFragmentEditedAsync(
			TuEditWipeAndFilterFragment fragment, TuSdkResult result)
	{
	    TLog.d("onTuEditWipeAndFilterFragmentEditedAsync: %s", result);
	    return false;
	}
	
	@Override
	public void onComponentError(TuFragment fragment, TuSdkResult result,
			Error error) 
	{
		TLog.d("onComponentError: fragment - %s, result - %s, error - %s", fragment, result, error);
	}

}
