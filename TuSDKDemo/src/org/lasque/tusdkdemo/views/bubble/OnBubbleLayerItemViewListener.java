package org.lasque.tusdkdemo.views.bubble;

/**
 * TuSDK
 * org.lasque.tusdkdemo.views
 * FPDemo
 *
 * @author H.ys
 * @Date 2021/2/1  19:07
 * @Copyright (c) 2020 tusdk.com. All rights reserved.
 */
public interface OnBubbleLayerItemViewListener {

    void onRefreshImage();

    void onItemSelected(BubbleItemView view);

    void onItemReleased(BubbleItemView view);

    void onItemClose(BubbleItemView view);

    void onItemUpdateText(BubbleItemView view,int textIndex);
}