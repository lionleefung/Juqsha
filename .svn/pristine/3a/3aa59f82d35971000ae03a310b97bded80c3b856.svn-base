package com.lightcone.feedback.message;

/**
 * Created by chenkehui on 0017 2017/2/17.
 */

public enum MessageType {
    UNKNOWN(0),

    TEXT(10001),
    IMAGE(10002),
    TIP(10003),

    ;

    public int typeValue;

    MessageType(int type) {
        this.typeValue = type;
    }

    public static MessageType typeForInt(int code) {
        for (MessageType item : values()) {
            if (item.typeValue == code) {
                return item;
            }
        }
        return UNKNOWN;
    }

}
