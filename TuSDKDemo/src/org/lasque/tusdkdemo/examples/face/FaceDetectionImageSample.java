/** 
 * TuSdkDemo
 * FaceDetectionImageSample.java
 *
 * @author 		Clear
 * @Date 		2016-3-7 下午12:52:13 
 * @Copyright 	(c) 2016 tusdk.com. All rights reserved.
 * 
 */
package org.lasque.tusdkdemo.examples.face;

import org.lasque.tusdk.TuSdkGeeV1;
import org.lasque.tusdk.core.TuSdkResult;
import org.lasque.tusdk.impl.activity.TuFragment;
import org.lasque.tusdk.modules.components.TuSdkComponent.TuSdkComponentDelegate;
import org.lasque.tusdkdemo.R;
import org.lasque.tusdkdemo.SampleBase;
import org.lasque.tusdkdemo.SampleGroup.GroupType;

import android.app.Activity;

/**
 * 人脸检测 图片
 * 
 * @author Clear
 */
public class FaceDetectionImageSample extends SampleBase
{

	public FaceDetectionImageSample()
	{
		super(GroupType.APISample, R.string.sample_api_face_detection_image);
	}

	@Override
	public void showSample(Activity activity)
	{
		if (activity == null) return;

		// 开启相册选择照片
		TuSdkGeeV1.albumCommponent(activity, new TuSdkComponentDelegate()
		{
			@Override
			public void onComponentFinished(TuSdkResult result, Error error, TuFragment lastFragment)
			{
				openFaceDetction(result, error, lastFragment);
			}
		}).showComponent();

	}

	protected void openFaceDetction(TuSdkResult result, Error error, TuFragment lastFragment)
	{
		TuEditFaceDetectionImageFragment fragment = new TuEditFaceDetectionImageFragment();

		// 输入的图片对象 (处理优先级: Image > TempFilePath > ImageSqlInfo)
		fragment.setImageSqlInfo(result.imageSqlInfo);
		// 开启人脸识别
		lastFragment.pushFragment(fragment);
	}
}
