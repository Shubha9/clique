package com.us.clique.activites;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.ClientCertRequest;
import android.webkit.DownloadListener;
import android.webkit.HttpAuthHandler;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import com.us.clique.R;
import com.us.clique.bottomNavigation.fragments.module.BottomNavigationFactory;
import com.us.clique.bottomNavigation.fragments.module.BottomNavigationViewModel;
import com.us.clique.databinding.ActivityBrowserBinding;
import com.us.clique.networkUtils.Constants;

import javax.inject.Inject;
import dagger.android.AndroidInjection;

public class BrowserActivity extends BaseActivity {
    BottomNavigationViewModel bottomNavigationViewModel;
    @Inject
    BottomNavigationFactory bottomNavigationFactory;
    ActivityBrowserBinding binding;
    Context context;
    String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(BrowserActivity.this, R.layout.activity_browser);
        AndroidInjection.inject(this);
        bottomNavigationViewModel = new ViewModelProvider(this, bottomNavigationFactory).get(BottomNavigationViewModel.class);
        binding.setWebView(bottomNavigationViewModel);
        binding.imageBack.setOnClickListener(this::onClick);
        binding.fab.setOnClickListener(this::onClick);
        if (getIntent().getExtras() != null) {

        } setUpWebView(getIntent().getExtras().getString(Constants.TITLE), getIntent().getExtras().getString(Constants.URL));


    }

    private void setUpWebView(String title, String url) {
        binding.textTitle.setText(title);
        title.toUpperCase();
        binding.progressBar.setVisibility(View.VISIBLE);
        if (getString(R.string.terms_of_services_and_privacy_policy).toUpperCase().equalsIgnoreCase(title)) {

        }


        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.getSettings().setLoadWithOverviewMode(true);
        binding.webView.getSettings().setUseWideViewPort(true);
        binding.webView.getSettings().setAllowFileAccess(true);
        binding.webView.getSettings().setAllowFileAccessFromFileURLs(true);
        binding.webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        binding.webView.getSettings().setAllowContentAccess(true);
        binding.webView.getSettings().setDomStorageEnabled(true);
        binding.webView.getSettings().setAppCacheEnabled(true);
        binding.webView.setVerticalScrollBarEnabled(true);
        binding.webView.setHorizontalScrollBarEnabled(true);
        // webView.getSettings().se


        binding.webView.setClickable(true);
        binding.webView.loadUrl(url);
        binding.webView.setWebChromeClient(new WebChromeClient());
        binding.webView.setOnScrollChangedCallback(new NestedWebView.OnScrollChangedCallback() {


            @Override
            public void onScrollChange(WebView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > oldScrollY && scrollY > 0) {
                    binding.fab.show();

                }
                if (scrollY < oldScrollY) {
                    binding.fab.hide();

                }
            }
        });

        binding.webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (!(BrowserActivity.this).isFinishing()) {
                    //show dialog
                    binding.progressBar.setVisibility(View.GONE);
                    Log.v("WebView Error Code", "" + "onReceivedError");
                }

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                binding.progressBar.setVisibility(View.GONE);
                //   wv_drive_with_spin.loadUrl("javascript:(function() { " + "document.getElementById('dd-url').style.display = 'none'; document.getElementById('desktop-url').style.display = 'none'; " + "})()");
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                binding.progressBar.setVisibility(View.GONE);
                //showAlertDialog(BrowserActivity.this, error.toString());

            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                super.onReceivedSslError(view, handler, error);
                handler.proceed();
                binding. progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onReceivedClientCertRequest(WebView view, ClientCertRequest request) {
                super.onReceivedClientCertRequest(view, request);
                binding.progressBar.setVisibility(View.GONE);
            }

          /*  @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
            }*/


            @Override
            public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
                super.onReceivedHttpAuthRequest(view, handler, host, realm);
                binding.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                super.onReceivedHttpError(view, request, errorResponse);
                binding.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onReceivedLoginRequest(WebView view, String realm, String account, String args) {
                super.onReceivedLoginRequest(view, realm, account, args);
                binding.progressBar.setVisibility(View.GONE);
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {

                if (url.toLowerCase().contains("/favicon.ico")) {
                    try {
                        return new WebResourceResponse("image/png", null, null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                return null;
            }

            @SuppressLint("NewApi")
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                if (!request.isForMainFrame() && request.getUrl().getPath().endsWith("/favicon.ico")) {
                    try {
                        return new WebResourceResponse("image/png", null, null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                return null;
            }


            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                Log.v("WebView Error Code", "" + errorCode);
                Log.v("failingUrl", "" + failingUrl);

                Log.v("Error description", "" + description);
            }

          /*  @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }*/

       /*     @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.endsWith(".pdf")||url.endsWith(".aspx")){

                    String pdfUrl = googleDocs + url;

                    view.loadUrl(pdfUrl);
                } else {

                    view.loadUrl(url);
                }
                return true;
            }*/


            @SuppressWarnings("deprecation")
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String url) {
                return shouldOverrideUrlLoading(url);
            }

            @TargetApi(Build.VERSION_CODES.N)
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, WebResourceRequest request) {
                Uri uri = request.getUrl();
                return shouldOverrideUrlLoading(uri.toString());
            }

            private boolean shouldOverrideUrlLoading(final String url) {


                binding.webView.loadUrl(url);
                // Here put your code

                return true; // Returning True means that application wants to leave the current WebView and handle the url itself, otherwise return false.
            }

        });


        binding.webView.setDownloadListener(new DownloadListener() {
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimetype,
                                        long contentLength) {

                //Get the URL entered


            }
        });
    }
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.image_back:
                onBackPressed();
                break;
            case R.id.fab:
                binding.webView.scrollTo(0,0);
        }
    }

}