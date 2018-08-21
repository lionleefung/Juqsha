package com.lightcone.feedback.http.response;

import com.lightcone.feedback.message.Message;

import java.util.ArrayList;

/**
 * Created by chenkehui on 0006 2017/12/6.
 */

public class MsgLoadResponse {
    public boolean eof;
    public ArrayList<Message> msgs;
}
