package com.tanat.nodeadstesttask.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import com.tanat.nodeadstesttask.Constants;
import com.tanat.nodeadstesttask.R;
import com.tanat.nodeadstesttask.utils.MyWebViewClient;

public class WebViewActivity extends AppCompatActivity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        setTitle("PDF declaration");

        Intent intent = getIntent();
        webView = (WebView)findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("http://docs.google.com/gview?embedded=true&url=" + intent.getStringExtra(Constants.LINK));
        webView.setWebViewClient(new MyWebViewClient());
    }
}
