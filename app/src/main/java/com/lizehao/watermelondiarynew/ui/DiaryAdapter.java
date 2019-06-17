package com.lizehao.watermelondiarynew.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lizehao.watermelondiarynew.R;
import com.lizehao.watermelondiarynew.bean.DiaryBean;
import com.lizehao.watermelondiarynew.event.StartUpdateDiaryEvent;
import com.lizehao.watermelondiarynew.event.StarLookDiaryEvent;
import com.lizehao.watermelondiarynew.utils.GetDate;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by 李 on 2017/1/26.
 */
public class DiaryAdapter extends RecyclerView.Adapter<DiaryAdapter.DiaryViewHolder> {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<DiaryBean> mDiaryBeanList;
    private int mEditPosition = -1;

    public DiaryAdapter(Context context, List<DiaryBean> mDiaryBeanList){
        mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mDiaryBeanList = mDiaryBeanList;
    }
    @Override
    public DiaryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DiaryViewHolder(mLayoutInflater.inflate(R.layout.item_rv_diary, parent, false));
    }

    @Override
    public void onBindViewHolder(final DiaryViewHolder holder, final int position) {

        String dateSystem = GetDate.getDate().toString();
        /**
         * 如果该日记是当天写的，则将日期左边的圆圈设置成橙色的
         */
        if(mDiaryBeanList.get(position).getDate().equals(dateSystem)){
            holder.mIvCircle.setImageResource(R.drawable.circle_orange);
        }
        holder.mTvDate.setText(mDiaryBeanList.get(position).getDate());
        holder.mTvTitle.setText(mDiaryBeanList.get(position).getTitle());
        holder.mTvContent.setText("       " + mDiaryBeanList.get(position).getContent());
        holder.mIvEdit.setVisibility(View.INVISIBLE);
        holder.mIVLook.setVisibility(View.INVISIBLE);
        /**
         * 当点击日记的内容时候，则显示出编辑按钮
         */
        if(mEditPosition == position){
            holder.mIvEdit.setVisibility(View.VISIBLE);
            holder.mIVLook.setVisibility(View.GONE);
        }else {
            holder.mIvEdit.setVisibility(View.GONE);
            holder.mIVLook.setVisibility(View.VISIBLE);
        }
        holder.mLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.mIvEdit.getVisibility() == View.VISIBLE){
                    holder.mIvEdit.setVisibility(View.GONE);
                    holder.mIVLook.setVisibility(View.VISIBLE);
                }else {
                    holder.mIvEdit.setVisibility(View.VISIBLE);
                    holder.mIVLook.setVisibility(View.GONE);
                }
                if(mEditPosition != position){
                    notifyItemChanged(mEditPosition);
                }
                mEditPosition = position;
            }
        });
/**
 * 使用 EventBus 来打开修改/查看日记的界面，事件接收函数在 MainActivity 中
 */
        holder.mIVLook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new StarLookDiaryEvent(position));
            }
        });

        holder.mIvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new StartUpdateDiaryEvent(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return mDiaryBeanList.size();
    }

    public static class DiaryViewHolder extends RecyclerView.ViewHolder{


        TextView mTvDate;
        TextView mTvTitle;
        TextView mTvContent;
        ImageView mIvEdit;
        ImageView mIVLook;
        LinearLayout mLlTitle;
        LinearLayout mLl;
        ImageView mIvCircle;
        LinearLayout mLlControl;
        RelativeLayout mRlEdit;

        DiaryViewHolder(View view){
            super(view);
            mIvCircle = (ImageView) view.findViewById(R.id.main_iv_circle);
            mTvDate = (TextView) view.findViewById(R.id.main_tv_date);
            mTvTitle = (TextView) view.findViewById(R.id.main_tv_title);
            mTvContent = (TextView) view.findViewById(R.id.main_tv_content);
            mIvEdit = (ImageView) view.findViewById(R.id.main_iv_edit);
            mIVLook = (ImageView) view.findViewById(R.id.main_iv_look);
            mLlTitle = (LinearLayout) view.findViewById(R.id.main_ll_title);
            mLl = (LinearLayout) view.findViewById(R.id.item_ll);
            mLlControl = (LinearLayout) view.findViewById(R.id.item_ll_control);
            mRlEdit = (RelativeLayout) view.findViewById(R.id.item_rl_edit);
        }
    }
}