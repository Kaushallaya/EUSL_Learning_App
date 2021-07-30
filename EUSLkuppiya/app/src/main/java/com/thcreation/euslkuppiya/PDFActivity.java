package com.thcreation.euslkuppiya;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PDFActivity extends AppCompatActivity {

    String url;
    WebView webView;

    private ProgressBar progressBar;
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);

        Intent in = getIntent();
        Bundle bundle = in.getExtras();
        url = bundle.getString("url");

        Toast.makeText(this, "url = "+url, Toast.LENGTH_SHORT).show();
        System.out.println("URL = "+url);

        webView = findViewById(R.id.WV);
        progressBar = findViewById(R.id.pb);
        progressBar.setVisibility(View.VISIBLE);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.setWebChromeClient(new WebChromeClient());
        Intent intent = getIntent();
        webView.setWebViewClient(new WebViewClient(){

            @Override
            public void onPageFinished(WebView view, String url) {
                webView.loadUrl("javascript:(function() { " +
                        "document.querySelector('[role=\"toolbar\"]').remove();})()");
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(url);
                return true;
            }
        });
        //https://docs.google.com/viewerng/viewer?embedded=true&url=
        //webView.loadUrl("https://docs.google.com/gview?embedded=true&url="+MainActivity.list.get(position).getPdfUrl());
        //String urlAdd = "https://docs.google.com/gview?embedded=true&url=https://drive.google.com/file/d/1dgOc444RMw0LCG-f_rxuGbXp-vr3Fmtc/view";

        webView.loadUrl("https://drive.google.com/viewerng/viewer?embedded=true&url=https://drive.google.com/uc?id="+url+"");




    }
}