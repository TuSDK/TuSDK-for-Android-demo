package org.lasque.tusdkdemo.views.jigsaw;

/**
 * TuSDK
 * org.lasque.tusdkdemo.views.jigsaw
 * android-fp-demo
 *
 * @author H.ys
 * @Date 2021/7/9  14:43
 * @Copyright (c) 2020 tusdk.com. All rights reserved.
 */
public interface OnJigsawItemViewListener {

    void onRefreshImage();

    void onItemSelected(JigsawItemView view);

    void onItemReleased(JigsawItemView view);

    void onItemClose(JigsawItemView view);

    void updateProperty();
}
