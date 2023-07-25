package org.lasque.tusdkdemo.examples.feature.jigsaw;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.tusdk.pulse.Config;
import com.tusdk.pulse.DispatchQueue;
import com.tusdk.pulse.Engine;
import com.tusdk.pulse.Property;
import com.tusdk.pulse.filter.Filter;
import com.tusdk.pulse.filter.FilterDisplayView;
import com.tusdk.pulse.filter.FilterPipe;
import com.tusdk.pulse.filter.Image;
import com.tusdk.pulse.filter.filters.JigsawFilter;

import org.lasque.tusdkdemo.R;
import org.lasque.tusdkdemo.views.jigsaw.JigsawItemView;
import org.lasque.tusdkdemo.views.jigsaw.JigsawLayerView;
import org.lasque.tusdkdemo.views.jigsaw.OnJigsawViewListener;
import org.lasque.tusdkpulse.TuSdkGeeV1;
import org.lasque.tusdkpulse.core.TuSdkContext;
import org.lasque.tusdkpulse.core.TuSdkResult;
import org.lasque.tusdkpulse.core.exif.ExifInterface;
import org.lasque.tusdkpulse.core.exif.ExifUtil;
import org.lasque.tusdkpulse.core.struct.TuSdkSize;
import org.lasque.tusdkpulse.core.utils.ContextUtils;
import org.lasque.tusdkpulse.core.utils.RectHelper;
import org.lasque.tusdkpulse.core.utils.TLog;
import org.lasque.tusdkpulse.core.utils.ThreadHelper;
import org.lasque.tusdkpulse.core.utils.image.AlbumHelper;
import org.lasque.tusdkpulse.core.utils.image.BitmapHelper;
import org.lasque.tusdkpulse.core.utils.image.ExifHelper;
import org.lasque.tusdkpulse.core.utils.image.RatioType;
import org.lasque.tusdkpulse.core.utils.sqllite.ImageSqlHelper;
import org.lasque.tusdkpulse.core.view.TuSdkTextView;
import org.lasque.tusdkpulse.impl.activity.TuFragment;
import org.lasque.tusdkpulse.impl.components.TuAlbumComponent;
import org.lasque.tusdkpulse.modules.components.TuSdkComponent;

import java.io.File;
import java.util.List;

/**
 * TuSDK
 * org.lasque.tusdkdemo.examples.feature.jigsaw
 * android-fp-demo
 *
 * @author H.ys
 * @Date 2021/7/7  14:52
 * @Copyright (c) 2020 tusdk.com. All rights reserved.
 */
public class JigsawFragment extends Fragment implements OnJigsawViewListener {

    private FilterDisplayView mDisplayView;

    private FilterPipe mFP;

    private DispatchQueue mRenderQueue;

    private String mCurrentJigsawPath;

    private Bitmap inputImage;

    private JigsawLayerView mLayerView;

    private JigsawFilter.PropertyBuilder mPropertyBuilder;

    private Filter mJigsawFilter;

    private TuSdkSize modelSize;

    private JigsawItemView mCurrentView;

    private ConstraintLayout mJigsawMenu;

    private TuSdkTextView mChangedItem;

    private TuSdkTextView mRotationItem;

    private TuSdkTextView mMirrorItem;

    private TuSdkTextView mFlipItem;


    private TuSdkTextView mPosterType;

    private TuSdkTextView mTemplateType;

    private TuSdkTextView mSplicingType;

    private TuSdkSize mCurrentRenderSize;

    private int mCurrentRatioType = RatioType.ratio_1_1;

    private ConstraintLayout mTemplateMenu;

    private TuSdkTextView mTemplatePadding;

    private TuSdkTextView mTemplateRatio;

    private boolean isLayout = false;

    private Image inImage = null;
    
    private ImageView mCommit;
    private ImageView mClose;

    protected boolean isNeedSaveImage = false;

    private boolean isChangedRatio = false;

    private int mCurrentPaddingSize = 0;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @org.jetbrains.annotations.NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.jigsaw_fragment,container,false);

        mDisplayView = view.findViewById(R.id.lsq_jigsaw_display_view);
        mDisplayView.init(Engine.getInstance().getMainGLContext());

        mDisplayView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLayerView.cancelAllItemSelected();
            }
        });

        initFilterPipe();

        mLayerView = view.findViewById(R.id.lsq_jigsaw_view);
        mLayerView.setRenderPool(mRenderQueue);
        mLayerView.setDelegate(this);


        SharedPreferences sp = TuSdkContext.context().getSharedPreferences("TU-TTF", Context.MODE_PRIVATE);

        String posterPath = sp.getString("jigsaw_ex","");
        mPosterType = view.findViewById(R.id.lsq_jigsaw_type_poster);
        mPosterType.setTag(posterPath);
        mPosterType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTemplateMenu.setVisibility(View.INVISIBLE);

                String path = (String) v.getTag();
                mCurrentJigsawPath = path;
                mLayerView.reset();
                mCurrentRenderSize = TuSdkSize.create(1080);
                mRenderQueue.runAsync(new Runnable() {
                    @Override
                    public void run() {
                        initJigsawFilter();
                        onDrawFrame();
                    }
                });

                mPosterType.setTextColor(Color.RED);
                mSplicingType.setTextColor(Color.WHITE);
                mTemplateType.setTextColor(Color.WHITE);
            }
        });

        String splicingPath = sp.getString("jigsaw_ex2","");
        mSplicingType = view.findViewById(R.id.lsq_jigsaw_type_splicing);
        mSplicingType.setTag(splicingPath);
        mSplicingType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTemplateMenu.setVisibility(View.INVISIBLE);

                String path = (String) v.getTag();
                mCurrentJigsawPath = path;
                mLayerView.reset();
                mCurrentRenderSize = TuSdkSize.create(1080,2400);
                mRenderQueue.runAsync(new Runnable() {
                    @Override
                    public void run() {
                        initJigsawFilter();
                        onDrawFrame();
                    }
                });

                mPosterType.setTextColor(Color.WHITE);
                mSplicingType.setTextColor(Color.RED);
                mTemplateType.setTextColor(Color.WHITE);
            }
        });

        String templatePath = sp.getString("jigsaw_ex3","");
        mTemplateType = view.findViewById(R.id.lsq_jigsaw_type_template);
        mTemplateType.setTag(templatePath);
        mTemplateType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTemplateMenu.setVisibility(View.VISIBLE);


                String path = (String) v.getTag();
                mCurrentJigsawPath = path;
                currentRatioSize();
                mRenderQueue.runAsync(new Runnable() {
                    @Override
                    public void run() {
                        initJigsawFilter();
                        onDrawFrame();
                    }
                });

                mPosterType.setTextColor(Color.WHITE);
                mSplicingType.setTextColor(Color.WHITE);
                mTemplateType.setTextColor(Color.RED);
            }
        });

        mCurrentJigsawPath = posterPath;
        mCurrentRenderSize = TuSdkSize.create(1080);
        mPosterType.setTextColor(Color.RED);

        TLog.e("model path %s",mCurrentJigsawPath);

        inputImage = BitmapHelper.getRawBitmap(getContext(), R.raw.sample_photo);

        mDisplayView.getViewTreeObserver().addOnDrawListener(new ViewTreeObserver.OnDrawListener() {
            @Override
            public void onDraw() {
                if (!isLayout){
                    mLayerView.reset();
                    mRenderQueue.runAsync(new Runnable() {
                        @Override
                        public void run() {
                            initJigsawFilter();
                            onDrawFrame();
                        }
                    });
                    final ViewTreeObserver.OnDrawListener listener = this;
                    mDisplayView.post(new Runnable() {
                        @Override
                        public void run() {
                            mDisplayView.getViewTreeObserver().removeOnDrawListener(listener);
                        }
                    });

                    isLayout = true;
                }

            }
        });

        mJigsawMenu = view.findViewById(R.id.lsq_jigasw_menu);

        mChangedItem = view.findViewById(R.id.lsq_jigsaw_changed_image);
        mChangedItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TuAlbumComponent comp = TuSdkGeeV1.albumCommponent(getActivity(), new TuSdkComponent.TuSdkComponentDelegate()
                {
                    @Override
                    public void onComponentFinished(TuSdkResult result, Error error, TuFragment lastFragment)
                    {
                        if (mCurrentView != null){
                            JigsawFilter.ImageLayerInfo info = mCurrentView.getCurrentInfo();
                            String newImagePath = result.imageSqlInfo.path;
                            TuSdkSize newSize = BitmapHelper.getBitmapSize(new File(newImagePath));

                            TuSdkSize pitsSize = TuSdkSize.create(((int) (modelSize.width * info.dsc_rect.width())), ((int) (modelSize.height * info.dsc_rect.height())));

                            if (newSize.width > newSize.height){
                                TuSdkSize imageRenderSize = TuSdkSize.create(((int) (pitsSize.height * newSize.maxMinRatio())),pitsSize.height);

                                float widthPercent = ((float) pitsSize.width) / imageRenderSize.width;

                                info.scale = 1;
                            } else {
                                TuSdkSize imageRenderSize = TuSdkSize.create(pitsSize.width, ((int) (pitsSize.width * newSize.maxMinRatio())));

                                float heightPercent = ((float) pitsSize.height) / imageRenderSize.height;

                                info.scale = 1;
                            }

                            info.path = newImagePath;
                            info.offSet.set(0,0);

                            mCurrentView.setImageLayerInfo(info);
                        }

                        mRenderQueue.runSync(new Runnable() {
                            @Override
                            public void run() {
                                onUpdateProperty();
                                onDrawFrame();
                            }
                        });
                    }
                });

                comp.setAutoDismissWhenCompleted(true)
                        // 显示组件
                        .showComponent();
            }
        });

        mRotationItem = view.findViewById(R.id.lsq_jigsaw_rotation);
        mRotationItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentView != null){
                    JigsawFilter.ImageLayerInfo info = mCurrentView.getCurrentInfo();

                    info.rotation =  ((info.rotation / 90) + 1) * 90;

                    mRenderQueue.runSync(new Runnable() {
                        @Override
                        public void run() {
                            onUpdateProperty();
                            onDrawFrame();
                        }
                    });
                }
            }
        });

        mMirrorItem = view.findViewById(R.id.lsq_jigsaw_mirror);
        mMirrorItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentView != null){
                    JigsawFilter.ImageLayerInfo info = mCurrentView.getCurrentInfo();

                    info.isHorizontalFlip = !info.isHorizontalFlip;

                    mRenderQueue.runSync(new Runnable() {
                        @Override
                        public void run() {
                            onUpdateProperty();
                            onDrawFrame();
                        }
                    });
                }

            }
        });

        mFlipItem = view.findViewById(R.id.lsq_jigsaw_flip);
        mFlipItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentView != null){
                    JigsawFilter.ImageLayerInfo info = mCurrentView.getCurrentInfo();

                    info.isVerticallyFlip = !info.isVerticallyFlip;

                    mRenderQueue.runSync(new Runnable() {
                        @Override
                        public void run() {
                            onUpdateProperty();
                            onDrawFrame();
                        }
                    });
                }
            }
        });

        mTemplateMenu = view.findViewById(R.id.lsq_template_menu);

        mTemplatePadding = view.findViewById(R.id.lsq_template_padding);
        mTemplatePadding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String paddingBtnName = "";
                int padddingSize = 0;
                switch (mCurrentPaddingSize){
                    case 0:
                        padddingSize = 10;
                        paddingBtnName = "边框 : 小";
                        break;
                    case 10:
                        padddingSize = 20;
                        paddingBtnName = "边框 : 中";
                        break;
                    case 20:
                        padddingSize = 30;
                        paddingBtnName = "边框 : 大";
                        break;
                    default:
                        padddingSize = 0;
                        paddingBtnName = "边框 : 无";
                        break;
                }
                mCurrentPaddingSize = padddingSize;
                mTemplatePadding.setText(paddingBtnName);
                if (mCurrentPaddingSize > 0){
                    mTemplatePadding.setTextColor(Color.RED);
                } else {
                    mTemplatePadding.setTextColor(Color.WHITE);
                }

                List<JigsawItemView> allViews = mLayerView.getAllViews();

                for (JigsawItemView currentView : allViews){
                    if (currentView != null){
                        JigsawFilter.ImageLayerInfo info = currentView.getCurrentInfo();
                        info.padding.set(padddingSize,padddingSize,padddingSize,padddingSize);
                    }
                }

                mRenderQueue.runSync(new Runnable() {
                    @Override
                    public void run() {
                        onUpdateProperty();
                        onDrawFrame();
                    }
                });


            }
        });

        mTemplateRatio = view.findViewById(R.id.lsq_template_ratio);
        mTemplateRatio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isChangedRatio = true;
                nextRatio();
                String path = (String) mTemplateType.getTag();
                mCurrentJigsawPath = path;
                mRenderQueue.runSync(new Runnable() {
                    @Override
                    public void run() {
                        initJigsawFilter();
                        onDrawFrame();

                        isChangedRatio = false;
                    }
                });
            }
        });

        mCommit = view.findViewById(R.id.lsq_jigsaw_commit);
        mCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRenderQueue.runSync(new Runnable() {
                    @Override
                    public void run() {
                        TLog.e("start image save");
                        isNeedSaveImage = true;
                        onDrawFrame();
                    }
                });
            }
        });

        mClose = view.findViewById(R.id.lsq_jigsaw_close);
        mClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        return view;
    }

    private void initFilterPipe(){
        mRenderQueue = new DispatchQueue();
        mRenderQueue.runSync(new Runnable() {
            @Override
            public void run() {
                mFP = new FilterPipe();
                boolean ret = mFP.create();
            }
        });
    }

    private void initJigsawFilter(){

        Filter old = mFP.getFilter(100);
        if (old != null){
            mFP.deleteFilter(100);
            old.release();
        }

        Filter jigsaw = new Filter(mFP.getContext(),JigsawFilter.TYPE_NAME);
        Config config = new Config();
        config.setNumber(JigsawFilter.CONFIG_MODEL_TYPE,JigsawFilter.TYPE_FILE);
        config.setString(JigsawFilter.CONFIG_MODEL_PATH,mCurrentJigsawPath);
        config.setNumber(JigsawFilter.CONFIG_WIDTH,mCurrentRenderSize.width);
        config.setNumber(JigsawFilter.CONFIG_HEIGHT,mCurrentRenderSize.height);

        jigsaw.setConfig(config);

        mFP.addFilter(100,jigsaw);
        mJigsawFilter = jigsaw;

        Property property = jigsaw.getProperty(JigsawFilter.PROP_INTERACTION_INFO);
        try {
            final JigsawFilter.InteractionInfo interactionInfo = new JigsawFilter.InteractionInfo(property);

            ThreadHelper.post(new Runnable() {
                @Override
                public void run() {
                    final TuSdkSize size = new TuSdkSize(mCurrentRenderSize.width,mCurrentRenderSize.height);

                    ConstraintLayout.LayoutParams layoutParams =((ConstraintLayout.LayoutParams) mDisplayView.getLayoutParams());
                    layoutParams.height = (int) (layoutParams.width * size.maxMinRatio());
                    mDisplayView.setLayoutParams(layoutParams);

                    final Rect rect = mDisplayView.getInteractionRect(size.width,size.height);

                    mLayerView.setModelSize(size);
                    mLayerView.resize(rect);

                    modelSize = size;
                }
            });


            if (isChangedRatio){
                onUpdateProperty();
                for (JigsawFilter.InteractionInfo.LayerInfo item : interactionInfo.layerInfos){
                    final JigsawItemView targetView = mLayerView.getTargetView(item.index);
                    if (targetView == null) continue;
                    final JigsawFilter.ImageLayerInfo imageLayer = targetView.getCurrentInfo();
                    imageLayer.dsc_rect = item.dsc_rect;
                    ThreadHelper.post(new Runnable() {
                        @Override
                        public void run() {
                            targetView.updateImageLayerInfo(imageLayer);
                        }
                    });
                }


            } else {
                JigsawFilter.PropertyBuilder builder = new JigsawFilter.PropertyBuilder();

                JigsawFilter.PropertyHolder holder = builder.holder;

                for (JigsawFilter.InteractionInfo.LayerInfo item : interactionInfo.layerInfos){
                    final JigsawFilter.ImageLayerInfo imageLayer = new JigsawFilter.ImageLayerInfo();
                    imageLayer.index = item.index;
                    imageLayer.path = item.path;
                    imageLayer.dsc_rect.set(item.dsc_rect);
                    holder.layerInfos.add(imageLayer);
                    ThreadHelper.post(new Runnable() {
                        @Override
                        public void run() {
                            mLayerView.appendJigsawView(imageLayer);
                        }
                    });
                }

                mPropertyBuilder = builder;
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void onDrawFrame(){
        if (inImage == null){
            inImage = new Image(inputImage, 0);
        }
        final Image output = mFP.process(inImage,null);
        if (isNeedSaveImage){
            Bitmap saveBitmap = output.toBitmap();
            saveBitmap = setWaterMark(saveBitmap);
            saveResource(saveBitmap);
            isNeedSaveImage = false;
        }
        mDisplayView.post(new Runnable() {
            @Override
            public void run() {
                mDisplayView.updateImage(output);
                output.release();
            }
        });

    }

    @Override
    public void onItemViewSelected(JigsawItemView view) {
        mCurrentView = view;

        mJigsawMenu.setVisibility(View.VISIBLE);

        JigsawFilter.ImageLayerInfo info = mCurrentView.getCurrentInfo();
        String paddingBtnName = "";
        switch (info.padding.left){
            case 10:
                paddingBtnName = "边框 : 小";
                break;
            case 20:
                paddingBtnName = "边框 : 中";
                break;
            case 30:
                paddingBtnName = "边框 : 大";
                break;
            default:
                paddingBtnName = "边框 : 无";
                break;
        }

        if (info.padding.left > 0){
            mTemplatePadding.setTextColor(Color.RED);
        } else {
            mTemplatePadding.setTextColor(Color.WHITE);
        }

        mTemplatePadding.setText(paddingBtnName);



    }

    @Override
    public void onItemViewReleased(JigsawItemView view) {

    }

    @Override
    public void onItemClosed(JigsawItemView view) {
//        mJigsawMenu.setVisibility(View.GONE);

    }

    @Override
    public void onRefreshImage() {
        onDrawFrame();
    }

    @Override
    public void onUpdateProperty() {
        mJigsawFilter.setProperty(JigsawFilter.PROP_PARAM,mPropertyBuilder.makeProperty());
    }

    @Override
    public void onCancelAllView() {
//        mJigsawMenu.setVisibility(View.GONE);
    }

    private void nextRatio(){
        mCurrentRatioType = RatioType.nextRatioType(RatioType.ratio_default,mCurrentRatioType,RatioType.ratio_orgin);
        switch (mCurrentRatioType){
            case RatioType.ratio_1_1:
                mCurrentRenderSize.height = mCurrentRenderSize.width;
                mTemplateRatio.setText("1:1");
                break;
            case RatioType.ratio_2_3:
                mTemplateRatio.setText("2:3");
                mCurrentRenderSize.height = mCurrentRenderSize.width / 2 * 3;
                break;
            case RatioType.ratio_3_4:
                mTemplateRatio.setText("3:4");
                mCurrentRenderSize.height = mCurrentRenderSize.width / 3 * 4;
                break;
            case RatioType.ratio_9_16:
                mTemplateRatio.setText("9:16");
                mCurrentRenderSize.height = mCurrentRenderSize.width / 9 * 16;
                break;
        }
    }

    private void currentRatioSize(){
        switch (mCurrentRatioType){
            case RatioType.ratio_1_1:
                mCurrentRenderSize.height = mCurrentRenderSize.width;
                mTemplateRatio.setText("1:1");
                break;
            case RatioType.ratio_2_3:
                mTemplateRatio.setText("2:3");
                mCurrentRenderSize.height = mCurrentRenderSize.width / 2 * 3;
                break;
            case RatioType.ratio_3_4:
                mTemplateRatio.setText("3:4");
                mCurrentRenderSize.height = mCurrentRenderSize.width / 3 * 4;
                break;
            case RatioType.ratio_9_16:
                mTemplateRatio.setText("9:16");
                mCurrentRenderSize.height = mCurrentRenderSize.width / 9 * 16;
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (inImage != null){
            inImage.release();
        }
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
