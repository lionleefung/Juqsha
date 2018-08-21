package com.lightcone.feedback;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lightcone.common.R;
import com.lightcone.feedback.message.Message;
import com.lightcone.feedback.message.MessageAdapter;
import com.lightcone.feedback.message.MessageManager;
import com.lightcone.feedback.message.callback.LoadMsgCallback;
import com.lightcone.feedback.message.callback.SendMsgCallback;
import com.lightcone.feedback.misc.BitmapHelper;
import com.lightcone.feedback.misc.KeyboardHelper;
import com.lightcone.feedback.misc.UriHelper;
import com.lightcone.utils.JsonUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class FeedbackActivity extends Activity {

    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private EditText inputView;
    private View sendBtn;
    private View mediaBtn;

    private MessageAdapter messageAdapter;
    List<Message> messages;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        findViewByIds();
        initRecyclerView();
        initInputView();

        initData();
    }

    private void initInputView() {
        sendBtn.setVisibility(View.VISIBLE);
        mediaBtn.setVisibility(View.INVISIBLE);
        inputView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                sendBtn.setVisibility(inputView.getText().length() == 0 ? View.VISIBLE : View
                        .VISIBLE);
                mediaBtn.setVisibility(inputView.getText().length() == 0 ? View.INVISIBLE : View
                        .INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void findViewByIds() {
        refreshLayout = findViewById(R.id.swipe_layout);
        recyclerView = findViewById(R.id.recycler_view);
        inputView = findViewById(R.id.text_input_view);
        sendBtn = findViewById(R.id.btn_send_msg);
        mediaBtn = findViewById(R.id.btn_add_media);
    }

    private void initRecyclerView() {
        refreshLayout.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                long time = 0;
                for (Message m : messages) {
                    if (m.getSendTime() > 1000) {
                        time = m.getSendTime();
                        break;
                    }
                }
                loadMore(time);
            }
        });

        RecyclerView.LayoutManager lm = new LinearLayoutManager(this, LinearLayoutManager
                .VERTICAL, false);
        recyclerView.setLayoutManager(lm);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context
                        .INPUT_METHOD_SERVICE);
                if (imm.isActive()) {
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                return false;
            }
        });
        new KeyboardHelper(getWindow().getDecorView(), new KeyboardHelper.Listener() {
            @Override
            public void onKeyboardOpened(int keyboardHeightInPx) {
                if (messages.size() > 0) {
                    recyclerView.scrollToPosition(messages.size() - 1);
                }
            }

            @Override
            public void onKeyboardClosed() {

            }
        });
    }

    private void initData() {
        messages = MessageManager.getInstance().loadLocalMessages();
        if (messages == null) {
            messages = new ArrayList<>();
        } else {
            tryAddNewestReply();
        }
        // 首条消息
        Message m = new Message();
        m.setMsgContent(String.format(getString(R.string.default_hello), getAppName(this)));
        m.setSenderId(1);
        messages.add(0, m);

        loadMore(0);

        messageAdapter = new MessageAdapter(this, messages);
        recyclerView.setAdapter(messageAdapter);

        if (messages != null) {
            recyclerView.scrollToPosition(messages.size() - 1);
        }
    }

    private String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    private void loadMore(final long time) {
        MessageManager.getInstance().loadMessages(time, new LoadMsgCallback() {
            @Override
            public void onResult(final boolean hasError, final boolean hasMore, final
            List<Message> msgs) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isFinishing()) {
                            return;
                        }

                        refreshLayout.setRefreshing(false);
                        if (hasError) {
                            showErrorTip();
                            return;
                        }

                        if (msgs == null || msgs.size() == 0) {
                            return;
                        }

                        Collections.reverse(msgs);

                        // 首条消息
                        if (!hasMore) {
                            Message m = new Message();
                            String appName = "";
                            try {
                                PackageInfo pkg = getPackageManager().getPackageInfo
                                        (getPackageName(), 0);
                                appName = pkg.applicationInfo.loadLabel(getPackageManager())
                                        .toString();
                            } catch (PackageManager.NameNotFoundException e) {
                                e.printStackTrace();
                            }
                            m.setMsgContent(String.format(getString(R.string.default_hello),
                                    appName));
                            m.setSenderId(1);
                            msgs.add(0, m);
                        }

                        if (time == 0) {
                            messages.clear();
                            messages.addAll(msgs);
                            tryAddNewestReply();
                            messageAdapter.notifyDataSetChanged();
                            recyclerView.scrollToPosition(messages.size() - 1);
                        } else {
                            messages.addAll(0, msgs);
                            messageAdapter.notifyItemRangeInserted(0, msgs.size());
                        }
                    }
                });
            }
        });
    }

    public void onSendBtnClick(View view) {
        String txt = inputView.getText().toString().trim();
        if (txt.length() == 0) {
            inputView.setText("");
            return;
        }
        sendBtn.setEnabled(false);

        final Message m = new Message();
        m.setMsgContent(txt);
        m.setSendTime(System.currentTimeMillis());

        MessageManager.getInstance().sendMessage(m, new SendMsgCallback() {
            @Override
            public void onResult(final boolean hasError) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isFinishing()) {
                            return;
                        }
                        sendBtn.setEnabled(true);
                        if (hasError) {
                            showErrorTip();
                            return;
                        }
                        inputView.setText("");
                        recyclerView.scrollToPosition(messages.size());
                        messageAdapter.notifyItemInserted(messages.size());
                        messages.add(m);
                        // 回复
                        recyclerView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                sendReplyMsg();
                            }
                        }, 600);
                    }
                });
            }
        });
    }

    // 回复不存数据库
    private void sendReplyMsg() {
        Message replyM = new Message();
        replyM.setMsgContent(getString(R.string.default_reply));
        replyM.setSendTime(System.currentTimeMillis());
        replyM.setSenderId(1);
        recyclerView.scrollToPosition(messages.size());
        messageAdapter.notifyItemInserted(messages.size());
        messages.add(replyM);
    }

    // 最新一条消息如果不是客服，要在最后加一条客服回复
    private void tryAddNewestReply() {
        if (messages == null || messages.size() == 0) {
            return;
        }
        Message newest = messages.get(messages.size() - 1);
        if (newest.fromMe()) {
            Message m = new Message();
            m.setMsgContent(getString(R.string.default_reply));
            m.setSenderId(1);
            m.setSendTime(newest.getSendTime() + 1000);
            messages.add(m);
        }
    }

    public void onAddMediaClick(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media
                .EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String imagePath = UriHelper.getPath(this, selectedImage);
            Bitmap bm = BitmapHelper.sampleDecodeFile(imagePath);
            String path = BitmapHelper.saveTempBitmapFile(bm);
            List l = Arrays.asList("image", path, path, 1.f * bm.getWidth() / bm.getHeight());
            String extend = "";
            try {
                extend = JsonUtil.writeValueAsString(l);
            } catch (JsonProcessingException e) {

            }
            Message m = new Message();
            m.setSendTime(System.currentTimeMillis());
            m.setExtend(extend);

            recyclerView.scrollToPosition(messages.size());
            messageAdapter.notifyItemInserted(messages.size());
            messages.add(m);
            // 回复
            sendReplyMsg();
        }
    }

    private void showErrorTip() {
        Toast.makeText(FeedbackActivity.this, getString(R.string.network_error), Toast
                .LENGTH_SHORT).show();
    }

    public void onBackClick(View view) {
        finish();
    }

}
