package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class Main2Activity extends AppCompatActivity {
private WebView wv;
private ProgressBar bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        getSupportActionBar().setTitle("NITA Access");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        bar=findViewById(R.id.progress_bar);
        bar.setVisibility(View.VISIBLE);
        bar.setMax(100);
        wv=(WebView)findViewById(R.id.webviwe);
        wv.setWebViewClient(new MyBrowser());
        wv.getSettings().getLoadsImagesAutomatically();
        wv.getSettings().setJavaScriptEnabled(true);


        wv.loadUrl("https://mis.nita.ac.in/mis/iitmsv4eGq0RuNHb0G5WbhLmTKLmTO7YBcJ4RHuXxCNPvuIw=?enc=g4R9l5ijEYlvf3Kt0STjKQ==");

        wv.setWebChromeClient(new WebChromeClient(){
            /*public void onProgressChanged (WebView view, int newProgress)           Tell the host application the current progress of loading a page.

                Parameters
                    view WebView: The WebView that initiated the callback.

                    newProgress int: Current page loading progress, represented by an
                        integer between 0 and 100.
            */
            public void onProgressChanged(WebView view, int newProgress){
                bar.setVisibility(View.VISIBLE);
                bar.setProgress(newProgress);
                if(newProgress == 100){
                    // Page loading finish
                    //Toast.makeText(getActivity().getApplicationContext(),"Page loaded",Toast.LENGTH_SHORT).show();
                    bar.setVisibility(View.INVISIBLE);
                }
            }
        });





    }
    private class MyBrowser extends WebViewClient
    {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        if(wv.canGoBack()){
            wv.goBack();
        }
        else
            super.onBackPressed();
    }
}
