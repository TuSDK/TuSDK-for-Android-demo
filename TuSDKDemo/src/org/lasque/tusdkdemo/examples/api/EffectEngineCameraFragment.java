package org.lasque.tusdkdemo.examples.api;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.SurfaceTexture;

import com.tusdk.pulse.Engine;
import com.tusdk.pulse.filter.FilterDisplayView;
import com.tusdk.pulse.filter.Image;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.lasque.tusdkpulse.core.TuSdk;
import org.lasque.tusdkpulse.core.TuSdkContext;
import org.lasque.tusdkpulse.core.TuSdkResult;
import org.lasque.tusdkpulse.core.seles.SelesParameters;
import org.lasque.tusdkpulse.core.struct.TuSdkSize;
import org.lasque.tusdkpulse.core.utils.ColorUtils;
import org.lasque.tusdkpulse.core.utils.TLog;
import org.lasque.tusdkpulse.core.utils.ThreadHelper;
import org.lasque.tusdkpulse.core.utils.anim.AccelerateDecelerateInterpolator;
import org.lasque.tusdkpulse.core.utils.hardware.CameraConfigs;
import org.lasque.tusdkpulse.core.utils.hardware.CameraHelper;
import org.lasque.tusdkpulse.core.utils.hardware.InterfaceOrientation;
import org.lasque.tusdkpulse.core.view.TuSdkViewHelper;
import org.lasque.tusdkpulse.cx.api.TuCamera1Shower;
import org.lasque.tusdkpulse.cx.api.TuFilterCombo;
import org.lasque.tusdkpulse.cx.api.TuFilterCombo.*;
import org.lasque.tusdkpulse.cx.api.TuFilterController;
import org.lasque.tusdkpulse.cx.api.TuFilterFactory;
import org.lasque.tusdkpulse.cx.api.impl.TuCamera1ShowerImpl;
import org.lasque.tusdkpulse.cx.api.impl.TuFilterComboImpl;
import org.lasque.tusdkpulse.cx.api.impl.TuFilterControllerImpl;
import org.lasque.tusdkpulse.cx.hardware.camera.TuCamera;
import org.lasque.tusdkpulse.cx.hardware.camera.TuCameraShot;
import org.lasque.tusdkpulse.cx.seles.view.TuEGLContextFactory;
import org.lasque.tusdkpulse.impl.activity.TuFragment;
import org.lasque.tusdkpulse.impl.components.camera.TuCameraFilterView;
import org.lasque.tusdkpulse.impl.components.widget.filter.FilterParameterConfigView;
import org.lasque.tusdkpulse.modules.view.widget.filter.GroupFilterItem;
import org.lasque.tusdkdemo.R;

import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorListenerAdapter;

/******************************************************************
 * droid-sdk-image
 * org.lasque.tusdkdemo.examples.api
 *
 * @author      : Clear
 * @Date        : 2020/6/9 12:32 PM
 * @Copyright   : (c) 2020 tutucloud.com. All rights reserved.
 * @brief       : 特效相机视图控制器
 * @details     : 
 ******************************************************************/

// 特效相机视图控制器
public class EffectEngineCameraFragment extends TuFragment
{
    private final static String TAG = "EffectEngineCameraFragment";
    /** 布局ID */
    public static final int layoutId = R.layout.demo_effect_engine_camera_fragment;

    public EffectEngineCameraFragment()
    {
        this.setRootViewLayoutId(layoutId);
    }

    /** 相机视图 */
    private RelativeLayout cameraView;
    /** GL视图 */
    private FilterDisplayView surfaceView;
    /** 相机渲染接口 */
    private TuCamera1Shower mCameraShower;
    /** 取消按钮 */
    private TextView cancelButton;
    /** 闪光灯栏 */
    private LinearLayout flashBar;
    /** 切换前后摄像头按钮 */
    private TextView switchCameraButton;
    /** 拍摄按钮 */
    private Button captureButton;
    /** 滤镜选择栏 */
    private TuCameraFilterView filterBar;
    /** 滤镜参数调节 */
    private FilterParameterConfigView mFilterParamsView;
    /** 滤镜开关按钮 */
    private TextView filterToggleButton;

    // 特效视图开关
    private TextView effectButton;
    // 特效视图
    private RelativeLayout effectView;
    // 特效控制栏
    private LinearLayout effectBar;
    // 特效参数设置视图
    FilterParameterConfigView effectParamsView;
    // 磨皮开关
    private TextView skinDisableBtn;
    // 光滑皮肤按钮
    private TextView skinSleekBtn;
    // 纹理皮肤按钮
    private TextView skinVeinBtn;

    // 哈哈镜关闭按钮
    private TextView monsterDisableBtn;
    // 哈哈镜按钮 - 大鼻子
    private TextView monsterBigNoseBtn;
    // 哈哈镜按钮 - 大饼脸
    private TextView monsterPieFaceBtn;
    // 哈哈镜按钮 - 国字脸
    private TextView monsterSquareFaceBtn;
    // 哈哈镜按钮 - 厚嘴唇
    private TextView monsterThickLipsBtn;
    // 哈哈镜按钮 - 眯眯眼
    private TextView monsterSmallEyesBtn;
    // 哈哈镜按钮 - 木瓜脸
    private TextView monsterPapayaFaceBtn;
    // 哈哈镜按钮 - 蛇精脸
    private TextView monsterSnakeFaceBtn;
    // 切换人脸塑性开关
    private TextView plasticBtn;
    // 磨皮开关
    private ArrayList<TextView> skinBtns = new ArrayList<>(3);
    // 哈哈镜开关
    private ArrayList<TextView> monsterBtns = new ArrayList<>(8);

    // 组合滤镜接口
    private TuFilterCombo mController;
    // 特效参数
    private SelesParameters mParams;

    // onCreateView
    @Override
    protected void loadView(ViewGroup view)
    {
        // 相机视图
        cameraView = this.getViewById(R.id.cameraView);
        // 配置栏
        // configBar = this.getViewById(R.id.configBar);
        // 取消按钮
        cancelButton = this.getViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(mClickListener);
        // 闪光灯栏
        flashBar = this.getViewById(R.id.flashBar);
        this.showViewIn(flashBar, false);
        // 切换前后摄像头按钮
        switchCameraButton = this.getViewById(R.id.switchCameraButton);
        switchCameraButton.setOnClickListener(mClickListener);
        // 设置是否显示前后摄像头切换按钮
        this.showViewIn(switchCameraButton, CameraHelper.cameraCounts() > 1);
        // 底部栏
        // bottomBar = this.getViewById(R.id.bottomBar);
        // 拍摄按钮
        captureButton = this.getViewById(R.id.captureButton);
        captureButton.setOnClickListener(mClickListener);

        // 滤镜参数调节
        mFilterParamsView = this.getViewById("lsq_filter_parameter_config_view");
        mFilterParamsView.setVisibility(View.GONE);

        // 滤镜开关按钮
        filterToggleButton = this.getViewById(R.id.filterButton);
        filterToggleButton.setOnClickListener(mClickListener);
        // 滤镜选择栏
        filterBar = this.getViewById(R.id.lsq_group_filter_view);
        // 设置控制器
        filterBar.setActivity(this.getActivity());
        // 绑定选择委托
        filterBar.setDelegate(mFilterBarDelegate);

        // 设置默认是否显示
        filterBar.setDefaultShowState(true);
        // 显示滤镜标题视图
        filterBar.setDisplaySubtitles(true);

        // 滤镜选择栏 设置SDK内置滤镜
        filterBar.loadFilters();

        // 特效视图开关
        effectButton = this.getViewById(R.id.effectButton);
        effectButton.setOnClickListener(mClickListener);
        // 特效视图
        effectView = this.getViewById(R.id.lsq_effect_view);
        effectView.setOnClickListener(mClickListener);
        this.showViewIn(effectView, false);

        // 特效控制栏
        effectBar = this.getViewById(R.id.lsq_effect_bar);

        // 特效参数设置视图
        effectParamsView = this.getViewById(R.id.lsq_effect_params_view);
        effectParamsView.setVisibility(View.GONE);
        // 磨皮开关
        skinDisableBtn = this.getViewById(R.id.lsq_skin_disable_btn);
        skinDisableBtn.setOnClickListener(mClickListener);
        skinDisableBtn.setTag(TuComboSkinMode.Empty);
        skinBtns.add(skinDisableBtn);
        // 光滑皮肤按钮
        skinSleekBtn = this.getViewById(R.id.lsq_skin_sleek_btn);
        skinSleekBtn.setOnClickListener(mClickListener);
        skinSleekBtn.setTag(TuComboSkinMode.Sleek);
        skinBtns.add(skinSleekBtn);
        // 纹理皮肤按钮
        skinVeinBtn = this.getViewById(R.id.lsq_skin_vein_btn);
        skinVeinBtn.setOnClickListener(mClickListener);
        skinVeinBtn.setTag(TuComboSkinMode.Vein);
        skinBtns.add(skinVeinBtn);
        skinModeChange(TuComboSkinMode.Empty);

        // 哈哈镜关闭按钮
        monsterDisableBtn = this.getViewById(R.id.lsq_monster_disable_btn);
        monsterDisableBtn.setOnClickListener(mClickListener);
        monsterDisableBtn.setTag(TuFaceMonsterMode.Empty);
        monsterBtns.add(monsterDisableBtn);
        // 哈哈镜按钮 - 大鼻子
        monsterBigNoseBtn = this.getViewById(R.id.lsq_monster_bigNose_btn);
        monsterBigNoseBtn.setOnClickListener(mClickListener);
        monsterBigNoseBtn.setTag(TuFaceMonsterMode.BigNose);
        monsterBtns.add(monsterBigNoseBtn);
        // 哈哈镜按钮 - 大饼脸
        monsterPieFaceBtn = this.getViewById(R.id.lsq_monster_PieFace_btn);
        monsterPieFaceBtn.setOnClickListener(mClickListener);
        monsterPieFaceBtn.setTag(TuFaceMonsterMode.PieFace);
        monsterBtns.add(monsterPieFaceBtn);
        // 哈哈镜按钮 - 国字脸
        monsterSquareFaceBtn = this.getViewById(R.id.lsq_monster_SquareFace_btn);
        monsterSquareFaceBtn.setOnClickListener(mClickListener);
        monsterSquareFaceBtn.setTag(TuFaceMonsterMode.SquareFace);
        monsterBtns.add(monsterSquareFaceBtn);
        // 哈哈镜按钮 - 厚嘴唇
        monsterThickLipsBtn = this.getViewById(R.id.lsq_monster_ThickLips_btn);
        monsterThickLipsBtn.setOnClickListener(mClickListener);
        monsterThickLipsBtn.setTag(TuFaceMonsterMode.ThickLips);
        monsterBtns.add(monsterThickLipsBtn);
        // 哈哈镜按钮 - 眯眯眼
        monsterSmallEyesBtn = this.getViewById(R.id.lsq_monster_SmallEyes_btn);
        monsterSmallEyesBtn.setOnClickListener(mClickListener);
        monsterSmallEyesBtn.setTag(TuFaceMonsterMode.SmallEyes);
        monsterBtns.add(monsterSmallEyesBtn);
        // 哈哈镜按钮 - 木瓜脸
        monsterPapayaFaceBtn = this.getViewById(R.id.lsq_monster_PapayaFace_btn);
        monsterPapayaFaceBtn.setOnClickListener(mClickListener);
        monsterPapayaFaceBtn.setTag(TuFaceMonsterMode.PapayaFace);
        monsterBtns.add(monsterPapayaFaceBtn);
        // 哈哈镜按钮 - 蛇精脸
        monsterSnakeFaceBtn = this.getViewById(R.id.lsq_monster_SnakeFace_btn);
        monsterSnakeFaceBtn.setOnClickListener(mClickListener);
        monsterSnakeFaceBtn.setTag(TuFaceMonsterMode.SnakeFace);
        monsterBtns.add(monsterSnakeFaceBtn);
        monsterModeChange(TuFaceMonsterMode.Empty);

        // 切换人脸塑性开关
        plasticBtn = this.getViewById(R.id.lsq_plastic_btn);
        plasticBtn.setTag(false);
        plasticBtn.setOnClickListener(mClickListener);

        // GL视图
        surfaceView = this.getViewById(R.id.surfaceView);
        if (surfaceView == null) return;
        /** step.1: 相机渲染 */
        mCameraShower = new TuCamera1ShowerImpl();
        /** step.2: 请求初始化 */
        mCameraShower.requestInit();

        /** step.5: 创建相机数据监听，并通知刷新到界面 */
        mCameraShower.setFrameAvailableListener(new SurfaceTexture.OnFrameAvailableListener(){
            @Override
            public void onFrameAvailable(SurfaceTexture surfaceTexture) {
                Image res = mCameraShower.onDrawFrame();
                surfaceView.updateImage(res);
            }
        });

        surfaceView.init(Engine.getInstance().getMainGLContext());

        // 全画幅，开启后为最大可显示区域, 默认按最大预览显示区域 [一般最大预览区域为16:9小于全画幅4:3]
        // 如果视频录制输出需要类似1920*1080，需要设置为false
        mCameraShower.camera().setFullFrame(true);

        /** step.6: 准备初始化相机 */
        if (!mCameraShower.prepare()) return;
        // 设置默认开启相机方向
        mCameraShower.camera().cameraBuilder().setDefaultFacing(CameraConfigs.CameraFacing.Front);
        // 设置默认相机方向
        mCameraShower.camera().cameraOrient().setOutputImageOrientation(InterfaceOrientation.Portrait);
        // 水平镜像前置摄像头
        mCameraShower.camera().cameraOrient().setHorizontallyMirrorFrontFacingCamera(true);

        TuSdkSize size = TuSdkContext.getScreenSize();

        mCameraShower.setPreviewRatio(size.width,size.height);
        // 显示选区百分比
        // mCameraShower.setDisplayRect(new RectF(0, 0, 1.0f, 1.0f));
        /** step.7: 设置相机状态监听 */
        mCameraShower.camera().setCameraListener(new TuCamera.TuCameraListener() {
            @Override
            public void onStatusChanged(CameraConfigs.CameraState status, TuCamera camera) {

            }
        });

        /** step.8: 设置相机拍照监听 */
        mCameraShower.camera().cameraShot().setShotListener(new TuCameraShot.TuCameraShotListener() {
            /** 拍摄照片失败 */
            @Override
            public void onCameraShotFailed(TuSdkResult data) {
                TLog.e("%s onCameraShotFailed", TAG);
            }

            /** 直接Bitmap TuSdkResult.image */
            @Override
            public void onCameraShotBitmap(TuSdkResult data) {
                if (mCameraShower == null) return;
                final Bitmap img = mCameraShower.filterImage(data.image);
                ThreadHelper.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        test(img);
                    }
                });
            }
        });

        /** step.9: 启动相机 [优先开启后置相机] */
        // 自动通过 onResume()调用
        // mCameraShower.camera().startPreview(CameraConfigs.CameraFacing.Back);
    }

    // onPreDraw
    @Override
    protected void viewDidLoad(ViewGroup view)
    {

    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (!this.isFragmentPause())
        {
            this.startCameraCapture();
        }
    }

    @Override
    public void onPause()
    {
        super.onPause();
        this.stopCameraCapture();
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        this.destroyCamera();
    }

    /******************************** TuFilterFactory ***************************************/
    private TuFilterFactory mFilterFactory = new TuFilterFactory() {
        @Override
        public TuFilterController build() {
            if (mController == null){
                mController = new TuFilterComboImpl(false);
            }
            return mController;
        }
    };

    /******************************** Camera Controller ***************************************/

    /** Start camera capturing */
    private void startCameraCapture()
    {
        if (mCameraShower == null) return;
        mCameraShower.camera().startPreview();
    }

    /** Stop camera capturing */
    private void stopCameraCapture()
    {
        if (mCameraShower == null) return;
        mCameraShower.camera().stopPreview();
    }

    /** Pause camera capturing */
    private void pauseCameraCapture()
    {
        if (mCameraShower == null) return;
        mCameraShower.camera().pausePreview();
    }

    /** Resume camera capturing */
    private void resumeCameraCapture()
    {
        mCameraShower.camera().resumePreview();
    }

    /** This flips between the front and rear cameras */
    public void rotateCamera()
    {
        if (mCameraShower == null) return;
        mCameraShower.camera().rotateCamera();
    }

    /** Capture Image */
    private void captureImage() {
        if (mCameraShower == null) return;
        mCameraShower.camera().shotPhoto();
    }

    /** Destroy camera */
    private void destroyCamera()
    {
        if (mCameraShower == null) return;
        mCameraShower.destroy();
        mCameraShower = null;
    }

    /** 滤镜选择栏委托 */
    private TuCameraFilterView.TuCameraFilterViewDelegate mFilterBarDelegate = new TuCameraFilterView.TuCameraFilterViewDelegate()
    {
        /**
         * @param view 滤镜分组视图
         * @param itemData 滤镜分组元素
         * @param canCapture 是否允许拍摄
         * @return 是否允许继续执行
         */
        @Override
        public boolean onGroupFilterSelected(TuCameraFilterView view, GroupFilterItem itemData, boolean canCapture)
        {
            // 直接拍照
            if (canCapture) {
                captureImage();
                return true;
            }

            switch (itemData.type)
            {
                case TypeFilter:// 设置滤镜
                {
                    if (mCameraShower == null) return false;
                    SelesParameters params = mCameraShower.changeFilter(itemData.filterOption == null ? null : itemData.filterOption.code);
                    // 磨皮滤镜与美肤相互排斥
                    if (params != null && mParams != null &&  params.getModel() == mParams.getModel()){
                        skinModeChange(TuComboSkinMode.Empty);
                    }

                    mFilterParamsView.setFilterParams(params);
                }
                default: return true;
            }
        }

        /** 滤镜栏状态已改变通知 */
        @Override
        public void onGroupFilterShowStateChanged(TuCameraFilterView view, boolean isShow)
        {
            if (isShow) return;
            mFilterParamsView.setVisibility(View.GONE);
        }

        /** 滤镜栏状态将要改变通知 */
        @Override
        public void onGroupFilterShowStateWillChanged(TuCameraFilterView view,boolean isShow)
        {
            if (mFilterParamsView == null) return;
            if (!isShow || !mFilterParamsView.hasFilterParams()) mFilterParamsView.setVisibility(View.GONE);
                // 第一次显示时执行动画再次切换滤镜时不执行
            else if (mFilterParamsView.getVisibility() == View.GONE) {
                mFilterParamsView.setVisibility(View.VISIBLE);
                mFilterParamsView.setAlpha(0);
                ViewCompat.animate(mFilterParamsView).alpha(1).setDuration(400);
            }
        }
    };

    /** 按钮点击事件 */
    private View.OnClickListener mClickListener = new TuSdkViewHelper.OnSafeClickListener()
    {
        @Override
        public void onSafeClick(View v)
        {
            switch (v.getId())
            {
                // 取消
                case R.id.cancelButton:
                    dismissActivityWithAnim();
                    break;
                // 切换摄像头
                case R.id.switchCameraButton:
                    rotateCamera();
                    break;
                // 拍摄
                case R.id.captureButton:
                    captureImage();
                    break;
                // 滤镜开关切换按钮
                case R.id.filterButton:
                    filterBar.showGroupView();
                    break;
                // 特效视图开关
                case R.id.effectButton:
                    effectViewShow(true);
                    break;
                case R.id.lsq_effect_view:
                    effectViewShow(false);
                    break;
                case R.id.lsq_skin_disable_btn:
                case R.id.lsq_skin_sleek_btn:
                case R.id.lsq_skin_vein_btn:
                    skinModeChange((TuComboSkinMode)v.getTag());
                    break;
                case R.id.lsq_monster_disable_btn:
                case R.id.lsq_monster_bigNose_btn:
                case R.id.lsq_monster_PieFace_btn:
                case R.id.lsq_monster_SquareFace_btn:
                case R.id.lsq_monster_ThickLips_btn:
                case R.id.lsq_monster_SmallEyes_btn:
                case R.id.lsq_monster_PapayaFace_btn:
                case R.id.lsq_monster_SnakeFace_btn:
                    monsterModeChange((TuFaceMonsterMode)v.getTag());
                    break;
                case R.id.lsq_plastic_btn:
                    plasticChange(!(Boolean)v.getTag());
                    break;
                default:
                    break;
            }
        }
    };

    // 设置特效视图是否显示
    private void effectViewShow(final boolean show){
        float y = show ? 0 : effectBar.getHeight();
        showViewIn(effectView, true);
        ViewCompat.animate(effectBar).translationY(y).setDuration(220).
                setInterpolator(new AccelerateDecelerateInterpolator()).setListener((new ViewPropertyAnimatorListenerAdapter()
        {
            @Override
            public void onAnimationEnd(View view)
            {
                showViewIn(effectParamsView, show && mParams != null);
                showViewIn(effectView, show);
            }
        }));
    }

    // 磨皮模式更换
    private void skinModeChange(TuComboSkinMode mode){
        if (mController != null)
            mParams = mController.changeSkin(mode);

        effectParamsView.setFilterParams(mParams);

        for (TextView view : skinBtns){
            if (view.getTag() == mode){
                view.setBackgroundColor(Color.argb(126, 255, 255, 255));
                view.setTextColor(Color.BLACK);
            }else{
                view.setBackgroundColor(0);
                view.setTextColor(Color.WHITE);
            }
        }
    }

    // 哈哈镜模式更换
    private void monsterModeChange(TuFaceMonsterMode mode){
        if (mController != null)
            mController.changeMonster(mode);

        for (TextView view : monsterBtns){
            if (view.getTag() == mode){
                view.setBackgroundColor(Color.argb(126, 255, 255, 255));
                view.setTextColor(Color.BLACK);
            }else{
                view.setBackgroundColor(0);
                view.setTextColor(Color.WHITE);
            }
        }
    }

    // 切换人脸塑性开关
    private void plasticChange(boolean show){
        plasticBtn.setTag(show);
        if (mController != null)
            mParams = mController.changePlastic(show);

        effectParamsView.setFilterParams(mParams);

        if (show){
            plasticBtn.setBackgroundColor(Color.argb(126, 255, 255, 255));
            plasticBtn.setTextColor(Color.BLACK);
        }else{
            plasticBtn.setBackgroundColor(0);
            plasticBtn.setTextColor(Color.WHITE);
        }
    }

    /** 测试方法 */
    private void test(Bitmap image)
    {
        ImageView imageView = new ImageView(this.getActivity());
        imageView.setBackgroundColor(Color.GRAY);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setImageBitmap(image);
        imageView.setOnClickListener(mImageViewClickListener);
        cameraView.addView(imageView, new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
    }

    private View.OnClickListener mImageViewClickListener = new TuSdkViewHelper.OnSafeClickListener()
    {
        @Override
        public void onSafeClick(View v)
        {
            v.setOnClickListener(null);
            cameraView.removeView(v);
            resumeCameraCapture();
        }
    };
}
