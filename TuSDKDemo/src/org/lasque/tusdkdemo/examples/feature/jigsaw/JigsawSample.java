package org.lasque.tusdkdemo.examples.feature.jigsaw;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.tusdk.pulse.utils.AssetsMapper;

import org.lasque.tusdkdemo.R;
import org.lasque.tusdkdemo.SampleBase;
import org.lasque.tusdkdemo.SampleGroup;
import org.lasque.tusdkpulse.core.TuSdkContext;
import org.lasque.tusdkpulse.core.utils.TLog;
import org.lasque.tusdkpulse.modules.components.TuSdkHelperComponent;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * TuSDK
 * org.lasque.tusdkdemo.examples.feature.jigsaw
 * android-fp-demo
 *
 * @author H.ys
 * @Date 2021/7/7  14:27
 * @Copyright (c) 2020 tusdk.com. All rights reserved.
 */
public class JigsawSample extends SampleBase {


    /**
     * 范例分组头部信息
     *
     * @param groupId    分组ID
     * @param titleResId
     */
    public JigsawSample() {
        super(SampleGroup.GroupType.FeatureSample, R.string.sample_comp_JigsawComponent);
    }

    @Override
    public void showSample(Activity activity) {
        if (activity == null) return;
        mapZipFile("jigsaw_ex","jigsaw_ex.zip");
        mapZipFile("jigsaw_ex2","jigsaw_ex2.zip");
        mapZipFile("jigsaw_ex3","jigsaw_ex3.zip");

        this.componentHelper = new TuSdkHelperComponent(activity);

        JigsawFragment fragment = new JigsawFragment();
        componentHelper.pushModalNavigationActivity(fragment,true);
    }

    private void mapZipFile(String key,String fileName) {
        SharedPreferences sp = TuSdkContext.context().getSharedPreferences("TU-TTF", Context.MODE_PRIVATE);
        AssetsMapper assetsMapper = new AssetsMapper(TuSdkContext.context());
        if (!sp.contains(key)) {
            try {
                String zipFilePath = assetsMapper.mapAsset(fileName);
                int index = zipFilePath.lastIndexOf(".");
                String saveDir = zipFilePath.substring(0,index);
                File saveFileDir = new File(saveDir);
                if (!saveFileDir.exists()) saveFileDir.mkdirs();

                ZipEntry entry = null;
                String entryFilePath = null;
                int count = 0, bufferSize = 1024;
                byte[] buffer = new byte[bufferSize];
                BufferedInputStream bis = null;
                BufferedOutputStream bos = null;

                ZipFile zip = new ZipFile(zipFilePath);
                Enumeration<ZipEntry> entries = (Enumeration<ZipEntry>) zip.entries();
                //循环对压缩包里的每一个文件进行解压
                while (entries.hasMoreElements()) {
                    entry = entries.nextElement();
                    /**这里提示如果当前元素是文件夹时，在目录中创建对应文件夹
                     * ，如果是文件，得出路径交给下一步处理*/
                    entryFilePath = saveDir + File.separator + entry.getName();
                    TLog.e("entry file path %s",entryFilePath);
                    File file = new File(entryFilePath);
                    if (entryFilePath.endsWith("/")) {
                        if (!file.exists()) {
                            file.mkdirs();
                        }
                        continue;
                    }
                    /***这里即是上一步所说的下一步，负责文件的写入✧*/
                    bos = new BufferedOutputStream(new FileOutputStream(entryFilePath));
                    bis = new BufferedInputStream(zip.getInputStream(entry));
                    while ((count = bis.read(buffer, 0, bufferSize)) != -1) {
                        bos.write(buffer, 0, count);
                    }
                    bos.flush();
                    bos.close();
                }
                sp.edit().putString(key,saveDir).apply();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                TLog.e(e);
            } catch (IOException e) {
                e.printStackTrace();
                TLog.e(e);
            }
        }
    }
}
