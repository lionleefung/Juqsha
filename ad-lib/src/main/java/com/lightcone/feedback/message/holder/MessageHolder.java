package com.lightcone.feedback.message.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.lightcone.common.R;
import com.lightcone.feedback.message.Message;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by chenkehui on 0021 2017/2/21.
 */

abstract public class MessageHolder extends RecyclerView.ViewHolder {

    private TextView timeLabel;
    protected Message message;

    public MessageHolder(View itemView) {
        super(itemView);
        timeLabel = itemView.findViewById(R.id.time_label);
    }

    public void resetWithData(Message msg) {
        message = msg;

        if (message.getSendTime() == 0) {
            timeLabel.setVisibility(View.INVISIBLE);
        } else {
            timeLabel.setVisibility(View.VISIBLE);
        }

        String timeStr = SimpleDateFormat.getDateTimeInstance().format(new Date(message.getSendTime()));
        timeLabel.setText(timeStr);
    }

}
