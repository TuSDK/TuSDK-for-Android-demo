package org.lasque.tusdkdemo.views.jigsaw;

import android.graphics.Rect;

import com.tusdk.pulse.DispatchQueue;
import com.tusdk.pulse.filter.filters.JigsawFilter;

import org.lasque.tusdkpulse.core.struct.TuSdkSize;

/**
 * TuSDK
 * org.lasque.tusdkdemo.views.jigsaw
 * android-fp-demo
 *
 * @author H.ys
 * @Date 2021/7/9  10:39
 * @Copyright (c) 2020 tusdk.com. All rights reserved.
 */
public interface JigsawItemViewInterface {

    void setRenderPool(DispatchQueue queue);

    void setParentRect(Rect rect);

    void setParentFrame(Rect frame);

    void setStroke(int color,int width);

    void setSelected(boolean isSelected);

    void setImageSize(TuSdkSize size);

    void setImageLayerInfo(JigsawFilter.ImageLayerInfo info);

    void setListener(OnJigsawItemViewListener listener);

}
