package com.mx.ui.activity;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;
import android.widget.ImageView;

import com.hannesdorfmann.swipeback.Position;
import com.hannesdorfmann.swipeback.SwipeBack;
import com.mx.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ZhihuStoryActivity extends AppCompatActivity {

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
        type=getIntent().getIntExtra("type",0);
        id=getIntent().getStringExtra("id");
        title=getIntent().getStringExtra("title");
        //

    }

    private void initView() {


    }

    private void getData() {

    }
}
