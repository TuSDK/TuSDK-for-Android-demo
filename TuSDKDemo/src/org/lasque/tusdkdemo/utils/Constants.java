/**
 * TuSDKVideoDemo
 * Constants.java
 *
 * @author Bonan
 * @Date: 2017-5-8 上午10:42:48
 * @Copyright: (c) 2017 tusdk.com. All rights reserved.
 */
package org.lasque.tusdkdemo.utils;

import org.lasque.tusdkdemo.R;

public class Constants {
    /**
     * 最大录制时长 (单位：秒)
     */
    public static final int MAX_RECORDING_TIME = 15;

    /**
     * 最小录制时长 (单位：秒)
     */
    public static final int MIN_RECORDING_TIME = 3;

    /** 最大合成数 (单位：个) */
    public static final int MAX_EDITOR_SELECT_MUN = 9;

    public static String TTF_KEY = "TTF-Path";

    public static String BUBBLE_FONTS = "Bubble-Fonts";

    public static String BUBBLE_1 = "Bubble_1";
    public static String BUBBLE_2 = "Bubble_2";
    public static String BUBBLE_3 = "Bubble_3";


    /**
     * 漫画滤镜 filterCode 列表
     */
    public static String[] COMICSFILTERS = {"None","CHComics_Video","USComics_Video","JPComics_Video","Lightcolor_Video","Ink_Video","Monochrome_Video"};
    /**
     * 滤镜 filterCode 列表
     */
    public static String[] VIDEOFILTERS = {"None","SkinLotus_1","SkinNatural_1","SkinFair_1",  "SkinBeckoning_1", "SkinTender_1",
            "SkinLeisurely_1", "SkinRose_1", "SkinWarm_1","SkinClear_1","SkinConfession_1","SkinJapanese_1","SkinExtraordinary_1","SkinHoney_1",
            "SkinButter_1","SkinDawn_1","SkinSummer_1","SkinSweet_1","SkinPlain_1","SkinDusk_1","SkinNostalgia_1","gaosi_01"};

    /** -----------注意事项：视频录制使用人像美颜滤镜(带有磨皮、大眼、瘦脸)，编辑组件尽量不要使用人像美颜滤镜，会造成视频处理过度，效果更不好，建议使用纯色偏滤镜 ----------------*/
}