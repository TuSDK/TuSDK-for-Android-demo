package org.lasque.tusdkdemo.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import org.lasque.tusdkpulse.core.TuSdkContext;
import org.lasque.tusdkpulse.core.seles.SelesParameters.FilterArg;
import org.lasque.tusdkpulse.core.view.TuSdkRelativeLayout;
import org.lasque.tusdkpulse.core.view.TuSdkViewHelper;
import org.lasque.tusdkpulse.core.view.TuSdkViewHelper.OnSafeClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xujie
 * @Date 2018/12/20
 */

public class ParamsConfigView extends TuSdkRelativeLayout {



    /**
     * 滤镜配置视图委托
     *
     * @author Clear
     */
    public interface FilterConfigViewDelegate
    {
        /**
         * 通知重新绘制
         *
         * @param configView
         */
        void onFilterConfigRequestRender(ParamsConfigView configView);
    }

    /**
     * 滤镜配置视图委托
     */
    private FilterConfigViewDelegate mDelegate;

    /**
     * 滤镜配置视图委托
     *
     * @return the mDelegate
     */
    public FilterConfigViewDelegate getDelegate()
    {
        return mDelegate;
    }

    /**
     * 滤镜配置视图委托
     *
     * @param mDelegate
     *            the mDelegate to set
     */
    public void setDelegate(FilterConfigViewDelegate mDelegate)
    {
        this.mDelegate = mDelegate;
    }

    public ParamsConfigView(Context context)
    {
        super(context);
    }

    public ParamsConfigView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public ParamsConfigView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    // 配置包装
    private LinearLayout mConfigWrap;

    /**
     * 配置包装
     *
     * @return
     */
    public LinearLayout getConfigWrap()
    {
        if (mConfigWrap == null)
        {
            mConfigWrap = this.getViewById("lsq_configWrap");
        }
        return mConfigWrap;
    }

    /**
     * 设置拖动条高度
     *
     * @param seekbarHeight
     */
    public void setSeekBarHeight(int seekbarHeight)
    {
        this.mSeekHeigth = seekbarHeight;
    }

    /**
     * 滤镜配置拖动栏列表
     */
    private ArrayList<FilterConfigSeekbar> mSeekbars;

    /**
     * 拖动条高度
     */
    private int mSeekHeigth = TuSdkContext.dip2px(32);

    /**
     * 创建并添加滤镜配置拖动栏
     *
     * @param parent
     *            父视图
     * @return
     */
    public FilterConfigSeekbar buildAppendSeekbar(LinearLayout parent, int height)
    {
        if (parent == null) return null;

        FilterConfigSeekbar seekbar = TuSdkViewHelper.buildView(this.getContext(), FilterConfigSeekbar.getLayoutId(), parent);
        seekbar.setPrefix(getPrefix());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, height);
        parent.addView(seekbar, params);
        return seekbar;
    }
    private String mPrefix = "lsq_filter_set_";
    public void setPrefix(String prefix){
        this.mPrefix = prefix;
    }
    public String getPrefix(){
        return mPrefix;
    }

    /**
     * 滤镜配置拖动栏委托
     */
    protected FilterConfigSeekbar.FilterConfigSeekbarDelegate mFilterConfigSeekbarDelegate = new FilterConfigSeekbar.FilterConfigSeekbarDelegate()
    {
        /**
         * 配置数据改变
         *
         * @param seekbar
         *            滤镜配置拖动栏
         * @param arg
         *            滤镜参数
         */
        public void onSeekbarDataChanged(FilterConfigSeekbar seekbar, FilterArg arg)
        {
            if (getSeekBarDelegate() != null) {
                getSeekBarDelegate().onSeekbarDataChanged(seekbar, arg);
            }

            requestRender();
        }
    };

    /**
     * 滤镜配置拖动栏状态委托
     *
     * @author Clear
     */
    public interface FilterConfigViewSeekBarDelegate
    {
        /**
         * 配置数据改变
         *
         * @param seekbar
         *            滤镜配置拖动栏
         * @param arg
         *            滤镜参数
         */
        public void onSeekbarDataChanged(FilterConfigSeekbar seekbar, FilterArg arg);

    }

    private FilterConfigViewSeekBarDelegate mSeekBarDelegate;

    public void setSeekBarDelegate(FilterConfigViewSeekBarDelegate seekBarDelegate)
    {
        this.mSeekBarDelegate = seekBarDelegate;
    }

    public FilterConfigViewSeekBarDelegate getSeekBarDelegate()
    {
        return this.mSeekBarDelegate;
    }

    /**
     * 按钮点击事件
     */
    protected OnSafeClickListener mOnClickListener = new OnSafeClickListener()
    {
        @Override
        public void onSafeClick(View v)
        {
        }
    };

    /**
     * 设置滤镜配置选线显示状态
     */
    protected void handleShowStateAction()
    {
    }

    /**
     * 请求渲染
     */
    protected void requestRender()
    {
        if (mDelegate != null)
        {
            mDelegate.onFilterConfigRequestRender(this);
        }
    }

}
