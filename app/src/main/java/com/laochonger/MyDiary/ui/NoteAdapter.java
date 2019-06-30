package com.laochonger.MyDiary.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.laochonger.MyDiary.R;
import com.laochonger.MyDiary.bean.NoteBean;
import com.laochonger.MyDiary.event.DeleteNoteEvent;
import com.laochonger.MyDiary.utils.GetDate;

import org.greenrobot.eventbus.EventBus;

import java.util.List;


public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.DiaryViewHolder> {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<NoteBean> mNoteBeanList;
    private int mEditPosition = -1;

    public NoteAdapter(Context context, List<NoteBean> mNodeBeanList){
        mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mNoteBeanList = mNodeBeanList;
    }
    @Override
    public DiaryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DiaryViewHolder(mLayoutInflater.inflate(R.layout.item_rv_diary, parent, false));
    }

    @Override
    public void onBindViewHolder(final DiaryViewHolder holder, final int position) {

        String dateSystem = GetDate.getDate().toString();
        /**
         * 如果该备忘录是当天写的，则将日期左边的圆圈设置成橙色的
         */
        if(mNoteBeanList.get(position).getDate().equals(dateSystem)){
            holder.mIvCircle.setImageResource(R.drawable.circle_orange);
        }
        holder.mTvDate.setText(mNoteBeanList.get(position).getDate());
        holder.mTvTitle.setText(mNoteBeanList.get(position).getTitle());
        holder.mTvContent.setText("       " + mNoteBeanList.get(position).getContent());
        holder.mIvEdit.setVisibility(View.GONE);
        holder.mIVLook.setVisibility(View.GONE);
        holder.mIvDelete.setVisibility(View.GONE);
        /**
         * 当点击日记的内容时候，则显示出删除按钮,其他的不显示
         */
//
//        Log.e("s",mEditPosition);
        if(mEditPosition == position){
//            String a= "";
//            a=a+position;
//            Log.e("f", a);
            holder.mIvDelete.setVisibility(View.GONE);
        }else {
            holder.mIvDelete.setVisibility(View.GONE);
        }
        holder.mLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.mIvDelete.getVisibility() == View.VISIBLE){
                    holder.mIvDelete.setVisibility(View.GONE);
                }else {
                    holder.mIvDelete.setVisibility(View.VISIBLE);
                }
                if(mEditPosition != position){
                    notifyItemChanged(mEditPosition);
                }
                mEditPosition = position;
            }
        });
/**
 * 使用 EventBus 来打开修改/查看日记的界面，事件接收函数在 ShowNoteActivity 中
 */
        holder.mIvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new DeleteNoteEvent(position));
            }
        });

//        holder.mIVLook.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                EventBus.getDefault().post(new StarLookDiaryEvent(position));
//            }
//        });
//
//        holder.mIvEdit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                EventBus.getDefault().post(new StartUpdateDiaryEvent(position));
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return mNoteBeanList.size();
    }

    public static class DiaryViewHolder extends RecyclerView.ViewHolder{


        TextView mTvDate;
        TextView mTvTitle;
        TextView mTvContent;
        ImageView mIvEdit;
        ImageView mIVLook;
        ImageView mIvDelete;
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
            mIvDelete = (ImageView) view.findViewById(R.id.main_iv_delete);
            mIVLook = (ImageView) view.findViewById(R.id.main_iv_look);
            mLlTitle = (LinearLayout) view.findViewById(R.id.main_ll_title);
            mLl = (LinearLayout) view.findViewById(R.id.item_ll);
            mLlControl = (LinearLayout) view.findViewById(R.id.item_ll_control);
            mRlEdit = (RelativeLayout) view.findViewById(R.id.item_rl_edit);
        }
    }
}