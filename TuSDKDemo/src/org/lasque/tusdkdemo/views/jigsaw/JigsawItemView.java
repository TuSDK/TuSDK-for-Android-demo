package org.lasque.tusdkdemo.views.jigsaw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.tusdk.pulse.DispatchQueue;
import com.tusdk.pulse.filter.filters.JigsawFilter;

import org.lasque.tusdkdemo.R;
import org.lasque.tusdkpulse.core.struct.TuSdkSize;
import org.lasque.tusdkpulse.core.utils.ContextUtils;
import org.lasque.tusdkpulse.core.utils.RectHelper;
import org.lasque.tusdkpulse.core.utils.TLog;
import org.lasque.tusdkpulse.core.utils.TuSdkGestureRecognizer;
import org.lasque.tusdkpulse.core.utils.image.BitmapHelper;
import org.lasque.tusdkpulse.core.view.TuSdkImageView;
import org.lasque.tusdkpulse.core.view.TuSdkViewHelper;

import java.io.File;

/**
 * TuSDK
 * org.lasque.tusdkdemo.views.jigsaw
 * android-fp-demo
 *
 * @author H.ys
 * @Date 2021/7/8  17:35
 * @Copyright (c) 2020 tusdk.com. All rights reserved.
 */
public class JigsawItemView extends ConstraintLayout implements JigsawItemViewInterface{
    
    public static int LayoutId = R.layout.jigsaw_item_view_layout;

    private int mStrokeWidth;

    private int mStrokeColor;

    private boolean isSetStroke;

    private TuSdkImageView mImageView;

    private Rect mParentRect = new Rect();

    protected Rect mParentFrame = new Rect();

    protected DispatchQueue mRenderPool;

    protected JigsawFilter.ImageLayerInfo mCurrentInfo;

    private TuSdkSize mImageSize;

    protected boolean isLayouted = false;

    protected OnJigsawItemViewListener mListener;

    private boolean isMoveEvent = false;

    protected PointF mLastPoint = new PointF();

    protected float mCHypotenuse;

    protected float mScale = 1f;

    protected float mMaxScale = 1f;

    protected float mMinScale = 1f;

    protected float mDegree = 0.0f;

    protected TuSdkSize mDefaultViewSize = new TuSdkSize();

    protected Rect mLayerRect = new Rect();


    protected TuSdkGestureRecognizer mConfigTouchListener = new TuSdkGestureRecognizer() {
        @Override
        public void onTouchBegin(TuSdkGestureRecognizer gesture, View view, MotionEvent event) {
            handleTransActionStart(event);
        }

        @Override
        public void onTouchSingleMove(TuSdkGestureRecognizer gesture, View view, MotionEvent event, StepData data) {
            handleTransActionMove(gesture, event);
        }

        @Override
        public void onTouchMultipleMoveForStablization(TuSdkGestureRecognizer gesture, StepData data) {
            TLog.e("onTouchMultipleMoveForStablization");
            handleDoubleActionMove(gesture,data);
        }

        @Override
        public void onTouchMultipleBegin(TuSdkGestureRecognizer gesture, View view, MotionEvent event) {
            super.onTouchMultipleBegin(gesture, view, event);
            TLog.e("onTouchMultipleBegin");
        }

        @Override
        public void onTouchMultipleMove(TuSdkGestureRecognizer gesture, View view, MotionEvent event, StepData data) {
            super.onTouchMultipleMove(gesture, view, event, data);
            TLog.e("onTouchMultipleMove");
        }

        @Override
        public void onTouchEnd(TuSdkGestureRecognizer gesture, View view, MotionEvent event, StepData data) {
            handleTransActionEnd(event);
        }
    };

    protected void initView(){
        ViewTreeObserver vto = getViewTreeObserver();
        OnGlobalLayoutListener globalLayoutListener = new OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (!isLayouted){
                    isLayouted = true;
                    mConfigTouchListener.setMultipleStablization(true);
                }
            }
        };
        vto.addOnGlobalLayoutListener(globalLayoutListener);

    }

    private TuSdkImageView getImageView(){
        if (mImageView == null){
            mImageView = (TuSdkImageView) getViewById(R.id.lsq_jigsaw_imageView);
        }
        return mImageView;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mConfigTouchListener.onTouch(this,event);
    }

    protected void handleTransActionStart(MotionEvent event){
        isMoveEvent = false;
        if (mListener != null) mListener.onItemSelected(this);
    }

    protected void handleTransActionEnd(MotionEvent event){
        if (!isMoveEvent){
            if (mListener != null) mListener.onItemReleased(this);
        }
    }

    protected void handleDoubleActionMove(TuSdkGestureRecognizer gesture,TuSdkGestureRecognizer.StepData stepData){

        mDegree = (360 + mDegree + stepData.stepDegree) % 360;

        Rect rect = new Rect();
        getGlobalVisibleRect(rect);

        PointF caPoint = new PointF(rect.centerX(),rect.centerY());

        computerScale(stepData.stepSpace,caPoint);

        TuSdkSize mScaleSize = mDefaultViewSize.scale(mScale);

        mLayerRect.set(0,0,mScaleSize.width,mScaleSize.height);

        mRenderPool.runSync(new Runnable() {
            @Override
            public void run() {
//                updateRotate();
                updateScale();
                updateProperty();
                mListener.onRefreshImage();
            }
        });




    }

    protected void handleTransActionMove(TuSdkGestureRecognizer gesture,MotionEvent event){
        if (!(Math.abs(gesture.getStepPoint().x) < 2f && Math.abs(gesture.getStepPoint().y) < 2f)){
            isMoveEvent = true;
        }

        final float stepWidthPercent = gesture.getStepPoint().x / mParentRect.width();
        final float stepHeightPercent = gesture.getStepPoint().y / mParentRect.height();

//        if (mSrcRectF.width() >= 1.0 || mSrcRectF.height() >= 1.0) return;

//        if (currentSrc.left <= 0.0 || currentSrc.right >= 1.0 || currentSrc.top <= 0.0 || currentSrc.bottom >= 1.0) return;



//        mSrcRectF.left = (float) Math.max(0.0,mSrcRectF.left);
//        mSrcRectF.right = (float) Math.min(1.0,mSrcRectF.right);
//        mSrcRectF.top = (float) Math.max(0.0,mSrcRectF.top);
//        mSrcRectF.bottom = (float) Math.min(1.0,mSrcRectF.bottom);

        mRenderPool.runSync(new Runnable() {
            @Override
            public void run() {
                mCurrentInfo.offSet.offset(stepWidthPercent,stepHeightPercent);
                updateScale();
                updateProperty();
                mListener.onRefreshImage();
            }
        });


    }

    protected void handleTureAndScaleActionStart(float xCoordinate, float yCoordinate){

    }

    protected void handleTurnAndScaleActionMove(float xCoordinate, float yCoordinate){

    }

    protected void computerScale(PointF point, PointF cPoint) {
        float sDistance = RectHelper.getDistanceOfTwoPoints(cPoint, mLastPoint);

        float cDistance = RectHelper.getDistanceOfTwoPoints(cPoint, point);

        computerScale(cDistance - sDistance, cPoint);


    }

    protected void computerScale(float distance, PointF cPoint) {
        if (distance == 0f) return;

        float offsetScale = distance / mCHypotenuse * 2;

        mScale += offsetScale;

        if (mScale < mMinScale) mScale = mMinScale;

        TuSdkSize size = mDefaultViewSize;

        TuSdkSize scaleSize = mDefaultViewSize.scale(mScale);
    }

    protected PointF getCenterOpposite(Rect trans) {
        Point globalOffset = new Point();
        getGlobalVisibleRect(trans, globalOffset);
        PointF centerPoint = new PointF(trans.centerX(), trans.centerY());
        centerPoint.set(trans.centerX(), trans.centerY());
        return centerPoint;
    }

    protected PointF getCenterOpposite(PointF trans){
        PointF oPoint = new PointF();
        oPoint.x = trans.x + getWidth() * 0.5f;
        oPoint.y = trans.y + getHeight() * 0.5f;

        return oPoint;
    }

    protected void computerAngle(PointF point, PointF cPoint) {
        float sAngle = getCenterOppositeAngle(mLastPoint, cPoint);

        float eAngle = getCenterOppositeAngle(point, cPoint);

        mDegree = (360 + mDegree + (eAngle - sAngle)) % 360;

        setRotation(mDegree);
    }

    protected float getCenterOppositeAngle(PointF point, PointF cPoint) {
        return RectHelper.computeAngle(point, cPoint);
    }

    /**
     * 绘制边框
     *
     * @param canvas
     */
    protected void drawStroke(Canvas canvas) {
        if (!this.isSetStroke) return;

        // 边框绘制采用的是居中方式，所以需要计算偏移位置
        float strokeOffset = this.mStrokeWidth * 0.5f;

        RectF rectF = new RectF(strokeOffset, strokeOffset, getWidth()
                - strokeOffset, getHeight() - strokeOffset);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(this.mStrokeColor);
        paint.setStrokeWidth(this.mStrokeWidth);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRoundRect(rectF, 0, 0, paint);

        if (this.mStrokeWidth == 0) {
            this.isSetStroke = false;
        }
    }





    public JigsawItemView(Context context) {
        this(context,null);
    }

    public JigsawItemView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public JigsawItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @Override
    public void setRenderPool(DispatchQueue queue) {
        mRenderPool = queue;
    }

    @Override
    public void setParentRect(Rect rect) {
        mParentRect = rect;
    }

    @Override
    public void setParentFrame(Rect frame) {
        mParentFrame = frame;
    }

    @Override
    public void setStroke(int color, int width) {
        mStrokeColor = color;
        mStrokeWidth = width;
        getImageView().setStroke(mStrokeColor,mStrokeWidth);
    }

    @Override
    public void setImageSize(TuSdkSize size) {
        mImageSize = size;
    }

    @Override
    public void setImageLayerInfo(JigsawFilter.ImageLayerInfo info) {
        TuSdkSize size = BitmapHelper.getBitmapSize(new File(info.path));
        mLayerRect.set(0,0,size.width,size.height);
        mDefaultViewSize = size;


        float x = mParentRect.width() * info.dsc_rect.left;
        float y = mParentRect.height() * info.dsc_rect.top;

        int width = (int) (mParentRect.width() * info.dsc_rect.width());
        int height = (int) (mParentRect.height() * info.dsc_rect.height());

        mCHypotenuse = RectHelper.getDistanceOfTwoPoints(0,0,width,height);


        setX(x);
        setY(y);
        setViewSize(this,width,height);

        mCurrentInfo = info;
    }

    public void updateImageLayerInfo(JigsawFilter.ImageLayerInfo info){
        float x = mParentRect.width() * info.dsc_rect.left;
        float y = mParentRect.height() * info.dsc_rect.top;

        int width = (int) (mParentRect.width() * info.dsc_rect.width());
        int height = (int) (mParentRect.height() * info.dsc_rect.height());

        mCHypotenuse = RectHelper.getDistanceOfTwoPoints(0,0,width,height);


        setX(x);
        setY(y);
        setViewSize(this,width,height);

        mCurrentInfo = info;
    }


    @Override
    public void setListener(OnJigsawItemViewListener listener) {
        mListener = listener;
    }

    @Override
    public void setSelected(boolean selected) {
        if (!isLayouted) return;
        int color = selected ? Color.RED : 0;
        mStrokeWidth = ContextUtils.dip2px(getContext(),2);
        getImageView().setStroke(color,mStrokeWidth);
    }

    /** 显示或隐藏视图 占位 */
    public void showViewIn(View view, boolean isShow)
    {
        TuSdkViewHelper.showViewIn(view, isShow);
    }

    private void updateRotate(){
        mCurrentInfo.rotation = (int) mDegree;
    }

    private void updateScale(){

        mCurrentInfo.scale = mScale;
    }

    private void updateProperty(){
        mListener.updateProperty();
    }

    private void setViewSize(View view, int width, int height) {
        TuSdkViewHelper.setViewWidth(view,width);
        TuSdkViewHelper.setViewHeight(view,height);
    }

    public JigsawFilter.ImageLayerInfo getCurrentInfo() {
        return mCurrentInfo;
    }
}
