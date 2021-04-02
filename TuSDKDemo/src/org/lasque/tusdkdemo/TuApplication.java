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

import org.lasque.tusdkpulse.core.TuSdk;
import org.lasque.tusdkpulse.core.TuSdkApplication;
import org.lasque.tusdkpulse.core.utils.TLog;

import com.tencent.bugly.crashreport.CrashReport;
import com.tusdk.pulse.filter.TuSDKFilter;

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
		 ************************* TuSDK 集成三部曲 *************************
		 * 1. 在官网注册开发者账户
		 * 2. 下载SDK和示例代码
		 * 3. 创建应用，获取appkey，导出资源包
		 ************************* TuSDK 集成三部曲 *************************
		 * 
		 * 关于TuSDK体积 (约2M大小)
		 * Android 编译知识：
		 * APK文件包含了Java代码，JNI库和资源文件；
		 * JNI库包含arm64-v8a,armeabi等不同CPU的编译结果的集合，这些都会编译进 APK 文件；
		 * 在安装应用时，系统会自动选择最合适的JNI版本，其他版本不会占用空间；
		 * 参考TuSDK Demo的APK 大小，除去资源和JNI库，SDK本身的大小约2M；
		 * 开发文档:https://tutucloud.com/doc
		 */
		
		// 初始化 Bugly SDK
		CrashReport.initCrashReport(getApplicationContext(), "393d990901", true);

		TuSDKFilter.register();


		// 设置资源类，当 Application id 与 Package Name 不相同时，必须手动调用该方法, 且在 init 之前执行。
		TuSdk.setResourcePackageClazz(org.lasque.tusdkdemo.R.class);
		
		// 自定义 .so 文件路径，在 init 之前调用
		// NativeLibraryHelper.shared().mapLibrary(NativeLibType.LIB_CORE, "libtusdk-core.so 文件路径");

		// 设置输出状态，建议在接入阶段开启该选项，以便定位问题。
		this.setEnableLog(true);
		/**
	     *  初始化SDK，应用密钥是您的应用在 TuSDK 的唯一标识符。每个应用的包名(Bundle Identifier)、密钥、资源包(滤镜、贴纸等)三者需要匹配，否则将会报错。
	     *
	     *  @param appkey 应用秘钥 (请前往 https://tutucloud.com/docs/help/package-name-and-app-key 申请秘钥)
	     */
		this.initPreLoader(this.getApplicationContext(), "60b0e73b850b25b3-04-ewdjn1");
//
//		this.initPreLoader(this.getApplicationContext(), "619925792c27278a-04-dhr9s1");



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

	@Override
	public void onTrimMemory(int level) {
		super.onTrimMemory(level);
		switch (level){
			case TRIM_MEMORY_RUNNING_CRITICAL:
			case TRIM_MEMORY_COMPLETE:
				// 清理共享缓存[当设备内存报警时执行该方法]
//				TuSdk.purgeSharedCache();
				TLog.w("TuSdk purgeSharedCache");
				break;
			default:
				break;
		}
	}
}