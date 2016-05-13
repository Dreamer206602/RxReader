package com.mx.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mx.R;
import com.mx.config.Config;
import com.mx.ui.helper.OnItemMoveListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by boobooL on 2016/5/13 0013
 * Created 邮箱 ：boobooMX@163.com
 */
public class ChannelAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements OnItemMoveListener {
    private static final int TYPE_CHANNEL_HEADER = 0;
    private static final int TYPE_CHANNEL = 1;

    private ArrayList<Config.Channel> mSavedChannel, mOtherChannelItems;
    private Context mContext;
    private ItemTouchHelper mItemTouchHelper;

    public ChannelAdapter(Context context, ItemTouchHelper itemTouchHelper, ArrayList<Config.Channel> savedChannel, ArrayList<Config.Channel> otherChannelItems) {
        this.mContext = context;
        this.mItemTouchHelper = itemTouchHelper;
        this.mSavedChannel = savedChannel;
        this.mOtherChannelItems = otherChannelItems;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 || position == mSavedChannel.size() + 1) {
            return TYPE_CHANNEL_HEADER;
        } else {
            return TYPE_CHANNEL;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case TYPE_CHANNEL_HEADER:
                view = LayoutInflater.from(mContext).inflate(R.layout.channel_header, parent, false);
                return new ChannelHeaderViewHolder(view);
            case TYPE_CHANNEL:
                view = LayoutInflater.from(mContext).inflate(R.layout.channel, parent, false);
                final ChannelViewHolder channelViewHolder = new ChannelViewHolder(view);
                channelViewHolder.itemView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        mItemTouchHelper.startDrag(channelViewHolder);
                        return true;
                    }
                });
                channelViewHolder.mIvDrag.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        mItemTouchHelper.startDrag(channelViewHolder);
                        return true;
                    }
                });
                return channelViewHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position == 0) {
            ((ChannelHeaderViewHolder) holder).mTvChannelHeader.setText(R.string.activity_change_channel_show_channel);
        } else if (position == mSavedChannel.size() + 1) {
            ((ChannelHeaderViewHolder) holder).mTvChannelHeader.setText(R.string.activity_change_channel_other_channel);
        } else if (position > 0 && position < mSavedChannel.size() + 1) {
            ((ChannelViewHolder) holder).mIvChannel.setImageResource(mSavedChannel.get(position - 1).getIcon());
            ((ChannelViewHolder) holder).mTvChannel.setText(mSavedChannel.get(position - 1).getTitle());
        } else if (position > mSavedChannel.size() + 1) {
            ((ChannelViewHolder) holder).mIvChannel.setImageResource(mOtherChannelItems.get(position - (mSavedChannel.size() + 2)).getIcon());
            ((ChannelViewHolder) holder).mTvChannel.setText(mOtherChannelItems.get(position - (mSavedChannel.size() + 2)).getTitle());
        }
    }

    @Override
    public int getItemCount() {
        return mSavedChannel.size() + mOtherChannelItems.size() + 2;
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < mSavedChannel.size() + 1 && toPosition < mSavedChannel.size() + 1 && toPosition > 0) {
            //显示栏目内移动
            Config.Channel item = mSavedChannel.get(fromPosition - 1);
            mSavedChannel.remove(fromPosition - 1);
            mSavedChannel.add(toPosition - 1, item);
            notifyItemMoved(fromPosition, toPosition);
        } else if (fromPosition < mSavedChannel.size() + 1 && toPosition == mSavedChannel.size() + 1) {
            //显示栏目移动到未显示栏目
            Config.Channel item = mSavedChannel.get(fromPosition - 1);
            mSavedChannel.remove(fromPosition - 1);
            mOtherChannelItems.add(0, item);
            notifyItemMoved(fromPosition, mSavedChannel.size() + 2);
        } else if (fromPosition > mSavedChannel.size() + 1 && toPosition == mSavedChannel.size() + 1) {
            //未显示栏目移动到显示栏目
            Config.Channel item = mOtherChannelItems.get(fromPosition - 2 - mSavedChannel.size());
            mOtherChannelItems.remove(fromPosition - 2 - mSavedChannel.size());
            mSavedChannel.add(0, item);
            notifyItemMoved(fromPosition, 1);
        } else if (fromPosition >= mSavedChannel.size() + 2 && toPosition <= mSavedChannel.size() + mOtherChannelItems.size() + 2 && toPosition >= mSavedChannel.size() + 2) {
            //未显示栏目内移动
            Config.Channel item = mOtherChannelItems.get(fromPosition - 1 - mSavedChannel.size() - 1);
            mOtherChannelItems.remove(fromPosition - 1 - mSavedChannel.size() - 1);
            mOtherChannelItems.add(toPosition - 1 - mSavedChannel.size() - 1, item);
            notifyItemMoved(fromPosition, toPosition);
        }
    }



    class ChannelHeaderViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_channel_header)
        TextView mTvChannelHeader;

        public ChannelHeaderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class ChannelViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_channel)
        ImageView mIvChannel;
        @BindView(R.id.tv_channel)
        TextView mTvChannel;
        @BindView(R.id.iv_drag)
        ImageView mIvDrag;

        public ChannelViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

