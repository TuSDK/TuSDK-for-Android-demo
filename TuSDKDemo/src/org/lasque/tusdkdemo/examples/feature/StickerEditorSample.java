/** 
 * TuSdkDemo
 * StickerEditorSample.java
 *
 * @author 		Yanlin
 * @Date 		2016-1-29 下午2:48:10 
 * @Copyright 	(c) 2015 tusdk.com. All rights reserved.
 * 
 */
package org.lasque.tusdkdemo.examples.feature;

import org.lasque.tusdk.core.TuSdkResult;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.image.BitmapHelper;
import org.lasque.tusdk.impl.activity.TuFragment;
import org.lasque.tusdk.impl.components.sticker.TuEditStickerFragment;
import org.lasque.tusdk.impl.components.sticker.TuEditStickerFragment.TuEditStickerFragmentDelegate;
import org.lasque.tusdk.impl.components.sticker.TuEditStickerOption;
import org.lasque.tusdk.modules.components.TuSdkHelperComponent;
import org.lasque.tusdkdemo.R;
import org.lasque.tusdkdemo.SampleBase;
import org.lasque.tusdkdemo.SampleGroup.GroupType;

import android.app.Activity;

/**
 * 贴纸组件示例
 * 
 * @author Yanlin
 */
public class StickerEditorSample extends SampleBase implements TuEditStickerFragmentDelegate
{
	
	/**
	 * 贴纸组件示例
	 */
	public StickerEditorSample()
	{
		super(GroupType.FeatureSample, R.string.sample_comp_StickerComponent);
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
	    // @see-http://tusdk.com/docs/android/api/org/lasque/tusdk/impl/components/sticker/TuEditStickerOption.html
	    TuEditStickerOption option = new TuEditStickerOption();
	    
	    // 是否在控制器结束后自动删除临时文件
	    option.setAutoRemoveTemp(true);
	    // 设置贴纸单元格的高度
	    option.setGridHeight(150);
	    // 设置贴纸单元格的间距
	    option.setGridPadding(8);
	    
	    // 是否显示处理结果预览图 (默认：关闭，调试时可以开启)
	    option.setShowResultPreview(true);
	    
	    TuEditStickerFragment fragment = option.fragment();

		// 输入的图片对象 (处理优先级: Image > TempFilePath > ImageSqlInfo)
		fragment.setImage(BitmapHelper.getRawBitmap(activity, R.raw.sample_photo));
		
	    fragment.setDelegate(this);
	    
	    // 开启贴纸编辑界面
	    componentHelper.pushModalNavigationActivity(fragment, true);
	}

	  /**
	   * 图片编辑完成
	   * 
	   * @param fragment
	   *            贴纸编辑视图控制器
	   * @param result
	   *            贴纸编辑视图控制器处理结果
	   */
	  @Override
	  public void onTuEditStickerFragmentEdited(TuEditStickerFragment fragment,
	      TuSdkResult result)
	  {
		  fragment.hubDismissRightNow();
		  fragment.dismissActivityWithAnim();
	      TLog.d("onTuEditStickerFragmentEdited: %s", result);
	    
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
	   *            贴纸编辑视图控制器
	   * @param result
	   *            贴纸编辑视图控制器处理结果
	   * @return 是否截断默认处理逻辑 (默认: false, 设置为true时使用自定义处理逻辑)
	   */
	  @Override
	  public boolean onTuEditStickerFragmentEditedAsync(
	      TuEditStickerFragment fragment, TuSdkResult result) 
	  {
		  TLog.d("onTuEditStickerFragmentEditedAsync: %s", result);
	      return false;
	  }

	  @Override
	  public void onComponentError(TuFragment fragment, TuSdkResult result,
	      Error error) 
	  {
		  TLog.d("onComponentError: fragment - %s, result - %s, error - %s", fragment, result, error);
	  }
}
