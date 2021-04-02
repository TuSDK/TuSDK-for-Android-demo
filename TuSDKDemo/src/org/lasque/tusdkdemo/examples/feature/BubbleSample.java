package org.lasque.tusdkdemo.examples.feature;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.tusdk.pulse.utils.AssetsMapper;

import org.lasque.tusdkdemo.R;
import org.lasque.tusdkdemo.SampleBase;
import org.lasque.tusdkdemo.SampleGroup;
import org.lasque.tusdkdemo.utils.Constants;
import org.lasque.tusdkpulse.core.TuSdkContext;
import org.lasque.tusdkpulse.modules.components.TuSdkHelperComponent;

/**
 * TuSDK
 * org.lasque.tusdkdemo.examples.feature
 * FPDemo
 *
 * @author H.ys
 * @Date 2021/2/2  19:57
 * @Copyright (c) 2020 tusdk.com. All rights reserved.
 */
public class BubbleSample extends SampleBase {
    /**
     * 范例分组头部信息
     *
     * @param groupId    分组ID
     * @param titleResId
     */
    public BubbleSample() {
        super(SampleGroup.GroupType.FeatureSample, R.string.sample_comp_BubbleComponent);
    }

    @Override
    public void showSample(Activity activity) {

        if (activity == null) return;

        SharedPreferences sp = TuSdkContext.context().getSharedPreferences("TU-TTF", Context.MODE_PRIVATE);
        AssetsMapper assetsMapper = new AssetsMapper(TuSdkContext.context());
        if (!sp.contains(Constants.BUBBLE_FONTS)){
            assetsMapper.mapAsset("AliHYAiHei.ttf");
            assetsMapper.mapAsset("NotoColorEmoji.ttf");
            assetsMapper.mapAsset("SOURCEHANSANSCN-LIGHT.OTF");
            assetsMapper.mapAsset("SOURCEHANSANSCN-REGULAR.OTF");
            assetsMapper.mapAsset("站酷快乐体2016修订版_0.ttf");
            String path = TuSdkContext.context().getExternalCacheDirs()[0].getAbsolutePath() + "/assets";
            sp.edit().putString(Constants.BUBBLE_FONTS,path).apply();
        }

//        if (!sp.contains(Constants.BUBBLE_1)){
//            String path = assetsMapper.mapAsset("bubbles/lsq_bubble_1.bt");
//            sp.edit().putString(Constants.BUBBLE_1,path).apply();
//        }
//        if (!sp.contains(Constants.BUBBLE_2)){
//            String path = assetsMapper.mapAsset("bubbles/lsq_bubble_2.bt");
//            sp.edit().putString(Constants.BUBBLE_2,path).apply();
//        }
//        if (!sp.contains(Constants.BUBBLE_3)){
//            String path = assetsMapper.mapAsset("bubbles/lsq_bubble_3.bt");
//            sp.edit().putString(Constants.BUBBLE_3,path).apply();
//        }
//        if (!sp.contains(Constants.BUBBLE_4)){
//            String path = assetsMapper.mapAsset("bubbles/lsq_bubble_4.bt");
//            sp.edit().putString(Constants.BUBBLE_4,path).apply();
//        }
        if (!sp.contains(Constants.BUBBLE_5)){
            String path = assetsMapper.mapAsset("bubbles/lsq_bubble_5.bt");
            sp.edit().putString(Constants.BUBBLE_5,path).apply();
        }
        if (!sp.contains(Constants.BUBBLE_6)){
            String path = assetsMapper.mapAsset("bubbles/lsq_bubble_6.bt");
            sp.edit().putString(Constants.BUBBLE_6,path).apply();
        }
        if (!sp.contains(Constants.BUBBLE_7)){
            String path = assetsMapper.mapAsset("bubbles/lsq_bubble_7.bt");
            sp.edit().putString(Constants.BUBBLE_7,path).apply();
        }



        this.componentHelper = new TuSdkHelperComponent(activity);

        BubbleTextFragment fragment = new BubbleTextFragment();
        componentHelper.pushModalNavigationActivity(fragment,true);

    }
}
