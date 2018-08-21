package com.lightcone.feedback.message;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lightcone.feedback.message.holder.MessageHolder;
import com.lightcone.feedback.message.holder.MessageHolderHelper;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Created by chenkehui on 0021 2017/2/21.
 */

public class MessageAdapter extends RecyclerView.Adapter {

    private final Context mContext;

    private List<Message> mData;

    public MessageAdapter(Context context, List<Message> datas) {
        mContext = context;
        mData = datas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(viewType, parent, false);
        Class holderClass = MessageHolderHelper.getInstance().holderClassForResId(viewType);
        try {
            Constructor con = holderClass.getConstructor(View.class);
            MessageHolder holder = (MessageHolder) con.newInstance(v);
            return holder;
        } catch (NoSuchMethodException e) {
            Log.e("construct error", "Constructor.newInstance 错误");
        } catch (IllegalAccessException e) {
            Log.e("construct error", "Constructor.newInstance 错误");
        } catch (InstantiationException e) {
            Log.e("construct error", "Constructor.newInstance 错误");
        } catch (InvocationTargetException e) {
            Log.e("construct error", "Constructor.newInstance 错误");
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((MessageHolder) holder).resetWithData(mData.get(position));
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemViewType(int position) {
        Message message = mData.get(position);
        int id = MessageHolderHelper.getInstance().layoutResId(message);
        return id;
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }
}
