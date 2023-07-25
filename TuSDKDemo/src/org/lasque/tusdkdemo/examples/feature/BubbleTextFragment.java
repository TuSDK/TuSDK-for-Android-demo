package org.lasque.tusdkdemo.examples.feature;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tusdk.pulse.Engine;
import com.tusdk.pulse.filter.FilterDisplayView;
import com.tusdk.pulse.filter.FilterPipe;
import com.tusdk.pulse.filter.Image;

import org.lasque.tusdkdemo.R;
import org.lasque.tusdkdemo.utils.Constants;
import org.lasque.tusdkdemo.views.bubble.BubbleItemView;
import org.lasque.tusdkdemo.views.bubble.BubbleLayerView;
import org.lasque.tusdkdemo.views.bubble.BubbleViewDelegate;
import org.lasque.tusdkpulse.core.TuSdkContext;
import org.lasque.tusdkpulse.core.exif.ExifInterface;
import org.lasque.tusdkpulse.core.struct.TuSdkSize;
import org.lasque.tusdkpulse.core.utils.ContextUtils;
import org.lasque.tusdkpulse.core.utils.ThreadHelper;
import org.lasque.tusdkpulse.core.utils.image.AlbumHelper;
import org.lasque.tusdkpulse.core.utils.image.BitmapHelper;
import org.lasque.tusdkpulse.core.utils.sqllite.ImageSqlHelper;

import java.io.File;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * TuSDK
 * org.lasque.tusdkdemo.examples.feature
 * FPDemo
 *
 * @author H.ys
 * @Date 2021/1/29  15:55
 * @Copyright (c) 2020 tusdk.com. All rights reserved.
 */
public class BubbleTextFragment extends Fragment {

    private BubbleLayerView mBubbleView;

    private FilterDisplayView mDisplayView;

    private FilterPipe mFP;

    private RecyclerView mBubbleList;

    private ImageView mBubbleCommit;

    private ImageView mBubbleClose;

    private ExecutorService mThreadPool = Executors.newSingleThreadExecutor();

    private Bitmap inputImage;

    private Image mLashImage;

    private BubbleAdapter mBubbleAdapter;

    private BubbleItemView mCurrentView;

    private ConstraintLayout mInputLayer;

    private EditText mInput;

    private Button mTextCommit;

    private Image mInputImage;

    private int mCurrentTextIndex = -1;

    private boolean isFirstCallSoftInput = false;

    protected boolean isNeedSaveImage = false;

    private OnClickListener mOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == mBubbleCommit.getId()) {
                mThreadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        isNeedSaveImage = true;
                        onDrawImage();
                    }
                });

            } else if (v.getId() == mBubbleClose.getId()) {
                getActivity().finish();
            }
        }
    };

    private int mWindowHeight = 0;

    private ViewTreeObserver.OnGlobalLayoutListener mKeyBoardListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            Rect r = new Rect();
            getActivity().getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
            int height = r.height();
            if (mWindowHeight == 0) {
                mWindowHeight = height;
            } else {
                if (mWindowHeight != height) {
                    int softKeyboardHeight = mWindowHeight - height;
                    ((ConstraintLayout.LayoutParams) mInputLayer.getLayoutParams()).setMargins(0, 0, 0, softKeyboardHeight);
                    mInputLayer.invalidate();
                }
            }
        }
    };

    private BubbleViewDelegate mDelegate = new BubbleViewDelegate() {
        @Override
        public void onItemViewSelected(BubbleItemView view) {
            mCurrentView = view;
        }

        @Override
        public void onItemViewReleased(BubbleItemView view) {

        }

        @Override
        public void onItemClosed(BubbleItemView view) {
            mCurrentView = null;
            mCurrentTextIndex = -1;
            mInputLayer.clearFocus();
            mInputLayer.setVisibility(View.INVISIBLE);
            hideSoftInput();
        }

        @Override
        public void onRefreshImage() {
            onDrawImage();
        }

        @Override
        public void onUpdateText(BubbleItemView view, final int textIndex) {
            mCurrentTextIndex = textIndex;
            final String currentText = mCurrentView.getTextByIndex(textIndex);
            ThreadHelper.post(new Runnable() {
                @Override
                public void run() {
                    mInput.setText(currentText);
                    mInputLayer.setVisibility(View.VISIBLE);
                    mInput.requestFocus();
                    showSoftInput();
                }
            });
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bubble_text_fragment, container, false);

        initFilterPipe();

        mDisplayView = view.findViewById(R.id.lsq_bubble_display_view);
        mDisplayView.init(Engine.getInstance().getMainGLContext());


        mBubbleView = view.findViewById(R.id.lsq_bubble_view);
        mBubbleView.setFilterPipe(mFP);
        mBubbleView.setThreadPool(mThreadPool);
        mBubbleView.setBubbleDelegate(mDelegate);

        mInputLayer = view.findViewById(R.id.lsq_text_input_layer);
        mInput = view.findViewById(R.id.lsq_text_input);
        mTextCommit = view.findViewById(R.id.lsq_edit_close);

        mInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String inputString = s.toString();
                if (mCurrentTextIndex != -1 && mCurrentView != null) {
                    mCurrentView.updateText(mCurrentTextIndex, inputString);
                }
            }
        });

        mTextCommit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mInputLayer.clearFocus();
                mInputLayer.setVisibility(View.GONE);
                hideSoftInput();
            }
        });

        SharedPreferences sp = TuSdkContext.context().getSharedPreferences("TU-TTF", Context.MODE_PRIVATE);
//        BubbleItem b1 = new BubbleItem("标签", sp.getString(Constants.BUBBLE_1, ""), "bubble_1");
//        BubbleItem b2 = new BubbleItem("粉色心", sp.getString(Constants.BUBBLE_2, ""), "bubble_2");
//        BubbleItem b3 = new BubbleItem("气泡7", sp.getString(Constants.BUBBLE_3, ""), "bubble_3");
//        BubbleItem b4 = new BubbleItem("气泡6", sp.getString(Constants.BUBBLE_4, ""), "bubble_4");
        BubbleItem b5 = new BubbleItem("message", sp.getString(Constants.BUBBLE_5, ""), "bubble_5");
        BubbleItem b6 = new BubbleItem("带劲", sp.getString(Constants.BUBBLE_6, ""), "bubble_6");
        BubbleItem b7 = new BubbleItem("快乐水", sp.getString(Constants.BUBBLE_7, ""), "bubble_7");

        mBubbleAdapter = new BubbleAdapter(getContext(), Arrays.asList(b5,b6,b7));
        mBubbleAdapter.setOnItemClickListener(new OnItemClickListener<BubbleItem, BubbleAdapter.BubbleViewHolder>() {
            @Override
            public void onItemClick(BubbleItem item, BubbleAdapter.BubbleViewHolder holder, int pos) {
                mBubbleView.appendBubble(item.path);
            }
        });
        mBubbleList = view.findViewById(R.id.lsq_bubble_model_list);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mBubbleList.setLayoutManager(manager);
        mBubbleList.setAdapter(mBubbleAdapter);

        mBubbleCommit = view.findViewById(R.id.lsq_bubble_commit);
        mBubbleCommit.setOnClickListener(mOnClickListener);

        mBubbleClose = view.findViewById(R.id.lsq_bubble_close);
        mBubbleClose.setOnClickListener(mOnClickListener);

        inputImage = BitmapHelper.getRawBitmap(getContext(), R.raw.sample_photo);

        final TuSdkSize size = TuSdkSize.create(inputImage);
        mBubbleView.setImageSize(size);
        mDisplayView.getViewTreeObserver().addOnDrawListener(new ViewTreeObserver.OnDrawListener() {
            @Override
            public void onDraw() {
                mThreadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        onDrawImage();
                    }
                });
                Rect rect = mDisplayView.getInteractionRect(size.width, size.height);
                mBubbleView.resize(rect);
                final ViewTreeObserver.OnDrawListener listener = this;
                mDisplayView.post(new Runnable() {
                    @Override
                    public void run() {
                        mDisplayView.getViewTreeObserver().removeOnDrawListener(listener);
                    }
                });
            }
        });

        getActivity().getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(mKeyBoardListener);

        return view;
    }

    private void initFilterPipe() {
        Future<Boolean> res = mThreadPool.submit(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                mFP = new FilterPipe();
                boolean ret = mFP.create();
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

    private void onDrawImage() {
        Image input = new Image(inputImage, 0);
        final Image output = mFP.process(input,null);
        input.release();
        if (isNeedSaveImage){
            Bitmap saveBitmap = output.toBitmap();
            saveBitmap = setWaterMark(saveBitmap);
            saveResource(saveBitmap);
            isNeedSaveImage = false;
        }
        mLashImage = output;
        mDisplayView.post(new Runnable() {
            @Override
            public void run() {
                mDisplayView.updateImage(output);
                output.release();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                onDrawImage();
            }
        });
    }

    private void showSoftInput() {
        View view = getActivity().getCurrentFocus();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getContext().getSystemService(InputMethodManager.class).showSoftInput(view, 0);
        } else {
            ((InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(view, 0);
        }
    }

    private void hideSoftInput() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getContext().getSystemService(InputMethodManager.class).hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), 0);
        } else {

            ((InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().getWindow().getDecorView().getViewTreeObserver().removeOnGlobalLayoutListener(mKeyBoardListener);
    }

    private Bitmap setWaterMark(Bitmap shotPhoto){
        Bitmap photo = shotPhoto;
        Bitmap waterMark = BitmapHelper.getRawBitmap(getContext(),R.raw.water);

        int margin = ContextUtils.dip2px(getContext(), 6);

        int width = photo.getWidth();
        int height = photo.getHeight();

        int paddingLeft = width - waterMark.getWidth() - margin;
        int paddingTop = margin;

        Bitmap newb = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newb);

        canvas.drawBitmap(photo, 0, 0, null);

        canvas.drawBitmap(waterMark, paddingLeft, paddingTop, null);

        canvas.save();

        canvas.restore();

        return newb;
    }

    /**
     * 保存拍照资源
     */
    public void saveResource(Bitmap bitmap) {
        ExifInterface exifInterface = new ExifInterface();
        exifInterface.setTagValue(ExifInterface.TAG_IMAGE_WIDTH,bitmap.getWidth());
        exifInterface.setTagValue(ExifInterface.TAG_IMAGE_LENGTH,bitmap.getByteCount());

        File file = null;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            file = AlbumHelper.getAlbumFileAndroidQ();
        } else {
            file = AlbumHelper.getAlbumFile(false);
        }
        ImageSqlHelper.saveJpgToAblum(getContext(), bitmap, 80, file,exifInterface);

        ThreadHelper.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext(),"图片保存成功",Toast.LENGTH_SHORT).show();
            }
        });

    }
}
