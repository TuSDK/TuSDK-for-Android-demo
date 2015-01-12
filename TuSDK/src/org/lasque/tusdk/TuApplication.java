/** 
 * TuSDK
 * TuApplication.java
 *
 * author 		Clear
 * Date 		2014-11-18 下午5:14:34 
 * Copyright 	(c) 2014 Lasque. All rights reserved.
 * 
 */
package org.lasque.tusdk;

import org.lasque.tusdk.core.TuSdk;
import org.lasque.tusdk.core.TuSdkApplication;

/**
 * 应用对象
 * author Clear
 */
public class TuApplication extends TuSdkApplication
{
	/**
	 * 应用程序创建
	 */
	@Override
	public void onCreate()
	{
		super.onCreate();
		// 设置输出状态
		this.setEnableLog(true);
		// 初始化SDK (请前往 http://tusdk.com 获取您的APP 开发秘钥)
		this.initPreLoader(this.getApplicationContext(), "57ba8da77fb46580-04");

		// 如果不想继承TuApplication，直接在自定义Application.onCreate()方法中调用以下方法
		// 初始化全局变量
		// TuSdk.enableDebugLog(true);
		// 开发ID (请前往 http://tusdk.com 获取您的APP 开发秘钥)
		// TuSdk.init(this.getApplicationContext(), "57ba8da77fb46580-04");

		// 可选：配置滤镜列表
		TuSdk.filterManager().configSampleTask(
				TuSdk.SAMPLE_DEFAULT_ORIGIN_IMAGE_RAW, TuSdk.VERSION, "Normal", 
				/***************************磨皮测试功能，发布正式产品时，请删除该部分选项 ************************************/
				"SkinTwiceMixed","SkinTwiceMixedSigma", "SkinRGBSpace", "SkinColorMixed", "BokehBlur",
				/***************************磨皮测试功能，发布正式产品时，请删除该部分选项  ************************************/
				"Artistic", "Brilliant", "Cheerful", "Clear", "Fade", "Forest",
				"Gloss", "Harmony", "Instant", "Lightup", "Morning", "Newborn",
				"Noir", "Relaxed", "Rough", "Thick", "Vintage");
	}
}
