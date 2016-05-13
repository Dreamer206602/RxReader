package com.mx.ui.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.hannesdorfmann.swipeback.Position;
import com.hannesdorfmann.swipeback.SwipeBack;
import com.mx.R;
import com.mx.config.Config;
import com.mx.event.StatusEvent;
import com.mx.iView.IChangeChannelView;
import com.mx.presenter.IChangeChannelPresenter;
import com.mx.presenter.impl.ChangeChannelPresenterImpl;
import com.mx.ui.adapter.ChannelAdapter;
import com.mx.ui.helper.ItemDragHelperCallback;
import com.mx.utils.RxBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChangeChannelActivity extends BaseActivity implements IChangeChannelView {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.rv_channel)
    RecyclerView mRecyclerView;
    private ArrayList<Config.Channel> savedChannels = new ArrayList<>();
    private ArrayList<Config.Channel> otherChannels = new ArrayList<>();
    private IChangeChannelPresenter mIChangeChannelPresenter;
    private ChannelAdapter mChannelAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SwipeBack.attach(this, Position.LEFT)
                .setContentView(R.layout.activity_change_channel)
                .setSwipeBackView(R.layout.swipe_back);
        ButterKnife.bind(this);

        initData();
        initView();
    }

    private void initData() {
        mIChangeChannelPresenter = new ChangeChannelPresenterImpl(this, ChangeChannelActivity.this);
    }

    private void initView() {

        mToolbar.setTitle(getString(R.string.activity_change_channel_title));
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        setTool(null, mToolbar, true, true, null);
        LinearLayoutManager linearLayoutManager = new
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        ItemDragHelperCallback callback=new ItemDragHelperCallback();
        ItemTouchHelper helper=new ItemTouchHelper(callback);
        helper.attachToRecyclerView(mRecyclerView);

        // true  if the adapter changes cannot affect the size the of recyclerView
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);//???
        mChannelAdapter=new ChannelAdapter
                (this,helper,savedChannels,otherChannels);
        mRecyclerView.setAdapter(mChannelAdapter);
        mIChangeChannelPresenter.getChannel();

    }

    @Override
    public void showChannel(ArrayList<Config.Channel> savedChannel, ArrayList<Config.Channel> otherChannel) {

        savedChannels.addAll(savedChannel);
        otherChannels.addAll(otherChannel);
        mChannelAdapter.notifyDataSetChanged();


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        RxBus.getDefaultInstance().send(new StatusEvent());
        mIChangeChannelPresenter.saveChannel(savedChannels);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


    }
}
