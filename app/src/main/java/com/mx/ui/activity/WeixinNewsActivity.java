package com.mx.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.hannesdorfmann.swipeback.Position;
import com.hannesdorfmann.swipeback.SwipeBack;
import com.mx.R;
import com.mx.utils.NetWorkUtil;

import java.lang.reflect.InvocationTargetException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WeixinNewsActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.pb_web)
    ProgressBar mPbWeb;
    @BindView(R.id.wv_weixin)
    WebView mWvWeixin;
    @BindView(R.id.nest)
    NestedScrollView mNest;
    @BindView(R.id.fabButton)
    FloatingActionButton mFabButton;
    private String url;
    private String title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SwipeBack.attach(this, Position.LEFT)
                .setContentView(R.layout.activity_weixin_news)
                .setSwipeBackView(R.layout.swipe_back);
        ButterKnife.bind(this);

        url = getIntent().getStringExtra("url");
        title = getIntent().getStringExtra("title");

        mToolbar.setTitle(title);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WeixinNewsActivity.this.onBackPressed();
            }
        });
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mFabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNest.smoothScrollTo(0, 0);
            }
        });

        int vibrantColor = setTool(mFabButton, mToolbar, true, true, null);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.swipe_back);

        if (linearLayout != null) {
            linearLayout.setBackgroundColor(vibrantColor);
        }

        overridePendingTransition(R.anim.zoomin, R.anim.zoomout);

        WebSettings webSettings = mWvWeixin.getSettings();
        if (!NetWorkUtil.isNetWorkAvailable(this))
            webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        else
            webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webSettings.setAppCachePath(
                getCacheDir().getAbsolutePath() + "/webViewCache");
        webSettings.setAllowFileAccess(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        mWvWeixin.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //处理自动跳转到浏览器的问题
                view.loadUrl(url);
                return true;
            }
        });

        mWvWeixin.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (mPbWeb != null) {
                    mPbWeb.setVisibility(View.GONE);
                } else {
                    if (mPbWeb.getVisibility() == View.GONE) {
                        mPbWeb.setVisibility(View.VISIBLE);
                    }
                    mPbWeb.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }

            //显示全屏的按钮
            private View myView = null;
            private CustomViewCallback mCallback = null;


            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {
                if (mCallback != null) {
                    mCallback.onCustomViewHidden();
                    mCallback = null;
                    return;
                }
                ViewGroup parent = (ViewGroup) mWvWeixin.getParent();
                parent.removeView(mWvWeixin);
                parent.addView(view);
                myView = view;
                mCallback = callback;
            }

            @Override
            public void onHideCustomView() {
                if (myView != null) {
                    if (mCallback != null) {
                        mCallback.onCustomViewHidden();
                        mCallback = null;
                    }
                    ViewGroup parent = (ViewGroup) myView.getParent();
                    parent.removeView(myView);
                    parent.addView(mWvWeixin);
                    myView = null;
                }
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                result.confirm();
                return super.onJsAlert(view, url, message, result);
            }
        });
        mWvWeixin.loadUrl(url);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_share,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if((keyCode==KeyEvent.KEYCODE_BACK)&&mWvWeixin.canGoBack()){
            mWvWeixin.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_share:
                Intent intent=new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT,title+" "
                        +url+getString(R.string.share_tail));
                intent.setType("text/plain");
                //设置分享的列表，并且每次都显示分享的列表
                startActivity(Intent.createChooser(intent,getString(R.string.share)));
                break;
            case R.id.action_use_browser:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            mWvWeixin.getClass().getMethod("onResume")
                    .invoke(mWvWeixin,(Object[]) null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            mWvWeixin.getClass().getMethod("onPause")
                    .invoke(mWvWeixin,(Object[])null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWvWeixin != null) {
            ViewGroup parent = (ViewGroup) mWvWeixin.getParent();
            parent.removeView(mWvWeixin);
            mWvWeixin.destroy();
            mWvWeixin=null;
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.zoomin,R.anim.zoomout);
    }

}
