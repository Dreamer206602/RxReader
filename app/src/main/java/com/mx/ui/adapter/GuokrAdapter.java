package com.mx.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mx.R;
import com.mx.config.Config;
import com.mx.model.guokr.GuokrHotItem;
import com.mx.ui.activity.ZhihuStoryActivity;
import com.mx.utils.DBUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by boobooL on 2016/5/11 0011
 * Created 邮箱 ：boobooMX@163.com
 */
public class GuokrAdapter extends RecyclerView.Adapter<GuokrAdapter.GuokrViewHolder>{
    private ArrayList<GuokrHotItem>mGuokrHotItems;
    private Context mContext;

    public GuokrAdapter(ArrayList<GuokrHotItem> guokrHotItems,
                        Context context) {
        mGuokrHotItems = guokrHotItems;
        mContext=context;
    }

    @Override
    public GuokrViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new
                GuokrViewHolder(LayoutInflater.from(mContext).inflate(R.layout.ithome_item,parent,false));
    }

    @Override
    public void onBindViewHolder(final GuokrViewHolder holder, int position) {

        final GuokrHotItem guokrHotItem=mGuokrHotItems.get(position);
        if(DBUtils.getDBUtils(mContext).isRead(
                Config.GUOKR,guokrHotItem.getId(),1))
            holder.mTvTitle.setTextColor(Color.GRAY);
        else
            holder.mTvTitle.setTextColor(Color.BLACK);
        holder.mTvTitle.setText(guokrHotItem.getTitle());
        holder.mTvDescription.setText(guokrHotItem.getSummary());
        holder.mTvTime.setText(guokrHotItem.getTime());
        Glide.with(mContext).load(guokrHotItem.getSmallImage())
                .into(holder.mIvIthome);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBUtils.getDBUtils(mContext)
                        .insertHasRead(Config.GUOKR,guokrHotItem.getId(),1);
                holder.mTvTitle.setTextColor(Color.GRAY);
                Intent intent=new Intent
                        (mContext,ZhihuStoryActivity.class);
                intent.putExtra("type",ZhihuStoryActivity.TYPE_GUOKR);
                intent.putExtra("id",guokrHotItem.getId());
                intent.putExtra("title",guokrHotItem.getTitle());
                mContext.startActivity(intent);
            }
        });

        holder.mBtnIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu=new PopupMenu(mContext,holder.mBtnIt);
                popupMenu.getMenuInflater().inflate(R.menu.pop_menu,popupMenu.getMenu());
                popupMenu.getMenu().removeItem(R.id.pop_share);
                popupMenu.getMenu().removeItem(R.id.pop_fav);
                final boolean isRead=DBUtils.getDBUtils(mContext).isRead(
                        Config.GUOKR,guokrHotItem.getId(),1
                );
                if(!isRead)
                    popupMenu.getMenu().findItem(R.id.pop_unread).setTitle(
                            R.string.common_set_read);
                else
                    popupMenu.getMenu().findItem(R.id.pop_unread)
                    .setTitle(R.string.common_set_read);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.pop_fav:
                                break;
                            case R.id.pop_unread:
                                if(isRead){
                                    DBUtils.getDBUtils(mContext)
                                            .insertHasRead(Config.GUOKR,
                                                    guokrHotItem.getId(),0);
                                    holder.mTvTitle.setTextColor(Color.BLACK);
                                }else{
                                    DBUtils.getDBUtils(mContext)
                                            .insertHasRead(Config.GUOKR,
                                                    guokrHotItem.getId(),1);
                                    holder.mTvTitle.setTextColor(Color.GRAY);
                                }
                                break;
                        }
                        return true;
                    }
                });
                popupMenu.show();


            }
        });


    }

    @Override
    public int getItemCount() {
        return mGuokrHotItems.size();
    }


    public class GuokrViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tv_title)
        TextView mTvTitle;
        @BindView(R.id.iv_ithome)
        ImageView mIvIthome;
        @BindView(R.id.tv_description)
        TextView mTvDescription;
        @BindView(R.id.tv_time)
        TextView mTvTime;
        @BindView(R.id.btn_it)
        Button mBtnIt;
        public GuokrViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
