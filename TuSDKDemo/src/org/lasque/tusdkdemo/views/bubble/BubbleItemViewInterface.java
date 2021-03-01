package org.lasque.tusdkdemo.views.bubble;

import android.graphics.Rect;

import com.tusdk.pulse.filter.FilterPipe;

import org.lasque.tusdkpulse.core.struct.TuSdkSize;

import java.util.concurrent.ExecutorService;

/**
 * TuSDK
 * org.lasque.tusdkdemo.views.bubble
 * FPDemo
 *
 * @author H.ys
 * @Date 2021/2/1  19:10
 * @Copyright (c) 2020 tusdk.com. All rights reserved.
 */
public interface BubbleItemViewInterface {

    void setThreadPool(ExecutorService threadPool);

    void setListener(OnBubbleLayerItemViewListener listener);

    void setParentRect(Rect rect);

    void setParentFrame(Rect frame);

    void setStroke(int color,int width);

    void setSelected(boolean isSelected);

    void setFilterPipe(FilterPipe fp);

    void updateText(int textIndex,String text);

    void setImageSize(TuSdkSize size);
}
