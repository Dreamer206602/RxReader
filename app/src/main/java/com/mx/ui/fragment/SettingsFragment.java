package com.mx.ui.fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.mx.R;
import com.mx.config.BuildConfig;
import com.mx.event.StatusEvent;
import com.mx.iView.ISettingFragmentView;
import com.mx.model.UpdateItem;
import com.mx.presenter.ISettingPresenter;
import com.mx.presenter.SettingPresenterImpl;
import com.mx.utils.CacheUtil;
import com.mx.utils.RxBus;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends PreferenceFragment implements ISettingFragmentView {

    private Preference mPreference;
    private ISettingPresenter mISettingPresenter;

    public SettingsFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mISettingPresenter=new SettingPresenterImpl(this);
        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPreference=findPreference(getString(R.string.pre_cache_size));
        mPreference.setOnPreferenceClickListener
                (new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                CacheUtil.deleteDir(SettingsFragment.this.getActivity().getCacheDir());
                showCacheSize(mPreference);
                return true;
            }
        });

        //意见反馈的点击事件
        findPreference(getString(R.string.pre_feedback))
                .setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {

                        Intent intent=new Intent(Intent.ACTION_SEND,
                                Uri.fromParts("mailto","boobooMX@163.com",null));
                        startActivity(Intent.createChooser(intent,"选择邮件客户端"));

                        return true;

                    }
                });

        findPreference(getString(R.string.pre_author))
                .setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://caiyao.name/releases")));

                        return true;
                    }
                });

        findPreference(getString(R.string.pre_status_bar))
                .setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        RxBus.getDefaultInstance().send(new StatusEvent());

                        return true;
                    }
                });

        findPreference(getString(R.string.pre_nav_color))
                .setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        RxBus.getDefaultInstance().send(new StatusEvent());
                        return true;
                    }
                });


        Preference version=findPreference(getString(R.string.pre_version));
        version.setSummary(BuildConfig.VERSION_NAME);
        version.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                mISettingPresenter.checkUpdate();
                return true;
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
        showCacheSize(mPreference);
    }

    @Override
    public void onPause() {
        super.onPause();
        mISettingPresenter.unSubscribe();
    }

    private void showCacheSize(Preference preference) {
        preference.setSummary(getActivity().getString(R.string.cache_size)+
        CacheUtil.getCacheSize(getActivity().getCacheDir()));
    }


    @Override
    public void showError(String error) {
        if(getActivity()!=null){
            Toast.makeText(getActivity(),error,Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void showUpdateDialog(final UpdateItem updateItem) {

        new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.update_title))
                .setMessage(String.format(getString(R.string.update_description),
                        updateItem.getVersionName(),
                        updateItem.getReleaseNote()))
                .setPositiveButton(getString(R.string.update_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse(updateItem.getDoenloadUrl())));
                    }
                })
                .setNegativeButton(getString(R.string.common_cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        getActivity().finish();
                    }
                })
                .show();

    }

    @Override
    public void showNoUpdate() {
        if(getActivity()!=null){
            Toast.makeText(getActivity(),
                    getString(R.string.update_no_update),
                    Toast.LENGTH_SHORT).show();
        }


    }
}
