package org.lasque.tusdkdemo.examples.feature;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.lasque.tusdkdemo.R;
import org.lasque.tusdkpulse.core.TuSdkContext;

import java.util.List;

/**
 * TuSDK
 * org.lasque.tusdkdemo.examples.feature
 * FPDemo
 *
 * @author H.ys
 * @Date 2021/2/2  19:31
 * @Copyright (c) 2020 tusdk.com. All rights reserved.
 */
public class BubbleAdapter extends RecyclerView.Adapter<BubbleAdapter.BubbleViewHolder> {

    private List<BubbleItem> mItemList;

    private Context mContext;

    private OnItemClickListener<BubbleItem,BubbleViewHolder> mItemClickListener;

    public BubbleAdapter(Context context, List<BubbleItem> itemList){
        mItemList = itemList;
        mContext = context;
    }

    @NonNull
    @Override
    public BubbleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BubbleViewHolder(LayoutInflater.from(mContext).inflate(R.layout.bubble_list_item_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final BubbleViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) mItemClickListener.onItemClick(mItemList.get(position),holder,position);
            }
        });
        BubbleItem item = mItemList.get(position);
        holder.mIcon.setImageBitmap(TuSdkContext.getRawBitmap(item.icon));
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    public void setOnItemClickListener(OnItemClickListener<BubbleItem,BubbleViewHolder> itemClickListener){
        mItemClickListener = itemClickListener;
    }

    public static class BubbleViewHolder extends RecyclerView.ViewHolder{
        public ImageView mIcon;

        public BubbleViewHolder(@NonNull View itemView) {
            super(itemView);
            mIcon = itemView.findViewById(R.id.lsq_bubble_icon);
        }
    }
}
