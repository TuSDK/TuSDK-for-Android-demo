/** 
 * TuSDK
 * TuApplication.java
 *
 * author 		Clear
 * Date 		2014-11-18 下午5:14:34 
 * Copyright 	(c) 2014 tusdk.com. All rights reserved.
 * 
 */
package org.lasque.tusdkdemo;

import org.lasque.tusdk.core.TuSdkApplication;

/**
 * 应用对象
 * 
 * @author Clear
 */
public class TuApplication extends TuSdkApplication
{
	/** 应用程序创建 */
	@Override
	public void onCreate()
	{
		super.onCreate();

		/**
		 * ！！！！！！！！！！！！！！！！！！！！！！！！！特别提示信息要长！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！
		 * 关于TuSDK体积（SDK编译后仅为0.9MB）：
		 * 1,如果您不需要使用本地贴纸功能，或仅需要使用在线贴纸功能，请删除/app/assets/TuSDK.bundle/stickers文件夹
		 * 2,如果您仅需要几款滤镜，您可以删除/app/assets/TuSDK.bundle/textures下的*.gsce文件
		 * 3,如果您不需要使用滤镜功能，请删除/app/assets/TuSDK.bundle/textures文件夹
		 * 4,TuSDK在线管理功能请访问：http://tusdk.com/
		 * 开发文档:http://tusdk.com/docs/android/api/
		 */

		// 设置输出状态
		this.setEnableLog(true);
		/**
	     *  初始化SDK，应用密钥是您的应用在 TuSDK 的唯一标识符。每个应用的包名(Bundle Identifier)、密钥、资源包(滤镜、贴纸等)三者需要匹配，否则将会报错。
	     *
	     *  @param appkey 应用秘钥 (请前往 http://tusdk.com 申请秘钥)
	     */
		this.initPreLoader(this.getApplicationContext(), "12aa4847a3a9ce68-04-ewdjn1");
		
		/**
	     *  指定开发模式,需要与lsq_tusdk_configs.json中masters.key匹配， 如果找不到devType将默认读取master字段
	     *  如果一个应用对应多个包名，则可以使用这种方式来进行集成调试。
	     */
		// this.initPreLoader(this.getApplicationContext(), "12aa4847a3a9ce68-04-ewdjn1", "debug");

		// 如果不想继承TuSdkApplication，直接在自定义Application.onCreate()方法中调用以下方法
		// 初始化全局变量
		// TuSdk.enableDebugLog(true);
		// 开发ID (请前往 http://tusdk.com 获取您的APP 开发秘钥)
		// TuSdk.init(this.getApplicationContext(), "12aa4847a3a9ce68-04-ewdjn1");
		// 需要指定开发模式 需要与lsq_tusdk_configs.json中masters.key匹配， 如果找不到devType将默认读取master字段
		// TuSdk.init(this.getApplicationContext(), "12aa4847a3a9ce68-04-ewdjn1", "debug");
	}
}