package com.mx.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.hannesdorfmann.swipeback.Position;
import com.hannesdorfmann.swipeback.SwipeBack;
import com.mx.R;
import com.mx.iView.IZhihuStoryView;
import com.mx.model.guokr.GuokrArticle;
import com.mx.model.zhihu.ZhihuStory;
import com.mx.presenter.IZhihuStoryPresenter;
import com.mx.presenter.impl.ZhihuStoryPresenterImpl;
import com.mx.utils.SharePreferenceUtil;
import com.mx.utils.WebUtils;

import java.lang.reflect.InvocationTargetException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ZhihuStoryActivity extends BaseActivity implements IZhihuStoryView {

    public static final int TYPE_ZHIHU = 1;
    public static final int TYPE_GUOKR = 2;
    @BindView(R.id.iv_zhihu_story)
    ImageView mIvZhihuStory;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.ctl)
    CollapsingToolbarLayout mCtl;
    @BindView(R.id.wv_zhihu)
    WebView mWvZhihu;
    @BindView(R.id.nest)
    NestedScrollView mNest;
    @BindView(R.id.fabButton)
    FloatingActionButton mFabButton;

    private IZhihuStoryPresenter mIZhihuStoryPresenter;
    private int type;
    private String id;
    private String title;
    private String url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SwipeBack.attach(this, Position.LEFT)
                .setContentView(R.layout.activity_zhihu_story);
        ButterKnife.bind(this);

        initData();
        initView();
        getData();

    }

    private void initData() {
        type = getIntent().getIntExtra("type", 0);
        id = getIntent().getStringExtra("id");
        title = getIntent().getStringExtra("title");
        //
        mIZhihuStoryPresenter = new ZhihuStoryPresenterImpl(this);

    }

    private void initView() {
        mToolbar.setTitle(title);
        setSupportActionBar(mToolbar);
        boolean isKitKat = Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT;
        int vibrant = setTool(mFabButton, mToolbar,
                false, isKitKat, null);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.swipe_back);
        if (linearLayout != null) {
            linearLayout.setBackgroundColor(vibrant);
        }
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ZhihuStoryActivity.this.finish();
            }
        });
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mFabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNest.smoothScrollTo(0, 0);
            }
        });

        WebSettings settings = mWvZhihu.getSettings();

        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        settings.setLoadWithOverviewMode(true);
        settings.setBuiltInZoomControls(true);
        //settings.setUseWideViewPort(true);//会造成文字太小
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setAppCacheEnabled(true);
        settings.setAppCachePath(getCacheDir().getAbsolutePath() + "/webViewCache");
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        mWvZhihu.setWebChromeClient(new WebChromeClient());
        mCtl.setContentScrimColor(getSharedPreferences(SharePreferenceUtil.SHARED_PREFERENCE_NAME,
                Context.MODE_PRIVATE).getInt(SharePreferenceUtil.VIBRANT,
                ContextCompat.getColor(this, R.color.colorPrimary)));
        mCtl.setStatusBarScrimColor(getSharedPreferences(
                SharePreferenceUtil.SHARED_PREFERENCE_NAME,
                Context.MODE_PRIVATE).getInt(SharePreferenceUtil.VIBRANT, ContextCompat.getColor(
                this, R.color.colorPrimary)));


    }

    private void getData() {
        switch (type) {
            case TYPE_ZHIHU:
                mIZhihuStoryPresenter.getZhihuStory(id);
                break;
            case TYPE_GUOKR:
                mIZhihuStoryPresenter.getGuokrArticle(id);
                break;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_share, menu);
        menu.findItem(R.id.action_share).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.removeItem(R.id.action_use_browser);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, title + " " + url
                        + getString(R.string.share_tail));
                intent.setType("text/plain");
                startActivity(Intent.createChooser(intent, getString(R.string.share)));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            mWvZhihu.getClass().getMethod("onResume")
                    .invoke(mWvZhihu, (Object[]) null);
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
            mWvZhihu.getClass().getMethod("onPause")
                    .invoke(mWvZhihu, (Object[]) null);
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

        if (mWvZhihu != null) {
            ((ViewGroup) mWvZhihu.getParent()).removeView(mWvZhihu);
            mWvZhihu.destroy();
            mWvZhihu = null;
        }
        mIZhihuStoryPresenter.unSubscribe();
        super.onDestroy();
    }

    @Override
    public void showError(String error) {
        Snackbar.make(mWvZhihu,
                getString(R.string.common_loading_error) + error, Snackbar.LENGTH_SHORT)
                .setAction(getString(R.string.comon_retry), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getData();
                    }
                }).show();
    }

    @Override
    public void showZhihuStory(ZhihuStory zhihuStory) {
        Glide.with(ZhihuStoryActivity.this)
                .load(zhihuStory.getImage())
                .into(mIvZhihuStory);
        url = zhihuStory.getShareUrl();
        if (TextUtils.isEmpty(zhihuStory.getBody())) {
            mWvZhihu.loadUrl(zhihuStory.getShareUrl());
        } else {
            String data = WebUtils.BuildHtmlWithCss(zhihuStory.getBody(), zhihuStory.getCss(),
                    false);
            mWvZhihu.loadDataWithBaseURL(WebUtils.BASE_URL,
                    data, WebUtils.MIME_TYPE,
                    WebUtils.ENCODING,
                    WebUtils.FAIL_URL);
        }

    }

    @Override
    public void showGuokrArticle(GuokrArticle guokrArticle) {
        Glide.with(ZhihuStoryActivity.this)
                .load(guokrArticle.getResult().getSmallImage())
                .into(mIvZhihuStory);
        url = guokrArticle.getResult().getUrl();
        if (TextUtils.isEmpty(guokrArticle.getResult().getContent())) {
            mWvZhihu.loadUrl(guokrArticle.getResult().getUrl());
        } else {
            //解决图片显示的问题，视频显示的问题
            String data = WebUtils.BuildHtmlWithCss(
                    guokrArticle.getResult().getContent()
                            .replaceAll("(style.*?\")>", "")
                            .replaceAll("width=\"(.*?)\"", "100%")
                            .replaceAll("height=\"(.*?)\"", "auto"),
                    new String[]{"news.css"}, false);
            mWvZhihu.loadDataWithBaseURL(WebUtils.BASE_URL,
                    data,WebUtils.MIME_TYPE,WebUtils.ENCODING,WebUtils.FAIL_URL);
        }


    }
}
