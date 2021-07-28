package org.lasque.tusdkdemo.views.jigsaw;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tusdk.pulse.DispatchQueue;
import com.tusdk.pulse.filter.filters.JigsawFilter;

import org.lasque.tusdkpulse.core.struct.TuSdkSize;
import org.lasque.tusdkpulse.core.utils.ContextUtils;
import org.lasque.tusdkpulse.core.utils.TLog;
import org.lasque.tusdkpulse.core.view.TuSdkViewHelper;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * TuSDK
 * org.lasque.tusdkdemo.views.jigsaw
 * android-fp-demo
 *
 * @author H.ys
 * @Date 2021/7/12  15:24
 * @Copyright (c) 2020 tusdk.com. All rights reserved.
 */
public class JigsawLayerView extends FrameLayout implements OnJigsawItemViewListener {
    public JigsawLayerView(@NonNull Context context) {
        super(context);
    }

    public JigsawLayerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public JigsawLayerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public JigsawLayerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private LinkedList<JigsawItemView> mJigsawList = new LinkedList<>();

    private HashMap<Integer,JigsawItemView> mJigsawMap = new HashMap<>();

    private Rect mParentRect = new Rect();

    private JigsawItemView mCurrentSelectView;

    private TuSdkSize mModelSize;

    private DispatchQueue mRenderPool;

    private OnJigsawViewListener mDelegate;





    @Override
    public void onRefreshImage() {
        if (mDelegate != null)
            mDelegate.onRefreshImage();
    }

    @Override
    public void onItemSelected(final JigsawItemView view) {
        for (final JigsawItemView jigsawItemView : mJigsawList){
            jigsawItemView.post(new Runnable() {
                @Override
                public void run() {
                    jigsawItemView.setSelected(jigsawItemView.equals(view));
                }
            });

        }

        if (mDelegate != null) mDelegate.onItemViewSelected(view);

    }

    @Override
    public void onItemReleased(JigsawItemView view) {
        if (mDelegate != null) mDelegate.onItemViewReleased(view);
    }

    @Override
    public void onItemClose(JigsawItemView view) {
        removeView(view);

        if (view.equals(mCurrentSelectView)){
            mCurrentSelectView = null;
        }

        if (mDelegate!= null) mDelegate.onItemClosed(view);

    }

    @Override
    public void updateProperty() {
        if (mDelegate != null) mDelegate.onUpdateProperty();
    }

    public void appendJigsawView(JigsawFilter.ImageLayerInfo info){
        JigsawItemView view = buildJigsawItemView(info);
        onItemSelected(view);
    }

    private JigsawItemView buildJigsawItemView(JigsawFilter.ImageLayerInfo info){
        final JigsawItemView view = TuSdkViewHelper.buildView(getContext(),JigsawItemView.LayoutId);
        addView(view);
        view.setRenderPool(mRenderPool);
        view.setListener(this);
        view.setParentFrame(TuSdkViewHelper.locationInWindow(this));
        view.setParentRect(mParentRect);
        view.setImageSize(mModelSize);
        view.setImageLayerInfo(info);
        mJigsawMap.put(info.index,view);
        mJigsawList.add(view);
        return view;
    }

    public void cancelAllItemSelected(){
        for (JigsawItemView view : mJigsawList){
            view.setSelected(false);
        }
        mCurrentSelectView = null;

        mDelegate.onCancelAllView();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN){
            cancelAllItemSelected();
        }
        return super.onTouchEvent(event);
    }

    public void resize(Rect modelSize){
        TLog.e("model size %s",modelSize);

        mParentRect = modelSize;
        TuSdkSize size = new TuSdkSize(modelSize.width(),modelSize.height());
        ViewGroup.LayoutParams layoutParams = getLayoutParams();

        layoutParams.width = size.width;
        layoutParams.height = size.height;

        for (JigsawItemView view : mJigsawList){
            view.setParentFrame(TuSdkViewHelper.locationInWindow(this));
            view.setParentRect(modelSize);
        }

        setLayoutParams(layoutParams);
    }

    public void setRenderPool(DispatchQueue queue){
        mRenderPool = queue;
    }

    public void setDelegate(OnJigsawViewListener listener){
        mDelegate = listener;
    }

    public void setModelSize(TuSdkSize size){
        mModelSize = size;
    }

    public JigsawItemView getTargetView(int index){
        return mJigsawMap.get(index);
    }

    public List<JigsawItemView> getAllViews(){
        return mJigsawList;
    }

    public void reset(){
        removeAllViews();
        mJigsawList.clear();
        mJigsawMap.clear();
    }
}
