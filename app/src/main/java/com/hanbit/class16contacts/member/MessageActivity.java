package com.hanbit.class16contacts.member;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.hanbit.class16contacts.R;

public class MessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        Context context=MessageActivity.this;
        WebView webView= (WebView) findViewById(R.id.webView);
        WebSettings settings=webView.getSettings();
        settings.setUseWideViewPort(true);
        settings.setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.addJavascriptInterface(new JavascriptInterface(context),"hybrid");
        webView.loadUrl("file:///android_asset/www/index.html");
    }
    class JavascriptInterface{
        Context context;

        public JavascriptInterface(Context context) {
            this.context = context;
        }
        @android.webkit.JavascriptInterface
        public void showToast(String message){

        }
    }
}
