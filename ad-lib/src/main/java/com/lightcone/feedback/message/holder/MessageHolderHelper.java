package com.lightcone.feedback.message.holder;

import android.util.SparseArray;

import com.lightcone.common.R;
import com.lightcone.feedback.message.Message;
import com.lightcone.feedback.message.MessageType;

/**
 * Created by chenkehui on 0017 2017/2/17.
 */

public class MessageHolderHelper {

    private static MessageHolderHelper instance = new MessageHolderHelper();

    public static MessageHolderHelper getInstance() {
        return instance;
    }

    private MessageHolderHelper() {
        initClassMap();
    }

    private SparseArray classMap = new SparseArray();

    private void initClassMap() {
        classMap.put(R.layout.chat_item_text_left, MessageTextHolder.class);
        classMap.put(R.layout.chat_item_text_right, MessageTextHolder.class);
        classMap.put(R.layout.chat_item_image_left, MessageImageHolder.class);
        classMap.put(R.layout.chat_item_image_right, MessageImageHolder.class);
        classMap.put(R.layout.chat_item_tip, MessageTipHolder.class);
    }

    public int layoutResId(Message message) {
        if (message.getMessageType() == MessageType.TEXT) {
            return message.fromMe() ? R.layout.chat_item_text_right : R.layout.chat_item_text_left;
        } else if (message.getMessageType() == MessageType.IMAGE) {
            return message.fromMe() ? R.layout.chat_item_image_right : R.layout
                    .chat_item_image_left;
        } else if (message.getMessageType() == MessageType.TIP) {
            return R.layout.chat_item_tip;
        } else {
            return R.layout.chat_item_tip;
        }
    }

    public Class holderClassForResId(int resId) {
        return (Class) classMap.get(resId);
    }

}
