package com.mx.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.hannesdorfmann.swipeback.Position;
import com.hannesdorfmann.swipeback.SwipeBack;
import com.mx.R;
import com.mx.iView.IItHomeArticleView;
import com.mx.model.ithome.ItHomeArticle;
import com.mx.model.ithome.ItHomeItem;
import com.mx.presenter.ItHomeArticlePresenter;
import com.mx.presenter.impl.ItHomeArticlePresenterImpl;
import com.mx.utils.WebUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ItHomeActivity extends BaseActivity implements IItHomeArticleView {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.pb_web)
    ProgressBar mProgressBar;
    @BindView(R.id.wv_it)
    WebView mWebView;
    @BindView(R.id.nest)
    NestedScrollView mNest;
    @BindView(R.id.fabButton)
    FloatingActionButton mFabButton;

    private ItHomeArticlePresenter mItHomeArticlePresenter;
    private ArrayList<ItHomeArticle> mItHomeArticles = new ArrayList<>();
    private ItHomeItem mItHomeItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SwipeBack.attach(this, Position.LEFT)
                .setContentView(R.layout.activity_it_home)
                .setSwipeBackView(R.layout.swipe_back);
        ButterKnife.bind(this);
        initData();
        initViw();
        getData();
    }

    private void initData() {
        mItHomeItem = getIntent().getParcelableExtra("item");
        mItHomeArticlePresenter = new ItHomeArticlePresenterImpl(this);

    }

    private void initViw() {
        mToolbar.setTitle(mItHomeItem.getTitle());
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        int vibrantColor = setTool(mFabButton, mToolbar, true, true, null);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.swipe_back);
        if (linearLayout != null) {
            linearLayout.setBackgroundColor(vibrantColor);
        }

        mFabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNest.smoothScrollTo(0, 0);
            }
        });
        setWebView();

    }

    private void setWebView() {
        WebSettings settings = mWebView.getSettings();
        settings.setDomStorageEnabled(true);
        settings.setAppCacheEnabled(true);
        settings.setJavaScriptEnabled(true);
        settings.setPluginState(WebSettings.PluginState.ON);
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setAppCachePath(getCacheDir().getAbsolutePath() + "/webViewCache");
        settings.setAppCacheEnabled(true);
        settings.setLoadWithOverviewMode(true);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (mProgressBar != null) {//修复未加载完成，用户返回崩溃
                    if (newProgress == 100) {
                        mProgressBar.setVisibility(View.GONE);
                    } else {
                        if (mProgressBar.getVisibility() == View.GONE) {
                            mProgressBar.setVisibility(View.VISIBLE);
                        }
                    }
                    mProgressBar.setProgress(newProgress);

                }

                super.onProgressChanged(view, newProgress);
            }
        });

    }

    private void getData() {
        mItHomeArticlePresenter.getItHomeArticle(mItHomeItem.getNewsid());

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_share, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, mItHomeItem.getTitle() +
                        "http://ithome.com" + mItHomeItem.getUrl() +
                        getString(R.string.share_tail));
                intent.setType("text/plain");
                //设置分享列表的标题，并且每次都显示分享列表
                startActivity(Intent.createChooser(intent, getString(R.string.share)));
                break;
            case R.id.action_use_browser:
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://ithome.com" + mItHomeItem.getUrl())));
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWebView != null) {
            ViewGroup parent = (ViewGroup) mWebView.getParent();
            parent.removeView(mWebView);
            mWebView.destroy();
            mWebView = null;
        }
        mItHomeArticlePresenter.unSubscribe();
    }

    @Override
    public void showError(String error) {

        Snackbar.make(mWebView, getString(R.string.common_loading_error) + error,
                Snackbar.LENGTH_SHORT)
                .setAction(getString(R.string.comon_retry), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getData();
                    }
                }).show();


    }

    @Override
    public void showItHomeArticle(ItHomeArticle itHomeArticle) {
        if (TextUtils.isEmpty(itHomeArticle.getDetail())) {
            mWebView.loadUrl(mItHomeItem.getUrl());
        } else {
            String data = WebUtils.BuildHtmlWithCss(itHomeArticle.getDetail(), new
                    String[]{"new.css"}, false);
            mWebView.loadDataWithBaseURL(WebUtils.BASE_URL,
                    data,WebUtils.MIME_TYPE,WebUtils.ENCODING,mItHomeItem.getUrl());
        }

    }
}
