package jianqiang.com.hook3;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;

import java.io.File;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import jianqiang.com.hook3.entity.Course;

public class BaseActivity extends Activity {

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        if(intent.getComponent() == null) {
            super.startActivityForResult(intent, requestCode);
            return;
        }

        String originalTargetActivity = intent.getComponent().getClassName();

        PageInfo pageInfo = MyApplication.pages.get(originalTargetActivity);
        if(pageInfo == null) {
            super.startActivityForResult(intent, requestCode);
            return;
        }


        StringBuilder sb2 = new StringBuilder();
        if(pageInfo.getFields()!= null && pageInfo.getFields().size() > 0) {
            sb2.append("{");

            for(String key: pageInfo.getFields().keySet()) {
                int type = pageInfo.getFields().get(key);
                switch (type) {
                    case 1:
                        String v1 = intent.getStringExtra(key);
                        sb2.append("\"" + key + "\"");
                        sb2.append(":");
                        sb2.append("\"" + v1 + "\"");
                        sb2.append(",");
                        break;
                    case 2:
                        int v2 = intent.getIntExtra(key, 0);
                        sb2.append("\"" + key + "\"");
                        sb2.append(":");
                        sb2.append(String.valueOf(v2));
                        sb2.append(",");
                        break;
                    case 3:
                        Serializable v3 = intent.getSerializableExtra(key);
                        Gson gson = new Gson();
                        String strJSON = gson.toJson(v3);
                        sb2.append("\"" + key + "\"");
                        sb2.append(":");
                        sb2.append(strJSON);
                        sb2.append(",");
                        break;
                    default:
                        break;
                }
            }

            sb2.deleteCharAt(sb2.length() - 1);
            sb2.append("}");
        }

        StringBuilder sb = new StringBuilder();
        sb.append(pageInfo.getUri());
        if(pageInfo.getFields()!= null && pageInfo.getFields().size() > 0) {
            sb.append("?json=");
            String str = null;
            try {
                str = URLEncoder.encode(sb2.toString(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            sb.append(str);
        }

        Intent newIntent = new Intent();
        newIntent.putExtra("FullURL", sb.toString());

        // 替身Activity的包名, 也就是我们自己的包名
        String stubPackage = MyApplication.getContext().getPackageName();

        // 这里我们把启动的Activity临时替换为 WebviewActivity
        ComponentName componentName = new ComponentName(stubPackage, WebviewActivity.class.getName());
        newIntent.setComponent(componentName);

        super.startActivityForResult(newIntent, requestCode);
    }
}
