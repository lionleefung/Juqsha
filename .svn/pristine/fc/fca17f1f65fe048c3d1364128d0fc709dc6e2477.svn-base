package com.lightcone.feedback.message.holder;

import android.view.View;
import android.widget.TextView;

import com.lightcone.common.R;
import com.lightcone.feedback.message.Message;

/**
 * Created by chenkehui on 0022 2017/2/22.
 */

public class MessageTipHolder extends MessageHolder {

    private TextView tv;

    public MessageTipHolder(View itemView) {
        super(itemView);
        tv = itemView.findViewById(R.id.text_view);
    }

    @Override
    public void resetWithData(Message msg) {
        super.resetWithData(msg);
        tv.setText(msg.getMsgContent());
    }

}
