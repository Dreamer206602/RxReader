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
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mx.R;
import com.mx.config.Config;
import com.mx.model.ithome.ItHomeItem;
import com.mx.ui.activity.ItHomeActivity;
import com.mx.utils.DBUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by boobooL on 2016/5/11 0011
 * Created 邮箱 ：boobooMX@163.com
 */
public class ItHomeAdapter extends  RecyclerView.Adapter<ItHomeAdapter.ItViewHolder>{
    private ArrayList<ItHomeItem> itHomeItems;
    private Context mContext;

    public ItHomeAdapter(Context context, ArrayList<ItHomeItem> itHomeItems) {
        this.itHomeItems = itHomeItems;
        this.mContext=context;
    }

    @Override
    public ItViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItViewHolder(LayoutInflater.from(mContext).inflate(R.layout.ithome_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final ItViewHolder holder, int position) {
        final ItHomeItem itHomeItem = itHomeItems.get(holder.getAdapterPosition());
        if (DBUtils.getDBUtils(mContext).isRead(Config.IT, itHomeItem.getNewsid(), 1))
            holder.tvTitle.setTextColor(Color.GRAY);
        else
            holder.tvTitle.setTextColor(Color.BLACK);
        holder.tvTitle.setText(itHomeItem.getTitle());
        holder.tvTime.setText(itHomeItem.getPostdate());
        holder.tvDescription.setText(itHomeItem.getDescription());
        Glide.with(mContext).load(itHomeItem.getImage()).placeholder(R.drawable.bg).into(holder.ivIthome);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBUtils.getDBUtils(mContext).insertHasRead(Config.IT, itHomeItem.getNewsid(), 1);
                holder.tvTitle.setTextColor(Color.GRAY);
                mContext.startActivity(new Intent(mContext, ItHomeActivity.class)
                        .putExtra("item", itHomeItem));
            }
        });
        holder.btnIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(mContext, holder.btnIt);
                popupMenu.getMenuInflater().inflate(R.menu.pop_menu, popupMenu.getMenu());
                popupMenu.getMenu().removeItem(R.id.pop_fav);
                final boolean isRead = DBUtils.getDBUtils(mContext).isRead(Config.IT, itHomeItem.getNewsid(), 1);
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
                                    DBUtils.getDBUtils(mContext).insertHasRead(Config.IT, itHomeItem.getNewsid(), 0);
                                    holder.tvTitle.setTextColor(Color.BLACK);
                                } else {
                                    DBUtils.getDBUtils(mContext).insertHasRead(Config.IT, itHomeItem.getNewsid(), 1);
                                    holder.tvTitle.setTextColor(Color.GRAY);
                                }
                                break;
                            case R.id.pop_share:
                                Intent shareIntent = new Intent();
                                shareIntent.setAction(Intent.ACTION_SEND);
                                shareIntent.putExtra(Intent.EXTRA_TEXT, itHomeItem.getTitle() +
                                        " http://ithome.com" + itHomeItem.getUrl() + mContext.getString(R.string.share_tail));
                                shareIntent.setType("text/plain");
                                //设置分享列表的标题，并且每次都显示分享列表
                                mContext.startActivity(Intent.createChooser(shareIntent, mContext.getString(R.string.share)));
                                break;
                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });
        //runEnterAnimation(holder.itemView);
    }

    public void runEnterAnimation(View view){
        view.setScaleX(3f);
        view.setScaleY(3f);
        view.animate()
                .setStartDelay(100)
                .setInterpolator(new DecelerateInterpolator(3f))
                .setDuration(700)
                .start();
    }

    @Override
    public int getItemCount() {
        return itHomeItems.size();
    }

    public class ItViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_ithome)
        ImageView ivIthome;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_description)
        TextView tvDescription;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.btn_it)
        Button btnIt;

        public ItViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
