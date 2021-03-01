package org.lasque.tusdkdemo.views.bubble;

/**
 * TuSDK
 * org.lasque.tusdkdemo.views.bubble
 * FPDemo
 *
 * @author H.ys
 * @Date 2021/2/2  15:58
 * @Copyright (c) 2020 tusdk.com. All rights reserved.
 */
public interface BubbleViewDelegate {

    void onItemViewSelected(BubbleItemView view);

    void onItemViewReleased(BubbleItemView view);

    void onItemClosed(BubbleItemView view);

    void onRefreshImage();

    void onUpdateText(BubbleItemView view,int textIndex);
}
