package com.lightcone.feedback.message;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lightcone.utils.JsonUtil;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.List;

/**
 * Created by chenkehui on 0004 2017/12/4.
 */

public class Message extends DataSupport {
    // 消息id，
    private long msgId;
    // 用户id
    private long uid;
    // 发送者id(senderId!=uid则为客服消息)
    private long senderId;
    // 消息文本
    private String msgContent;
    // 消息扩展，格式暂定为：数组转json字符串，如：
    // 图片类型 ["image", "thumbnail_url", "original_url", ratio]
    // 声音类型 ["sound", "url", duration]
    private String extend;
    // 发送时间戳(ms)
    private long sendTime;

    @JsonIgnore
    private int type;

    @JsonIgnore
    @Column(ignore = true)
    public List<String> extendList;

    public Message() {
        decideType();
    }

    private void decideType() {
        if (type > 0) {
            return;
        }

        if (extend == null || extend.length() == 0) {
            type = MessageType.TEXT.typeValue;
            return;
        }
        try {
            extendList = JsonUtil.readValue(extend, List.class, String.class);
            if (extendList.size() > 0) {
                if (extendList.get(0).equals("image")) {
                    type = MessageType.IMAGE.typeValue;
                    return;
                }
            }
        } catch (IOException e) {

        }
        msgContent = "无法解析的消息类型";
        type = MessageType.TEXT.typeValue;
    }

    public MessageType getMessageType() {
        return MessageType.typeForInt(type);
    }

    public long getMsgId() {
        return this.msgId;
    }

    public void setMsgId(long msgId) {
        this.msgId = msgId;
    }

    public long getUid() {
        return this.uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public long getSenderId() {
        return this.senderId;
    }

    public void setSenderId(long senderId) {
        this.senderId = senderId;
    }

    public String getMsgContent() {
        return this.msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public String getExtend() {
        return this.extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
        if (extend != null && extend.length() > 0) {
            type = 0;
        }
        decideType();
    }

    public long getSendTime() {
        return this.sendTime;
    }

    public void setSendTime(long sendTime) {
        this.sendTime = sendTime;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean fromMe() {
        return senderId == uid;
    }

}
