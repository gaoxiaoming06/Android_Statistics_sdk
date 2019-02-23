package com.tiger.tigerstatisticssdk.util;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.UUID;

/**
 * tkid的生成与存储
 * Created by gao on 2017/6/2.
 */

public class TKIDUtil {

    private static String tkid;

    public static String getTkid(Context context){
        if(TextUtils.isEmpty(tkid)){
            tkid = readExistFile(context);
            tkid = PreferenceForeverUtil.getString(context,PreferenceForeverUtil.KEY_TKID,null);
        }
        return TextUtils.isEmpty(tkid) ? buildTKID(context) : tkid;
    }

    public static String buildTKID(Context context){
        if(TextUtils.isEmpty(PreferenceForeverUtil.getString(context,PreferenceForeverUtil.KEY_TKID,null))
                && TextUtils.isEmpty(readExistFile(context))){
            tkid = UUID.randomUUID().toString();
        }else {
            if(!TextUtils.isEmpty(readExistFile(context))){
                tkid = readExistFile(context);
            }
            if(!TextUtils.isEmpty(PreferenceForeverUtil.getString(context,PreferenceForeverUtil.KEY_TKID,null))){
                tkid = PreferenceForeverUtil.getString(context,PreferenceForeverUtil.KEY_TKID,null);
            }
        }
        if(TextUtils.isEmpty(PreferenceForeverUtil.getString(context,PreferenceForeverUtil.KEY_TKID,null))){
            PreferenceForeverUtil.putString(context, PreferenceForeverUtil.KEY_TKID, tkid);
        }
        if(TextUtils.isEmpty(readExistFile(context))){
            writeFile(context, tkid);
        }
        return tkid;
    }

    private static String readExistFile(Context mContext) {
        String result = "";
        if ((ActivityCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                &&(ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {

            File mFile = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + "tkid");
            //若该文件存在
            if (mFile.exists()) {
                try {
                    FileInputStream is = new FileInputStream(mFile);
                    byte[] b = new byte[is.available()];
                    is.read(b);
                    result = new String(b);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return result;
    }

    private static void writeFile(Context mContext, String str){
        if ((ActivityCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                &&(ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)){
            try {
                File file = new File(Environment.getExternalStorageDirectory(),
                        "tkid");
                FileOutputStream fos = new FileOutputStream(file);
                String info = str;
                fos.write(info.getBytes());
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
