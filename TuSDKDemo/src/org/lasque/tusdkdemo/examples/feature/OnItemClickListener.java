package org.lasque.tusdkdemo.examples.feature;

import androidx.recyclerview.widget.RecyclerView;

/**
 * TuSDK
 * org.lasque.tusdkdemo.examples.feature
 * FPDemo
 *
 * @author H.ys
 * @Date 2021/2/2  19:43
 * @Copyright (c) 2020 tusdk.com. All rights reserved.
 */
public interface OnItemClickListener<I,H extends RecyclerView.ViewHolder> {

    void onItemClick(I item,H holder,int pos);
}
