package com.cloud.service.utils.info;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.annotation.Nullable;

import com.cloud.annotations.CloudService;
import com.cloud.api.context.ContextManager;
import com.cloud.api.error.CloudApiError;
import com.cloud.api.services.CloudUtilsShareService;
import com.cloud.api.tag.CloudServiceTagUtils;
import com.cloud.api.utils.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: cangHX
 * on 2020/06/11  12:42
 */
@CloudService(serviceTag = CloudServiceTagUtils.UTILS_SHARE)
public class CloudUtilsShareServiceImpl implements CloudUtilsShareService {

    /**
     * 打开系统分享，文字
     *
     * @param info : 分享文字内容
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-11 10:14
     */
    @Override
    public void openSystemShareTxt(@Nullable String info) {
        Context context = ContextManager.getCurrentActivity();
        if (context == null) {
            Logger.Error(CloudApiError.NO_INIT.build());
            return;
        }
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, info);
        context.startActivity(Intent.createChooser(intent, "share"));
    }

    /**
     * 打开系统分享，图片
     *
     * @param imgPaths : 图片地址集合
     * @version: 1.0
     * @author: cangHX
     * @date: 2020-06-11 10:15
     */
    @Override
    public void shareAppImg(@Nullable List<String> imgPaths) {
        Context context = ContextManager.getCurrentActivity();
        if (context == null) {
            Logger.Error(CloudApiError.NO_INIT.build());
            return;
        }
        ArrayList<Uri> imageUris = new ArrayList<>();
        if (imgPaths != null) {
            for (String path : imgPaths) {
                try {
                    Uri imageUri = Uri.fromFile(new File(path));
                    imageUris.add(imageUri);
                } catch (Throwable throwable) {
                    Logger.Debug(CloudApiError.DATA_ERROR.append("the img is error on " + path).build());
                    Logger.Debug(throwable.getMessage());
                }
            }
        }
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND_MULTIPLE);
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
        intent.setType("image/*");
        context.startActivity(Intent.createChooser(intent, "share"));
    }
}