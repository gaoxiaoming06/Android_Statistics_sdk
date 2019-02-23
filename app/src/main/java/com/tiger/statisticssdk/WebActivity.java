package com.tiger.statisticssdk;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.tiger.tigerstatisticssdk.TkAgent;

import java.lang.reflect.Field;

public class WebActivity extends AppCompatActivity {

    String title = "";
    private WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setConfigCallback((WindowManager)getApplicationContext().getSystemService(Context.WINDOW_SERVICE));
        setContentView(R.layout.activity_web);

        webview = (WebView) findViewById(R.id.webviwe);
        webview.loadUrl("https://www.baidu.com/");

        webview.setWebChromeClient(new TTWebChromeClient());
        webview.setWebViewClient(new TTWebViewClient());
    }

    public class TTWebChromeClient extends WebChromeClient {

        @Override
        public void onReceivedTitle(WebView view, String title) {
            WebActivity.this.title = title;
            super.onReceivedTitle(view, title);
        }

    }

    class TTWebViewClient extends WebViewClient {

        @Override
        public void onPageFinished(WebView view, String url) {
            TkAgent.onWebComplete(WebActivity.this,url,title);
            TkAgent.onWebComplete(WebActivity.this,url,title);
            super.onPageFinished(view, url);
        }

    }

    @Override
    protected void onDestroy() {
        setConfigCallback(null);
        super.onDestroy();

        if (webview != null) {
            ViewParent parent = webview.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(webview);
            }
            webview.stopLoading();
            // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
            webview.getSettings().setJavaScriptEnabled(false);
            webview.clearHistory();
            webview.clearView();
            webview.removeAllViews();

            try {
                webview.destroy();
            } catch (Throwable ex) {

            }
            webview = null;
        }
    }

    public void setConfigCallback(WindowManager windowManager) {
        try {
            Field field = WebView.class.getDeclaredField("mWebViewCore");
            field = field.getType().getDeclaredField("mBrowserFrame");
            field = field.getType().getDeclaredField("sConfigCallback");
            field.setAccessible(true);
            Object configCallback = field.get(null);

            if (null == configCallback) {
                return;
            }

            field = field.getType().getDeclaredField("mWindowManager");
            field.setAccessible(true);
            field.set(configCallback, windowManager);
        } catch(Exception e) {
        }
    }
}
