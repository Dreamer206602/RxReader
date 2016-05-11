package com.mx.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mx.R;
import com.mx.config.Config;
import com.mx.model.zhihu.ZhihuDailyItem;
import com.mx.ui.activity.ZhihuStoryActivity;
import com.mx.utils.DBUtils;
import com.mx.utils.ScreenUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by boobooL on 2016/5/11 0011
 * Created 邮箱 ：boobooMX@163.com
 */
public class ZhihuAdapter extends RecyclerView.Adapter<ZhihuAdapter.ZhihuViewHolder> {
    private ArrayList<ZhihuDailyItem> zhihuStories;
    private Context mContext;

    public ZhihuAdapter(Context context,ArrayList<ZhihuDailyItem> zhihuStories) {
        this.zhihuStories = zhihuStories;
        this.mContext = context;
    }

    @Override
    public ZhihuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ZhihuViewHolder(LayoutInflater.from(mContext).inflate(R.layout.zhihu_daily_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final ZhihuViewHolder holder, int position) {
        final ZhihuDailyItem zhihuDailyItem = zhihuStories.get(holder.getAdapterPosition());
        if (DBUtils.getDBUtils(mContext).isRead(Config.ZHIHU, zhihuDailyItem.getId(), 1))
            holder.tvZhihuDaily.setTextColor(Color.GRAY);
        else
            holder.tvZhihuDaily.setTextColor(Color.BLACK);
        holder.tvZhihuDaily.setText(zhihuDailyItem.getTitle());
        holder.tvTime.setText(zhihuDailyItem.getDate());
        holder.cvZhihu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBUtils.getDBUtils(mContext).insertHasRead(Config.ZHIHU, zhihuDailyItem.getId(), 1);
                holder.tvZhihuDaily.setTextColor(Color.GRAY);
                Intent intent = new Intent(mContext, ZhihuStoryActivity.class);
                intent.putExtra("type", ZhihuStoryActivity.TYPE_ZHIHU);
                intent.putExtra("id", zhihuDailyItem.getId());
                intent.putExtra("title", zhihuDailyItem.getTitle());
                mContext.startActivity(intent);
            }
        });
        holder.btnZhihu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(mContext, holder.btnZhihu);
                popupMenu.getMenuInflater().inflate(R.menu.pop_menu, popupMenu.getMenu());
                popupMenu.getMenu().removeItem(R.id.pop_share);
                popupMenu.getMenu().removeItem(R.id.pop_fav);
                final boolean isRead = DBUtils.getDBUtils(mContext).isRead(Config.ZHIHU, zhihuDailyItem.getId(), 1);
                if (!isRead)
                    popupMenu.getMenu().findItem(R.id.pop_unread).setTitle(R.string.common_set_read);
                else
                    popupMenu.getMenu().findItem(R.id.pop_unread).setTitle(R.string.common_set_unread);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.pop_unread:
                                if (isRead) {
                                    DBUtils.getDBUtils(mContext).insertHasRead(Config.ZHIHU, zhihuDailyItem.getId(), 0);
                                    holder.tvZhihuDaily.setTextColor(Color.BLACK);
                                } else {
                                    DBUtils.getDBUtils(mContext).insertHasRead(Config.ZHIHU, zhihuDailyItem.getId(), 1);
                                    holder.tvZhihuDaily.setTextColor(Color.GRAY);
                                }
                                break;
                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });
        runEnterAnimation(holder.itemView);
        if (zhihuStories.get(position).getImages() != null)
            Glide.with(mContext).load(zhihuDailyItem.getImages()[0]).placeholder(R.drawable.icon).into(holder.ivZhihuDaily);
    }

    private void runEnterAnimation(View view) {
        view.setTranslationX(ScreenUtil.getScreenWidth(mContext));
        view.animate()
                .translationX(0)
                .setStartDelay(100)
                .setInterpolator(new DecelerateInterpolator(3.f))
                .setDuration(700)
                .start();
    }

    @Override
    public int getItemCount() {
        return zhihuStories.size();
    }

    public class ZhihuViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_zhihu_daily)
        ImageView ivZhihuDaily;
        @BindView(R.id.tv_zhihu_daily)
        TextView tvZhihuDaily;
        @BindView(R.id.cv_zhihu)
        CardView cvZhihu;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.btn_zhihu)
        Button btnZhihu;

        public ZhihuViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
