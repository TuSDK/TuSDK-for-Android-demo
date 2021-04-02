package org.lasque.tusdkdemo.views.bubble;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.tusdk.pulse.Config;
import com.tusdk.pulse.filter.Filter;
import com.tusdk.pulse.filter.FilterPipe;
import com.tusdk.pulse.filter.filters.BubbleTextFilter;

import org.lasque.tusdkdemo.R;
import org.lasque.tusdkdemo.utils.Constants;
import org.lasque.tusdkpulse.core.TuSdkContext;
import org.lasque.tusdkpulse.core.struct.TuSdkSize;
import org.lasque.tusdkpulse.core.struct.ViewSize;
import org.lasque.tusdkpulse.core.utils.ContextUtils;
import org.lasque.tusdkpulse.core.utils.RectHelper;
import org.lasque.tusdkpulse.core.utils.TLog;
import org.lasque.tusdkpulse.core.utils.ThreadHelper;
import org.lasque.tusdkpulse.core.utils.TuSdkGestureRecognizer;
import org.lasque.tusdkpulse.core.view.TuSdkImageView;
import org.lasque.tusdkpulse.core.view.TuSdkViewHelper;
import org.lasque.tusdkpulse.core.view.widget.button.TuSdkImageButton;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * TuSDK
 * org.lasque.tusdkdemo.views.bubble
 * FPDemo
 *
 * @author H.ys
 * @Date 2021/2/1  19:09
 * @Copyright (c) 2020 tusdk.com. All rights reserved.
 */
public class BubbleItemView extends ConstraintLayout implements BubbleItemViewInterface {

    public static int LayoutId = R.layout.bubble_item_view_layout;

    private static int Message_Update_Text = 100;

    private static int Message_Double_Click = 200;

    public static int Bubble_Index = 50;

    /**
     * 边框宽度
     */
    private int mStrokeWidth;

    /**
     * 边框颜色
     */
    private int mStrokeColor;

    /**
     * 是否设置边框
     */
    private boolean isSetStroke;

    private TuSdkImageButton mCancelButton;

    private TuSdkImageButton mTurnButton;

    private TuSdkImageView mImageView;

    protected PointF mLastPoint = new PointF();

    protected PointF mLastCenterPoint = new PointF();

    protected PointF mTranslation = new PointF();

    protected Rect mParentRect = new Rect();

    protected float mCHypotenuse;

    protected float mScale = 1f;

    protected float mDegree = 0.0f;

    protected TuSdkSize mDefaultViewSize = new TuSdkSize();

    protected Rect mParentFrame = new Rect();

    protected ExecutorService mThreadPool;

    private BubbleTextFilter.PropertyBuilder mBubbleProperty = new BubbleTextFilter.PropertyBuilder();

    private Filter mCurrentFilter;

    private int mCurrentFilterIndex;

    private TuSdkSize mImageSize;

    private OnBubbleLayerItemViewListener mListener;

    private FilterPipe mFP;

    private boolean isMoveEvent = false;

    protected boolean isLayouted = false;

    private ViewTreeObserver.OnPreDrawListener mOnDrawListener;

    private Handler mHandler;

    private HandlerThread mHandlerThread;

    private ArrayList<RectF> mTextsPos;

    private boolean willShowKeyBoard = false;

    private ArrayList<View> mTextRectView = new ArrayList<>();

    public int getCurrentFilterIndex() {
        return mCurrentFilterIndex;
    }

    private OnClickListener mButtonClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == getCancelButton().getId()){
                handleCancelButton();
            }

        }
    };

    protected OnTouchListener mOnTouchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getPointerCount() > 1) return false;
            Rect parentFrame = TuSdkViewHelper.locationInWindow((View) getParent());
            int yOffset = 0;

            if (mParentFrame.top - parentFrame.top > 0){
                yOffset = mParentFrame.top - parentFrame.top;
            }

            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    handleTurnAndScaleActionStart(event.getRawX(),event.getRawY() - yOffset);
                    break;
                case MotionEvent.ACTION_MOVE :
                    handleTurnAndScaleActionMove(event.getRawX(),event.getRawY() - yOffset);
                    break;
            }
            return true;
        }
    };

    private PointF mStartPointPercent;
    private PointF mStartPoint;

    protected TuSdkGestureRecognizer mConfigTouchListener = new TuSdkGestureRecognizer() {
        @Override
        public void onTouchBegin(TuSdkGestureRecognizer gesture, View view, MotionEvent event) {
            if (event.getPointerCount() == 1){
                float startX = event.getX();
                float startY = event.getY();
                mStartPoint = new PointF(startX,startY);
                mStartPointPercent = new PointF(event.getX() / view.getWidth(),event.getY() / view.getHeight());
                int index = isHasText(mStartPointPercent);
                if (index > -1){
                    if (willShowKeyBoard){
                        Message msg = mHandler.obtainMessage();
                        msg.arg1 = index;
                        msg.what = Message_Update_Text;
                        mHandler.sendMessage(msg);
                    } else {
                        willShowKeyBoard = true;
                        Message msg = mHandler.obtainMessage();
                        msg.arg1 = index;
                        msg.what = Message_Double_Click;
                        mHandler.sendMessageDelayed(msg,200);
                        TLog.e("Message_Update_Text send message");
                    }


                }
            }
            handleTransActionStart(event);
        }

        @Override
        public void onTouchSingleMove(TuSdkGestureRecognizer gesture, View view, MotionEvent event, StepData data) {
            handleTransActionMove(gesture, event);
        }

        @Override
        public void onTouchMultipleMoveForStablization(TuSdkGestureRecognizer gesture, StepData data) {
            handleDoubleActionMove(gesture,data);
        }

        @Override
        public void onTouchEnd(TuSdkGestureRecognizer gesture, View view, MotionEvent event, StepData data) {
            handleTransActionEnd(event);
        }
    };


    public BubbleItemView(Context context) {
        this(context,null);
    }

    public BubbleItemView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public BubbleItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
        mHandlerThread = new HandlerThread(this.toString() + "LongClick");
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == Message_Update_Text){
                    TLog.e("Message_Update_Text");
                    mListener.onItemUpdateText(BubbleItemView.this,msg.arg1);
                    return true;
                }
                if (msg.what == Message_Double_Click){
                    willShowKeyBoard = false;
                    return true;
                }
                return false;
            }
        });

    }

    protected void initView(){
        ViewTreeObserver vto = getViewTreeObserver();
        ViewTreeObserver.OnGlobalLayoutListener globalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (!isLayouted){
                    isLayouted = true;

                    mConfigTouchListener.setMultipleStablization(true);
                }
            }
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
            mOnDrawListener = new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    mLastCenterPoint = new PointF(getX() + (getWidth() / 2f),getY() + (getHeight() / 2f));
                    return true;
                }
            };
            vto.addOnPreDrawListener(mOnDrawListener);
        }
        vto.addOnGlobalLayoutListener(globalLayoutListener);

        getCancelButton();
        getTurnButton();
    }

    public void createBubble(final String path){
        SharedPreferences sp = TuSdkContext.context().getSharedPreferences("TU-TTF", Context.MODE_PRIVATE);
        final String fontDir = sp.getString(Constants.BUBBLE_FONTS,"");
        Future<Boolean> res = mThreadPool.submit(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                Filter bubbleText = new Filter(mFP.getContext(),BubbleTextFilter.TYPE_NAME);
                Config config = new Config();
                config.setString(BubbleTextFilter.CONFIG_MODEL,path);
                config.setString(BubbleTextFilter.CONFIG_FONT_DIR,fontDir);
                bubbleText.setConfig(config);
                mCurrentFilterIndex = Bubble_Index++;
                boolean ret = mFP.addFilter(mCurrentFilterIndex,bubbleText);
                mCurrentFilter = bubbleText;
                mListener.onRefreshImage();
                mBubbleProperty.holder.scale = 0.7;
                bubbleText.setProperty(BubbleTextFilter.PROP_PARAM,mBubbleProperty.makeProperty());
                mListener.onRefreshImage();
                final BubbleTextFilter.InteractionInfo interactionInfo = new BubbleTextFilter.InteractionInfo
                        (mCurrentFilter.getProperty(BubbleTextFilter.PROP_INTERACTION_INFO));
                final float hp = mImageSize.width / ((float) mParentRect.width());
                mTextsPos = interactionInfo.rects;
                if (mBubbleProperty.holder.texts.isEmpty()){
                    for (int i = 0;i<mTextsPos.size();i++){
                        mBubbleProperty.holder.texts.add("");
                    }
                }
                final BubbleItemView targetView = BubbleItemView.this;
                ThreadHelper.post(new Runnable() {
                    @Override
                    public void run() {
                        int oWidth = (int) (interactionInfo.width / hp);
                        int oHeight = (int) (interactionInfo.height / hp);
                        int width = (int) (interactionInfo.width / hp) + ContextUtils.dip2px(targetView.getContext(),26);
                        int height = (int) (interactionInfo.height / hp) + ContextUtils.dip2px(targetView.getContext(),26);

                        mDefaultViewSize = TuSdkSize.create(width,height);
                        mCHypotenuse = RectHelper.getDistanceOfTwoPoints(0F,0f,interactionInfo.width,interactionInfo.height);
                        mScale = 0.7f;
                        double px = interactionInfo.posX * mParentRect.width();
                        double py = interactionInfo.posY * mParentRect.height();

                        float x = (float) (px - (width / 2f));
                        float y = (float) (py - (height / 2f));
                        setX(x);
                        setY(y);
                        setViewSize(targetView,width,height);
                        setSelected(true);
                        mTranslation = new PointF(getX(),getY());
                        mLastCenterPoint = new PointF(((float) px), ((float) py));

                        for (int i =0;i<mTextsPos.size();i++){
                            RectF vRect = mTextsPos.get(i);
                            int vWidth = (int) (vRect.width() * oWidth);
                            int vHeight = (int) (vRect.height() * oHeight);
                            View v = new View(getContext());
                            targetView.addView(v,i);
                            v.setBackground(getContext().getDrawable(R.drawable.bubble_dash_line));
                            ConstraintLayout.LayoutParams layoutParams = (LayoutParams) v.getLayoutParams();
                            layoutParams.width = vWidth;
                            layoutParams.height = vHeight;
                            layoutParams.leftToLeft = LayoutParams.PARENT_ID;
                            layoutParams.topToTop = LayoutParams.PARENT_ID;
                            int marginLeft =  (int) (vRect.left * oWidth) + ContextUtils.dip2px(targetView.getContext(),13);
                            int marginTop = (int) (vRect.top * oHeight) + ContextUtils.dip2px(targetView.getContext(),13);
                            layoutParams.topMargin = marginTop;
                            layoutParams.goneTopMargin = marginTop;
                            layoutParams.leftMargin = marginLeft;
                            layoutParams.goneLeftMargin = marginLeft;
                            v.setLayoutParams(layoutParams);

                            mTextRectView.add(v);

                        }
                    }
                });
                return ret;
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

    private TuSdkImageButton getCancelButton() {
        if (mCancelButton == null){
            mCancelButton = (TuSdkImageButton) getViewById(R.id.lsq_bubble_cancelButton);
            if (mCancelButton != null){
                mCancelButton.setOnClickListener(mButtonClickListener);
            }
        }
        return mCancelButton;
    }

    private TuSdkImageView getImageView(){
        if (mImageView == null){
            mImageView = (TuSdkImageView) getViewById(R.id.lsq_bubble_imageView);
        }
        return mImageView;
    }

    private TuSdkImageButton getTurnButton() {
        if (mTurnButton == null){
            mTurnButton = (TuSdkImageButton) getViewById(R.id.lsq_bubble_turnButton);
            if (mTurnButton != null){
                mTurnButton.setOnTouchListener(mOnTouchListener);
            }
        }
        return mTurnButton;
    }

    private void handleCancelButton(){
        mListener.onItemClose(this);
        Future<Boolean> res = mThreadPool.submit(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                boolean ret = mFP.deleteFilter(mCurrentFilterIndex);
                mListener.onRefreshImage();
                return ret;
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

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
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

    protected void handleDoubleActionMove(TuSdkGestureRecognizer gesture, TuSdkGestureRecognizer.StepData data){
        mDegree = (360 + mDegree + data.stepDegree) % 360;

        setRotation(mDegree);

        Rect rect = new Rect();

        getGlobalVisibleRect(rect);

        PointF caPoint = new PointF(rect.centerX(),rect.centerY());

        computerScale(data.stepSpace,caPoint);

        requestLayout();

        Future<Boolean> res = mThreadPool.submit(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                updateRotate();
                updateZoom();
                updateProperty();
                updateViewSize();
                return true;
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

    protected void handleTransActionMove(TuSdkGestureRecognizer gesture,MotionEvent event){
        if (!(Math.abs(gesture.getStepPoint().x) < 2f && Math.abs(gesture.getStepPoint().y) < 2f)) isMoveEvent = true;

        mTranslation.offset(gesture.getStepPoint().x,gesture.getStepPoint().y);

        Rect trans = new Rect();

        boolean isLandScape = mParentFrame.width() > mParentFrame.height();

        PointF cPoint = isLandScape ? getCenterOpposite(mTranslation) : getCenterOpposite(trans);

        ViewSize size = ViewSize.create(this);

        RectF outRect = RectHelper.minEnclosingRectangle(cPoint,size,mDegree);

        cPoint.offset(gesture.getStepPoint().x,gesture.getStepPoint().y);

        fixedMovePoint(isLandScape ? mTranslation : cPoint,outRect);

        setX(mTranslation.x);
        setY(mTranslation.y);

        requestLayout();

        mLastCenterPoint.set(getX() + getWidth() / 2f,getY() + getHeight() / 2f);
        final PointF cPointInLayer = new PointF();

        cPointInLayer.set(mLastCenterPoint.x / mParentRect.width(),mLastCenterPoint.y / mParentRect.height());

        Future<Boolean> res = mThreadPool.submit(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                updatePan(cPointInLayer);
                updateProperty();
                return true;
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

    protected void fixedMovePoint(PointF trans,RectF outRect){
        if (mParentFrame == null || trans == null || outRect == null) return;

        RectF edge = new RectF(-outRect.width() * 0.5f, -outRect.height() * 0.5f, mParentFrame.width() + outRect.width() * 0.5f, mParentFrame.height()
                + outRect.height() * 0.5f);

        if (outRect.left < edge.left) {
            trans.x = edge.left + (outRect.width() - this.getWidth()) * 0.5f;
        }
        if (outRect.right > edge.right) {
            trans.x = edge.right - (outRect.width() + this.getWidth()) * 0.5f;
        }
        if (outRect.top < edge.top) {
            trans.y = edge.top + (outRect.height() - this.getHeight()) * 0.5f;
        }
        if (outRect.bottom > edge.bottom) {
            trans.y = edge.bottom - (outRect.height() + this.getHeight()) * 0.5f;
        }
    }

    protected void handleTurnAndScaleActionStart(float xCoordinate, float yCoordinate){
        mLastPoint.set(xCoordinate, yCoordinate);

        mListener.onItemSelected(this);
    }

    protected void handleTurnAndScaleActionMove(float xCoordinate, float yCoordinate) {
        mLastCenterPoint = new PointF(getX() + (getWidth() / 2), getY() + (getHeight() / 2));

        PointF point = new PointF(xCoordinate,yCoordinate);

        Rect trans = new Rect();

        PointF cPoint = getCenterOpposite(trans);

        computerAngle(point,cPoint);

        computerScale(point,cPoint);

        requestLayout();

        mLastPoint.set(point.x,point.y);
        Future<Boolean> res = mThreadPool.submit(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                updateRotate();
                updateZoom();
                updateProperty();
                updateViewSize();
                return true;
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

    private void updateViewSize() {
        try {
            final BubbleTextFilter.InteractionInfo interactionInfo = new BubbleTextFilter.InteractionInfo
                    (mCurrentFilter.getProperty(BubbleTextFilter.PROP_INTERACTION_INFO));
            final float hp = mImageSize.width / ((float) mParentRect.width());
            mTextsPos = interactionInfo.rects;
            final View targetView = this;
            ThreadHelper.post(new Runnable() {
                @Override
                public void run() {
                    int oWidth = (int) (interactionInfo.width / hp);
                    int oHeight = (int) (interactionInfo.height / hp);
                    int width = (int) (interactionInfo.width / hp) + ContextUtils.dip2px(targetView.getContext(),26);
                    int height = (int) (interactionInfo.height / hp) + ContextUtils.dip2px(targetView.getContext(),26);

                    mDefaultViewSize = TuSdkSize.create(width,height);

                    double px = interactionInfo.posX * mParentRect.width();
                    double py = interactionInfo.posY * mParentRect.height();

                    float x = (float) (px - (width / 2f));
                    float y = (float) (py - (height / 2f));
                    setX(x);
                    setY(y);

                    mTranslation.set(x,y);
                    mLastCenterPoint = new PointF(((float) px), ((float) py));
                    setViewSize(targetView,width,height);

                    for (int i =0;i<mTextsPos.size();i++){
                        RectF vRect = mTextsPos.get(i);
                        int vWidth = (int) (vRect.width() * oWidth);
                        int vHeight = (int) (vRect.height() * oHeight);
                        View v = mTextRectView.get(i);
                        v.setBackground(getContext().getDrawable(R.drawable.bubble_dash_line));
                        ConstraintLayout.LayoutParams layoutParams = (LayoutParams) v.getLayoutParams();
                        layoutParams.width = vWidth;
                        layoutParams.height = vHeight;
                        layoutParams.leftToLeft = LayoutParams.PARENT_ID;
                        layoutParams.topToTop = LayoutParams.PARENT_ID;
                        int marginLeft =  (int) (vRect.left * oWidth) + ContextUtils.dip2px(targetView.getContext(),13);
                        int marginTop = (int) (vRect.top * oHeight) + ContextUtils.dip2px(targetView.getContext(),13);
                        layoutParams.topMargin = marginTop;
                        layoutParams.goneTopMargin = marginTop;
                        layoutParams.leftMargin = marginLeft;
                        layoutParams.goneLeftMargin = marginLeft;
                        v.setLayoutParams(layoutParams);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setViewSize(View view, int width, int height) {
        TuSdkViewHelper.setViewWidth(view,width);
        TuSdkViewHelper.setViewHeight(view,height);
    }

    private void updateProperty() {
        mCurrentFilter.setProperty(BubbleTextFilter.PROP_PARAM,mBubbleProperty.makeProperty());
        mListener.onRefreshImage();
    }

    private void updateZoom() {
        mBubbleProperty.holder.scale = mScale;
    }

    private void updateRotate() {
        mBubbleProperty.holder.rotate = mDegree;
    }

    private void updatePan(PointF point){
        mBubbleProperty.holder.posX = point.x;
        mBubbleProperty.holder.posY = point.y;
    }



    protected void computerScale(PointF point, PointF cPoint) {
        float sDistance = RectHelper.getDistanceOfTwoPoints(cPoint, mLastPoint);

        float cDistance = RectHelper.getDistanceOfTwoPoints(cPoint, point);

        computerScale(cDistance - sDistance, cPoint);
    }

    protected void computerScale(float distance, PointF cPoint) {
        if (distance == 0f) return;

        float offsetScale = distance / mCHypotenuse * 2;
        TLog.e("offsetScale %s  distance %s mCHypotenuse %s",offsetScale,distance,mCHypotenuse);

        mScale += offsetScale;

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

    @Override
    public void setThreadPool(ExecutorService threadPool) {
        mThreadPool = threadPool;
    }

    @Override
    public void setListener(OnBubbleLayerItemViewListener listener) {
        this.mListener = listener;
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
        isSetStroke = true;
        invalidate();
    }

    @Override
    public void setFilterPipe(FilterPipe fp) {
        mFP = fp;
    }

    @Override
    public void updateText(final int textIndex, final String text){
        Future<Boolean> res = mThreadPool.submit(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                mBubbleProperty.holder.texts.set(textIndex,text);
                updateProperty();
                updateViewSize();
                return true;
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

    @Override
    public void setImageSize(TuSdkSize size) {
        mImageSize = size;
    }

    @Override
    public void setSelected(boolean selected) {
        if (!isLayouted) return;
        int color = selected ? Color.WHITE : 0;
        getImageView().setStroke(color,mStrokeWidth);
        showViewIn(getCancelButton(),selected);
        showViewIn(getTurnButton(),selected);
        for (View v : mTextRectView){
            showViewIn(v,selected);
        }
    }

    /** 显示或隐藏视图 占位 */
    public void showViewIn(View view, boolean isShow)
    {
        TuSdkViewHelper.showViewIn(view, isShow);
    }

    private int isHasText(PointF p){
        if (mTextsPos == null || mTextsPos.isEmpty()) return -1;
        boolean res = false;
        for (int i =0;i<mTextsPos.size() ;i++){
            RectF rf = mTextsPos.get(i);
            res = rf.contains(p.x,p.y);
            if (res) return i;
        }
        return -1;
    }

    public String getTextByIndex(int index){
        return mBubbleProperty.holder.texts.get(index);
    }
}
