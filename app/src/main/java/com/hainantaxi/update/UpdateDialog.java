package com.hainantaxi.update;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.text.Html;

import com.hainantaxi.R;
import com.hainantaxi.utils.PLLog;


public class UpdateDialog {


    public static void show(final Context context, String content, final String downloadUrl) {
        if (isContextValid(context)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(R.string.android_auto_update_dialog_title);
            builder.setMessage(Html.fromHtml(content))
                    .setPositiveButton(R.string.android_auto_update_dialog_btn_download, (dialog, id) -> {
                        goToDownload(context, downloadUrl);
                    })
                    .setNegativeButton(R.string.android_auto_update_dialog_btn_cancel, (dialog, id) -> {
                    });

            AlertDialog dialog = builder.create();
            //点击对话框外面,对话框不消失
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        } else {
            PLLog.e("===", "Context 不合法");
        }
    }

    private static boolean isContextValid(Context context) {
        return context instanceof Activity && !((Activity) context).isFinishing();
    }


    private static void goToDownload(Context context, String downloadUrl) {
        PLLog.e("===", "点击下载");
        Intent intent = new Intent(context.getApplicationContext(), DownloadService.class);
        intent.putExtra(DownloadService.APK_DOWNLOAD_URL, downloadUrl);
        context.startService(intent);
    }
}
