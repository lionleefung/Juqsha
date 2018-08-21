package com.lightcone.feedback.message.holder;

import android.view.View;
import android.widget.TextView;

import com.lightcone.common.R;
import com.lightcone.feedback.message.Message;

/**
 * Created by chenkehui on 0017 2017/2/17.
 */

public class MessageTextHolder extends MessageHolder {

    private TextView contentTextView;

    public MessageTextHolder(View itemView) {
        super(itemView);
        contentTextView = itemView.findViewById(R.id.chat_item_content_text);
    }

    @Override
    public void resetWithData(Message msg) {
        super.resetWithData(msg);
        contentTextView.setText(msg.getMsgContent());
    }

}
