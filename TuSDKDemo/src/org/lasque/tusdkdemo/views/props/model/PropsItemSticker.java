package org.lasque.tusdkdemo.views.props.model;

import org.lasque.tusdkpulse.modules.view.widget.sticker.StickerGroup;
import org.lasque.tusdkpulse.modules.view.widget.sticker.StickerLocalPackage;

/******************************************************************
 * droid-sdk-video 
 * org.lasque.tusdkdemo.views.props.model
 *
 * @author sprint
 * @Date 2018/12/28 1:41 PM
 * @Copyright (c) 2018 tutucloud.com. All rights reserved.
 ******************************************************************/
/* 贴纸道具 */
public class PropsItemSticker extends PropsItem {

    /** 贴纸对象 */
    private StickerGroup mStickerGrop;

    public PropsItemSticker(StickerGroup stickerGroup) {
        this.mStickerGrop  = stickerGroup;
    }

    public StickerGroup getStickerGrop () {
        return mStickerGrop;
    }

}

