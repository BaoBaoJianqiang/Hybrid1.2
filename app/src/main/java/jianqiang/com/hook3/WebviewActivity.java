package jianqiang.com.hook3;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class WebviewActivity extends Activity {

    private static final String TAG = "WebviewActivity";
    WebView wv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        String fullURL = getIntent().getStringExtra("FullURL");

        wv = (WebView) findViewById(R.id.wv);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setBuiltInZoomControls(false);
        wv.getSettings().setSupportZoom(true);
        wv.getSettings().setUseWideViewPort(true);
        wv.getSettings().setLoadWithOverviewMode(true);
        wv.getSettings().setSupportMultipleWindows(true);

        wv.getSettings().setTextZoom(100);

        wv.setWebViewClient(new MyWebChromeClient());
        wv.loadUrl(fullURL);
    }

    public class MyWebChromeClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            Uri url = request.getUrl();

            if(url == null) {
                return super.shouldOverrideUrlLoading(view, request);
            }

            Intent intent = null;
            if (url.toString().toLowerCase().startsWith("activity://")) {
                intent = parseUrl(url.toString(), "activity://");
                startActivity(intent);
            } else if(url.toString().toLowerCase().startsWith("startactivityforresult://")) {
                intent = parseUrl(url.toString(), "startactivityforresult://");
                setResult(2, intent);
                finish();
            } else {
                return super.shouldOverrideUrlLoading(view, request);
            }

            return true;
        }
    }

    Intent parseUrl(String url, String prefix) {
        int pos = url.indexOf("?");
        String activity = url.substring(prefix.length(), pos);

        //6 means ?json=
        String jsonEncodeData = url.substring(pos + 6);

        String jsonData = null;
        try {
            jsonData = URLDecoder.decode(jsonEncodeData, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jsonData);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONArray jsonType = jsonObject.optJSONArray("jsonType");
        JSONObject jsonValue = jsonObject.optJSONObject("jsonValue");

        Intent intent = new Intent();

        for (int i = 0; i < jsonType.length(); i++) {
            JSONObject item = jsonType.optJSONObject(i);
            String key = item.optString("key");
            String value = item.optString("value");

            switch (value) {
                case "1":
                    String strData = jsonValue.optString(key);
                    intent.putExtra(key, strData);
                    break;
                case "2":
                    int intData = jsonValue.optInt(key);
                    intent.putExtra(key, intData);
                    break;
                default:
                    JSONArray arrayData = jsonValue.optJSONArray(key);
                    Gson gson = new Gson();
                    ArrayList arrayList = new ArrayList();

                    try {
                        for (int j = 0; j < arrayData.length(); j++) {
                            Object data = gson.fromJson(arrayData.optJSONObject(j).toString(), Class.forName(value));
                            arrayList.add(data);
                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }

                    intent.putExtra(key, arrayList);

                    break;
            }
        }

        ComponentName componentName = new ComponentName(getPackageName(), activity);
        intent.setComponent(componentName);

        return intent;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_BACK && wv.canGoBack()) { // 表示按返回键时的操作
                wv.goBack(); // 后退
                // webview.goForward();//前进
                return true; // 已处理
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}