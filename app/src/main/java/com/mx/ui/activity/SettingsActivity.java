package com.mx.ui.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.hannesdorfmann.swipeback.Position;
import com.hannesdorfmann.swipeback.SwipeBack;
import com.mx.R;
import com.mx.event.StatusEvent;
import com.mx.ui.fragment.SettingsFragment;
import com.mx.utils.RxBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.functions.Action1;

public class SettingsActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.fl_preference)
    FrameLayout mFrameLayout;
    private Subscription mSubscription;
    private SettingsFragment mSettingsFragment = new SettingsFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SwipeBack.attach(this, Position.LEFT)
                .setContentView(R.layout.activity_settings)
                .setSwipeBackView(R.layout.swipe_back);
        ButterKnife.bind(this);

        mToolbar.setTitle(getString(R.string.activity_setting_title));
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

        mSubscription = RxBus.getDefaultInstance().toObservable(StatusEvent.class)
                .subscribe(new Action1<StatusEvent>() {
                    @Override
                    public void call(StatusEvent statusEvent) {
                        recreate();
                    }
                });
        int vibrantColor = setTool(null, mToolbar, true, true, null);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.swipe_back);
        if (linearLayout != null) {
            linearLayout.setBackgroundColor(vibrantColor);
        }
        getFragmentManager().beginTransaction()
                .replace(R.id.fl_preference, mSettingsFragment).commit();



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!mSubscription .isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }
}
