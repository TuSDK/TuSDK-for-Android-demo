package org.lasque.tusdkdemo.views.bubble;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tusdk.pulse.filter.FilterPipe;

import org.lasque.tusdkpulse.core.struct.TuSdkSize;
import org.lasque.tusdkpulse.core.utils.ContextUtils;
import org.lasque.tusdkpulse.core.view.TuSdkViewHelper;

import java.util.LinkedList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * TuSDK
 * org.lasque.tusdkdemo.views
 * FPDemo
 *
 * @author H.ys
 * @Date 2021/2/1  19:06
 * @Copyright (c) 2020 tusdk.com. All rights reserved.
 */
public class BubbleLayerView extends FrameLayout implements OnBubbleLayerItemViewListener {
    public BubbleLayerView(@NonNull Context context) {
        super(context);
    }

    public BubbleLayerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BubbleLayerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public BubbleLayerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private LinkedList<BubbleItemView> mBubbleList = new LinkedList<>();

    private Rect mParentRect = new Rect();

    private BubbleItemView mCurrentSelectedView;

    private BubbleViewDelegate mDelegate;

    public boolean isSelectedTwice = false;

    private TuSdkSize mImageSize;

    public void setBubbleDelegate(BubbleViewDelegate delegate){
        mDelegate = delegate;
    }



    @Override
    public void onRefreshImage() {
        mDelegate.onRefreshImage();
    }

    @Override
    public void onItemSelected(BubbleItemView view) {

        isSelectedTwice = false;

        if (view.equals(mCurrentSelectedView)){
            isSelectedTwice = true;
        } else {
            mCurrentSelectedView = view;
        }

        for (BubbleItemView bubble : mBubbleList){
            bubble.setSelected(bubble.equals(view));
        }

        if (mDelegate != null) mDelegate.onItemViewSelected(view);

    }

    @Override
    public void onItemReleased(BubbleItemView view) {
        if (!isSelectedTwice) return;

        if (mDelegate != null) mDelegate.onItemViewReleased(view);
    }

    @Override
    public void onItemClose(BubbleItemView view) {
        removeView(view);
        if (view.equals(mCurrentSelectedView)){
            mCurrentSelectedView = null;
        }
        mThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                if (mDelegate!= null) mDelegate.onRefreshImage();
            }
        });

        if (mDelegate != null) mDelegate.onItemClosed(view);
    }

    @Override
    public void onItemUpdateText(BubbleItemView view, int textIndex) {
        if (mDelegate != null) mDelegate.onUpdateText(view, textIndex);
    }

    public void appendBubble(final String path){
        BubbleItemView view = buildBubbleItemView(path);
        onItemSelected(view);
    }

    private BubbleItemView buildBubbleItemView(final String path){
        final BubbleItemView view = TuSdkViewHelper.buildView(getContext(),BubbleItemView.LayoutId);
        addView(view);
        view.setFilterPipe(mFP);
        view.setThreadPool(mThreadPool);
        view.setListener(this);
        view.setParentFrame(TuSdkViewHelper.locationInWindow(this));
        view.setParentRect(mParentRect);
        view.setImageSize(mImageSize);
        view.setStroke(Color.WHITE, ContextUtils.dip2px(getContext(),2));
        view.createBubble(path);
        mBubbleList.add(view);
        return view;
    }

    public void cancelAllItemSelected(){
        for (BubbleItemView view : mBubbleList){
            view.setSelected(false);
        }

        mCurrentSelectedView = null;
        if (mDelegate != null) mDelegate.onItemClosed(null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN){
            cancelAllItemSelected();
        }
        return super.onTouchEvent(event);
    }

    public void resize(Rect videoSize){
        mParentRect = videoSize;
        TuSdkSize size = new TuSdkSize(videoSize.width(),videoSize.height());
        ViewGroup.LayoutParams layoutParams = getLayoutParams();

        layoutParams.width = size.width;
        layoutParams.height = size.height;

        for (BubbleItemView view : mBubbleList){
            view.setParentFrame(TuSdkViewHelper.locationInWindow(this));
            view.setParentRect(videoSize);
        }
    }

    public void setImageSize(TuSdkSize size){
        mImageSize = size;
    }

    public void removeAllBubbles(){
        for (BubbleItemView view : mBubbleList){
            removeView(view);
            final BubbleItemView targetView = view;
            Future<Boolean> res = mThreadPool.submit(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return mFP.deleteFilter(targetView.getCurrentFilterIndex());
                }
            });

            try {
                res.get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        mBubbleList.clear();
        if (mDelegate != null) mDelegate.onRefreshImage();
        mCurrentSelectedView = null;
    }

    /** ------------------------------------------- FilterPipe ------------------------------------------------------ */

    private FilterPipe mFP;

    private ExecutorService mThreadPool;

    public void setFilterPipe(FilterPipe fp){
        mFP = fp;
    }

    public void setThreadPool(ExecutorService threadPool){
        mThreadPool = threadPool;
    }
}
