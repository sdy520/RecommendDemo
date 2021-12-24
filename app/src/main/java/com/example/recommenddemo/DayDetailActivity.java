package com.example.recommenddemo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.recommenddemo.base.BaseActivity;
import com.example.recommenddemo.databinding.ActivityDayDetailBinding;

public class DayDetailActivity extends BaseActivity {
    ActivityDayDetailBinding detailBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        detailBinding = ActivityDayDetailBinding.inflate(getLayoutInflater());
        setContentView(detailBinding.getRoot());
        Intent intent = getIntent();
        String rootUrl = intent.getStringExtra("url");
        detailBinding.webView.loadUrl(rootUrl);
        //系统默认会通过手机浏览器打开网页，为了能够直接通过WebView显示网页，则必须设置
        //设置WebViewClient
        detailBinding.webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                detailBinding.webView.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                detailBinding.pbLoading.setVisibility(View.VISIBLE);
                detailBinding.webView.setVisibility(View.GONE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                detailBinding.pbLoading.setVisibility(View.GONE);
                detailBinding.webView.setVisibility(View.VISIBLE);
            }
        });
        //设置自适应屏幕，两者合用
        detailBinding.webView.getSettings().setUseWideViewPort(true);
        detailBinding.webView.getSettings().setLoadWithOverviewMode(true);
        detailBinding.webView.getSettings().setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        detailBinding.webView.getSettings().setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        detailBinding.webView.getSettings().setDisplayZoomControls(false); //隐藏原生的缩放控件

    }
}
