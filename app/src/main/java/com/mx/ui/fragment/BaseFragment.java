package com.mx.ui.fragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;

import com.mx.R;
import com.mx.utils.SharePreferenceUtil;

/**
 * Created by boobooL on 2016/5/10 0010
 * Created 邮箱 ：boobooMX@163.com
 */
public class BaseFragment extends Fragment {
    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public void setSwipeRefreshLayoutColor(SwipeRefreshLayout swipeRefreshLayoutColor) {
        swipeRefreshLayoutColor.setColorSchemeColors(getActivity()
                .getSharedPreferences(SharePreferenceUtil.SHARED_PREFERENCE_NAME,
                        Context.MODE_PRIVATE).getInt(SharePreferenceUtil.VIBRANT,
                        ContextCompat.getColor(getActivity(),
                                R.color.colorAccent)));
    }
}
