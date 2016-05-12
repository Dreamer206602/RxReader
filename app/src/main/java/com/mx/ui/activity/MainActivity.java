package com.mx.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mx.R;
import com.mx.event.StatusEvent;
import com.mx.iView.IMainView;
import com.mx.model.UpdateItem;
import com.mx.presenter.IMainPresenter;
import com.mx.presenter.impl.MainPresenterImpl;
import com.mx.utils.RxBus;
import com.mx.utils.SharePreferenceUtil;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.functions.Action1;

public class MainActivity extends BaseActivity implements
        NavigationView.OnNavigationItemSelectedListener, IMainView {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.replace)
    FrameLayout mReplace;
    @BindView(R.id.ctl_main)
    CoordinatorLayout mCtlMain;
    @BindView(R.id.nav_view)
    NavigationView mNavView;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    private Fragment currentFragment;
    private ArrayList<Fragment> mFragments;
    private ArrayList<Integer> mTitles;
    private IMainPresenter mIMainPresenter;
    private Subscription mSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        mIMainPresenter = new MainPresenterImpl(this, this);

        boolean isKitKat = Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT;
        if (isKitKat)
            //TODO 设置系统的窗口
            mNavView.setFitsSystemWindows(false);
        setTool(null, mToolbar, true, false, mDrawerLayout);

        //对ToolBar进行设置
        // setTooBar();
        //改变statusBar颜色而DrawerLayout依然可以显示在StatusBar上
        //
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //切换动画
        overridePendingTransition(android.R.anim.fade_in,
                android.R.anim.fade_out);

        if (mNavView != null) {
            mNavView.setNavigationItemSelectedListener(this);
        }

        int[][] state = new int[][]{
                new int[]{-android.R.attr.state_checked}, // unchecked
                new int[]{android.R.attr.state_checked}  // pressed
        };
        int[] color = new int[]{
                Color.BLACK,
                getSharedPreferences(SharePreferenceUtil.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE).getInt(
                        SharePreferenceUtil.MUTED, ContextCompat.getColor(this, R.color.colorAccent))};

        mNavView.setItemTextColor(new ColorStateList(state, color));
        mNavView.setItemIconTintList(new ColorStateList(state, color));

        View headLayout = mNavView.getHeaderView(0);
        LinearLayout llImage = (LinearLayout) headLayout.findViewById(R.id.side_image);
        TextView imageDescription = (TextView) headLayout.findViewById(R.id.image_description);

        if (new File(getFilesDir().getPath() + "/bg.png").exists()) {
            BitmapDrawable bitmapDrawable = new
                    BitmapDrawable(getResources(), getFilesDir().getPath() + "/bg.png");
            llImage.setBackground(bitmapDrawable);
            imageDescription.setText(getSharedPreferences(SharePreferenceUtil.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE).getString(SharePreferenceUtil.IMAGE_DESCRIPTION,
                    "我的愿望，就是希望你的愿望里也有我"));
        }

        mIMainPresenter.initMenu(mNavView);
        mIMainPresenter.checkUpdate();

        mSubscription= RxBus.getDefaultInstance().toObservable(StatusEvent.class)
                .subscribe(new Action1<StatusEvent>() {
                    @Override
                    public void call(StatusEvent statusEvent) {
                       finish();
                        startActivity(new Intent(MainActivity.this,MainActivity.class));
                    }
                });

    }

    @Override
    public void onBackPressed() {
        assert mDrawerLayout != null;
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(!mSubscription.isUnsubscribed()){
            mSubscription.unsubscribe();
        }
        mIMainPresenter.unSubscribe();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id < mFragments.size()) {
            switchFragment(mFragments.get(id), getString(mTitles.get(id)));
        }
        switch (id) {
            case R.id.nav_setting:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            case R.id.nav_change:
                startActivity(new Intent(this, ChangeChannelActivity.class));
                break;
        }
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
        return true;
    }

    @Override
    public void initMenu(ArrayList<Fragment> fragments, ArrayList<Integer> titles) {
        mFragments = fragments;
        mTitles = titles;
        switchFragment(mFragments.get(0), getString(titles.get(0)));

    }


    @Override
    public void showUpDate(UpdateItem updateItem) {

    }

    private void switchFragment(Fragment fragment, String title) {

        Slide slideTransition;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            slideTransition = new Slide(Gravity.LEFT);
            slideTransition.setDuration(700);
            fragment.setEnterTransition(slideTransition);
            fragment.setExitTransition(slideTransition);
        }
        if (currentFragment == null ||
                !currentFragment.getClass().getName()
                        .equals(fragment.getClass().getName())) {

            getSupportFragmentManager().beginTransaction().
                    replace(R.id.replace, fragment).commit();
            currentFragment = fragment;
            ActionBar actionBar = getSupportActionBar();

            assert actionBar != null;
            actionBar.setTitle(title);

        }
    }
}
