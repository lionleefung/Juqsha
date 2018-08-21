package com.lightcone.feedback.message.holder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lightcone.common.R;
import com.lightcone.feedback.BubbleMaskFrame;
import com.bumptech.glide.Glide;
import com.lightcone.feedback.message.Message;

/**
 * Created by chenkehui on 0017 2017/2/17.
 */

public class MessageImageHolder extends MessageHolder {

    private ImageView contentImageView;
    private BubbleMaskFrame maskFrame;

    public MessageImageHolder(View itemView) {
        super(itemView);

        contentImageView = itemView.findViewById(R.id.chat_item_image);
        maskFrame = itemView.findViewById(R.id.chat_item_bubble_frame);
        contentImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
    }

    private String url;
    private String bigUrl;
    private double ratio;

    @Override
    public void resetWithData(Message msg) {
        super.resetWithData(msg);

        if (msg.extendList == null || msg.extendList.size() < 3) {
            return;
        }

        url = msg.extendList.get(1);
        bigUrl = msg.extendList.get(2);
        ratio = Float.parseFloat(msg.extendList.get(3));
        if (url == null || url.length() == 0 || ratio < 0.01) {
            System.out.println("图片为空");
            return;
        }

        setMaskSize();
        Glide.with(contentImageView).load(url).into(contentImageView);
    }

    private void setMaskSize() {
        maskFrame.setRatio(ratio);
        ViewGroup.LayoutParams lp = contentImageView.getLayoutParams();
        lp.width = maskFrame.width;
        lp.height = maskFrame.height;
        contentImageView.setLayoutParams(lp);
    }
}
