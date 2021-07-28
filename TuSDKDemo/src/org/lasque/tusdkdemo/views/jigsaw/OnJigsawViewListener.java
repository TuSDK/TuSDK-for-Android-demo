package org.lasque.tusdkdemo.views.jigsaw;

/**
 * TuSDK
 * org.lasque.tusdkdemo.views.jigsaw
 * android-fp-demo
 *
 * @author H.ys
 * @Date 2021/7/12  16:19
 * @Copyright (c) 2020 tusdk.com. All rights reserved.
 */
public interface OnJigsawViewListener {

    void onItemViewSelected(JigsawItemView view);

    void onItemViewReleased(JigsawItemView view);

    void onItemClosed(JigsawItemView view);

    void onRefreshImage();

    void onUpdateProperty();

    void onCancelAllView();

}
