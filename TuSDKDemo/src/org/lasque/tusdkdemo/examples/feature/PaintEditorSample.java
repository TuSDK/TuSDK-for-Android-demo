/**
 * TuSdkDemo
 * PaintEditorSample.java
 *
 * @author H.ys
 * @Date 2019/4/25
 */
package org.lasque.tusdkdemo.examples.feature;

import android.app.Activity;

import org.lasque.tusdk.core.TuSdkResult;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.image.BitmapHelper;
import org.lasque.tusdk.impl.activity.TuFragment;
import org.lasque.tusdk.impl.components.paintdraw.TuEditPaintOption;
import org.lasque.tusdk.modules.components.TuSdkHelperComponent;
import org.lasque.tusdk.modules.view.widget.smudge.BrushSize;
import org.lasque.tusdkdemo.R;
import org.lasque.tusdkdemo.SampleBase;
import org.lasque.tusdk.impl.components.paintdraw.TuEditPaintFragment;
import org.lasque.tusdkdemo.SampleGroup;

import java.util.Arrays;


/**
 * 涂鸦功能示例, 涂鸦功能需要权限验证，使用前请确认已获取相应权限。
 *
 * 更多服务细节请参考: http://tusdk.com/services
 * @since v3.1.0
 * @author H.ys
 */
public class PaintEditorSample extends SampleBase implements TuEditPaintFragment.TuEditPaintFragmentDelegate {

    /**
     * 默认涂鸦画笔颜色
     */
    private String[] mBrushGroup = new String[]{"#123456", "#654321", "#ff1d34", "#fbf234", "#14ee22", "#19b2d1", "#8c67a2","#76d3a0","#12c90a"};

    /**
     * 范例分组头部信息
     */
    public PaintEditorSample() {
        super(SampleGroup.GroupType.FeatureSample, R.string.sample_comp_PaintComponent);
    }

    /**
     * 显示视图
     *
     * @param activity
     */
    @Override
    public void showSample(Activity activity) {
        if (activity == null) return;
        // see-http://tusdk.com/docs/android/image/api/org/lasque/tusdk/impl/components/base/TuSdkHelperComponent.html
        this.componentHelper = new TuSdkHelperComponent(activity);
        // 组件选项配置
        // @see-http://tusdk.com/docs/android/image/api/org/lasque/tusdk/impl/components/filter/TuEditWipeAndFilterOption.html
        TuEditPaintOption option = new TuEditPaintOption();
        // 是否在控制器结束后自动删除临时文件
        option.setAutoRemoveTemp(true);
        //设置笔刷缩放效果(默认3.0f,范围1.0f-3.0f)
        option.setBrushScale(3);
        // 设置默认的笔刷大小 (默认: BrushSize.SizeType.MediumBrush，中等粗细)
        option.setDefaultBrushSize(BrushSize.SizeType.MediumBrush);
        // 是否显示处理结果预览图 (默认：关闭，调试时可以开启)
        option.setShowResultPreview(true);

        // 允许撤销的次数 (默认: 5)
        // option.setMaxUndoCount(5);
        //设置画笔颜色列表 传入格式错误将使用默认颜色列表
        option.setBrushGroup(Arrays.asList(mBrushGroup));
        //设置最短绘制距离(默认10f,范围10f-20f)
        option.setMinDistance(10f);
        //设置是否记录用户最后使用的笔刷
        option.setSaveLastBrush(true);

        TuEditPaintFragment fragment = option.fragment();
        // 输入的图片对象 (处理优先级: Image > TempFilePath > ImageSqlInfo)
        fragment.setImage(BitmapHelper.getRawBitmap(activity, R.raw.sample_photo));

        fragment.setDelegate(this);
        // 开启涂鸦界面
        componentHelper.pushModalNavigationActivity(fragment, true);
    }

    /**
     * @param fragment 图片编辑涂鸦控制器
     * @param result   处理结果
     */
    @Override
    public void onTuEditPaintFragmentEdited(TuEditPaintFragment fragment, TuSdkResult result) {
        fragment.hubDismissRightNow();
        fragment.dismissActivityWithAnim();
        TLog.d("onTuEditPaintFragmentEdited: %s", result);

        // 默认输出为 Bitmap  -> result.image

        // 如果保存到临时文件 (默认不保存, 当设置为true时, TuSdkResult.imageFile, 处理完成后将自动清理原始图片)
        // option.setSaveToTemp(true);  ->  result.imageFile

        // 保存到系统相册 (默认不保存, 当设置为true时, TuSdkResult.sqlInfo, 处理完成后将自动清理原始图片)
        // option.setSaveToAlbum(true);  -> result.image
    }

    /**
     * @param fragment 图片编辑涂鸦控制器
     * @param result   处理结果
     * @return 是否截断默认处理逻辑 (默认: false, 设置为true时使用自定义处理逻辑)
     */
    @Override
    public boolean onTuEditPaintFragmentEditedAsync(TuEditPaintFragment fragment, TuSdkResult result) {
        TLog.d("onTuEditPaintFragmentEditedAsync: %s", result);
        return false;
    }

    /**
     * @param fragment 图片编辑涂鸦控制器
     * @param result   返回结果
     * @param error
     */
    @Override
    public void onComponentError(TuFragment fragment, TuSdkResult result, Error error) {
        TLog.d("onComponentError: fragment - %s, result - %s, error - %s", fragment, result, error);
    }
}
