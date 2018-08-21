package lightcone.com.pack.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lightcone.ad.admob.banner.AdmobBannarActivity;
import com.lightcone.googleanalysis.GaManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lightcone.com.pack.R;
import lightcone.com.pack.adapter.OnItemClickedListener;
import lightcone.com.pack.adapter.SmallRulerAdapter;
import lightcone.com.pack.bean.Play;
import lightcone.com.pack.bean.PlotBean;
import lightcone.com.pack.bean.Relation;
import lightcone.com.pack.bean.Role;
import lightcone.com.pack.dialog.BackSelectDialog;
import lightcone.com.pack.dialog.TipDialog;
import lightcone.com.pack.view.ReadView;

public class MyRole2Activity extends AdmobBannarActivity {
    private static final String TAG = "MyRole2Activity";

    @BindView(R.id.back_my_role2)
    ImageView backMyRole2;
    @BindView(R.id.title_my_role2)
    TextView titleMyRole2;
    @BindView(R.id.name_my_role2)
    TextView nameMyRole2;
    @BindView(R.id.recyclerview_my_role2)
    RecyclerView recyclerviewMyRole2;
    @BindView(R.id.content_my_role2)
    TextView contentMyRole2;
    //@BindView(R.id.describe_my_role)
    private ReadView describeMyRole2;

    private List<String> name_small = new ArrayList<>();
    private BackSelectDialog backSelectDialog;
    private TipDialog tipDialog;
    private PlotBean plotBean;
    private Role role;

    private List<String> relation_title = new ArrayList<>();
    private List<String> des_cribe = new ArrayList<>();
    private List<String> tips = new ArrayList<>();
    int index = 0;
    private int r;
    private boolean bianhuan = false;

    private int maxCharLength;
    private int curPosition;
    private List<List<Integer>> positions = new ArrayList<>();//缓存前几页开始位置
    private TipDialog tipDialog2;
    private int position1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_role2);
        ButterKnife.bind(this);
        describeMyRole2 = findViewById(R.id.describe_my_role2);
        initData();
        initView();
        loadPage(0);

    }

    private void initData() {
        plotBean = (PlotBean) getIntent().getSerializableExtra("plotBean3");
        role = (Role) getIntent().getSerializableExtra("role");
        for (Play play : plotBean.plot_plays) {
            name_small.add(play.label);
        }
        for (Relation relation : role.relations) {
            relation_title.add(relation.relationtitle);
            des_cribe.add(relation.describe);
            tips.add(relation.tip);
            positions.add(new ArrayList<Integer>());
        }
        maxCharLength = des_cribe.get(0).length();   //初始长度
        //positions.get(0).add(0);

    }

    private void loadPage(int position) {
        describeMyRole2.setText(des_cribe.get(index).substring(position));
    }

    private void initView() {
        if (plotBean.plot_id <= 15) {
            titleMyRole2.setText((plotBean.plot_name + " (" + plotBean.plot_pernum + "人" + (plotBean.plot_type == 0 ? "封闭" : "开放") + ")"));
            nameMyRole2.setText("我的角色：" + role.name);
        } else {
            titleMyRole2.setText((plotBean.plot_name + " (" + plotBean.plot_pernum + " characters" + ")"));
            nameMyRole2.setText("My character：" + role.name);
        }
        contentMyRole2.setText(role.relations.get(0).relationtitle);
        //describeMyRole.setText(role.relations.get(0).describe);
        r = getIntent().getIntExtra("textsizerole", 16);
        describeMyRole2.setTextSize(TypedValue.COMPLEX_UNIT_DIP, r);
       //describeMyRole2.setMovementMethod(ScrollingMovementMethod.getInstance());

        final SmallRulerAdapter smallRulerAdapter = new SmallRulerAdapter(name_small);
        final LinearLayoutManager linearLayout = new LinearLayoutManager(this, LinearLayout.HORIZONTAL, false);
        recyclerviewMyRole2.setLayoutManager(linearLayout);
        recyclerviewMyRole2.setAdapter(smallRulerAdapter);
        smallRulerAdapter.setOnItemClickListener(new OnItemClickedListener() {
            @Override
            public void onItemClick(View view, int position) {
                position1 = position;
                if(plotBean.plot_plays.get(position).plots.get(0).titletip != null){
                    tipDialogshow2(plotBean.plot_plays.get(position).plots.get(0).titletip);
                }else {
                    //Toast.makeText(getApplicationContext(), "你点击了" + position, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MyRole2Activity.this, ImformationRoleActivity.class);
                    intent.putExtra("plot_plays", plotBean.plot_plays.get(position));
                    intent.putExtra("r", r);
                    startActivity(intent);
                }
                //Toast.makeText(getApplicationContext(), "你点击了" + position, Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void selectBackDialogshow() {
        backSelectDialog = new BackSelectDialog(this, R.style.MyDialog, new
                BackSelectDialog.LeaveMyDialogListener2() {
                    @Override
                    public void onClick(View view) {
                        switch (view.getId()) {
                            case R.id.yes_back_dialog:
                                GaManager.sendEvent("我的角色页","点击退出继续退出","character_" +plotBean.plot_name +" _exit");
                                backSelectDialog.dismiss();
                                finish();
                                break;
                            case R.id.no_back_dialog:
                                GaManager.sendEvent("角色页","点击退出取消","character_continue");
                                backSelectDialog.dismiss();
                                break;
                        }
                    }
                });
        backSelectDialog.setCanceledOnTouchOutside(false);
        backSelectDialog.show();
    }

    public void tipDialogshow(String r) {
        tipDialog = new TipDialog(this, R.style.MyDialog, r, new
                TipDialog.LeaveMyDialogListener2() {
                    @Override
                    public void onClick(View view) {
                        switch (view.getId()) {
                            case R.id.Ok:
                                tipDialog.dismiss();
                                positions.get(index).add(curPosition);  //把每一章最后一页加入
                                index++;
                                curPosition = 0;
                                maxCharLength = role.relations.get(index).describe.length();
                                contentMyRole2.setText(relation_title.get(index));
                                loadPage(curPosition);
                                break;
                            case R.id.No:
                                contentMyRole2.setText(relation_title.get(index));
                                bianhuan = true;
                                tipDialog.dismiss();
                                break;
                        }
                    }
                });
        tipDialog.setCanceledOnTouchOutside(false);
        tipDialog.show();
    }

    public void previewPage() {
        if (positions.get(index).size() <= 0) {
            if (index <= 0) {
                return;
            } else {
                index--;
            }
        }
        int lastIndex = positions.get(index).size() - 1;
        contentMyRole2.setText(relation_title.get(index));
        maxCharLength = role.relations.get(index).describe.length();
        curPosition = positions.get(index).get(lastIndex);
        positions.get(index).remove(lastIndex);
        loadPage(curPosition);
        describeMyRole2.resize();
    }

    /**
     * 下一页按钮
     */
    public void nextPage() {
        int charNum = describeMyRole2.getCharNum();
        if (curPosition + charNum >= maxCharLength) {
            if (index >= relation_title.size() - 1) {
                //最后一章，返回
                return;
            } else {
                if (tips.get(index + 1) != null) {
                    tipDialogshow(tips.get(index + 1));   //显示提示dialog
                    if(!bianhuan){
                        charNum = 0;
                    }
                } else {
                    positions.get(index).add(curPosition);  //把每一章最后一页加入
                    index++;   //进入下一章
                    curPosition = 0; //重置起始位置
                    maxCharLength = des_cribe.get(index).length();//重置最大长度
                    contentMyRole2.setText(relation_title.get(index));//设置当前title
                    loadPage(curPosition); //加载一页
                    charNum = 0;
                }
            }
        }
        if (bianhuan){
            bianhuan = false;
        }else {
            positions.get(index).add(curPosition);
            curPosition += charNum;
            loadPage(curPosition);
            describeMyRole2.resize();
        }
    }

    @OnClick({R.id.up_page_my_role2, R.id.next_page_my_role2, R.id.back_my_role2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.up_page_my_role2:
                Log.d(TAG, "onViewClicked: 你点击了上一页");
                previewPage();
                break;
            case R.id.next_page_my_role2:
                Log.d(TAG, "onViewClicked: 你点击了下一页");
                nextPage();
                break;
            case R.id.back_my_role2:
                selectBackDialogshow();
                break;
        }
    }
    public void tipDialogshow2(String r2) {
        tipDialog = new TipDialog(this, R.style.MyDialog, r2, new
                TipDialog.LeaveMyDialogListener2() {
                    @Override
                    public void onClick(View view) {
                        switch (view.getId()) {
                            case R.id.Ok:
                                tipDialog.dismiss();
                                Intent intent = new Intent(MyRole2Activity.this, ImformationRoleActivity.class);
                                intent.putExtra("plot_plays", plotBean.plot_plays.get(position1));
                                intent.putExtra("r", r);
                                startActivity(intent);
                                break;
                            case R.id.No:
                                tipDialog.dismiss();
                                break;
                        }
                    }
                });
        tipDialog.setCanceledOnTouchOutside(false);
        tipDialog.show();
    }
}
