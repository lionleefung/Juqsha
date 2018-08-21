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
import android.widget.Toast;

import com.lightcone.ad.admob.banner.AdmobBannarActivity;

import java.util.ArrayList;
import java.util.List;

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

public class MyRoleActivity extends AdmobBannarActivity {
    private static final String TAG = "MyRoleActivity";

    @BindView(R.id.back_my_role)
    ImageView backMyRole;
    @BindView(R.id.title_my_role)
    TextView titleMyRole;
    @BindView(R.id.name_my_role)
    TextView nameMyRole;
    @BindView(R.id.recyclerview_my_role)
    RecyclerView recyclerviewMyRole;
    @BindView(R.id.content_my_role)
    TextView contentMyRole;
    @BindView(R.id.describe_my_role)
    TextView describeMyRole;
    @BindView(R.id.up_page_my_role)
    Button upPageMyRole;
    @BindView(R.id.next_page_my_role)
    Button nextPageMyRole;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_role);
        ButterKnife.bind(this);
        initData();
        initView();
        for(Relation relation :role.relations){
            relation_title.add(relation.relationtitle);
            des_cribe.add(relation.describe);
            tips.add(relation.tip);
        }
    }

    private void initData() {
        plotBean = (PlotBean) getIntent().getSerializableExtra("plotBean3");
        role = (Role) getIntent().getSerializableExtra("role");
        for (Play play : plotBean.plot_plays) {
            name_small.add(play.label);
        }
    }

    private void initView() {
        if( plotBean.plot_id <= 15) {
            titleMyRole.setText((plotBean.plot_name + " (" + plotBean.plot_pernum + "人" + (plotBean.plot_type == 0 ? "封闭": "开放") + ")"));
            nameMyRole.setText("我的角色：" + role.name);
        }else{
            titleMyRole.setText((plotBean.plot_name + " (" + plotBean.plot_pernum + " characters" + ")"));
            nameMyRole.setText("My character：" + role.name);
        }
        contentMyRole.setText(role.relations.get(0).relationtitle);
        describeMyRole.setText(role.relations.get(0).describe);
        r = getIntent().getIntExtra("textsizerole",16);
        describeMyRole.setTextSize(TypedValue.COMPLEX_UNIT_DIP ,r);
        describeMyRole.setMovementMethod(ScrollingMovementMethod.getInstance());

        final SmallRulerAdapter smallRulerAdapter = new SmallRulerAdapter(name_small);
        final LinearLayoutManager linearLayout = new LinearLayoutManager(this, LinearLayout.HORIZONTAL, false);
        recyclerviewMyRole.setLayoutManager(linearLayout);
        recyclerviewMyRole.setAdapter(smallRulerAdapter);
        smallRulerAdapter.setOnItemClickListener(new OnItemClickedListener() {
            @Override
            public void onItemClick(View view, int position) {
                //Toast.makeText(getApplicationContext(), "你点击了" + position, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MyRoleActivity.this, ImformationRoleActivity.class);
                intent.putExtra("plot_plays", plotBean.plot_plays.get(position));
                intent.putExtra("r",r);
                startActivity(intent);
            }
        });
    }


    @OnClick({R.id.back_my_role,R.id.up_page_my_role,R.id.next_page_my_role})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_my_role:
                //Log.d("MyRoleActivity", "你点击了");
                selectBackDialogshow();
                break;
            case R.id.up_page_my_role:
                if(index <= 0){
                    //Toast.makeText(MyRoleActivity.this, "没有上一页了", Toast.LENGTH_SHORT).show();
                }else{
                    index --;
                    contentMyRole.setText(relation_title.get(index));
                    describeMyRole.setText(des_cribe.get(index));
                }
                break;
            case R.id.next_page_my_role:
                if (index >= relation_title.size() - 1){
                    //Toast.makeText(MyRoleActivity.this, "没有下一页了", Toast.LENGTH_SHORT).show();
                }else{
                    if(tips.get(index + 1 ) != null){
                        tipDialogshow(tips.get(index +1 ));
                    }else {
                        index++;
                        contentMyRole.setText(relation_title.get(index));
                        describeMyRole.setText(des_cribe.get(index));
                    }
                }
                break;
        }

    }

    public void selectBackDialogshow() {
        backSelectDialog = new BackSelectDialog(this, R.style.MyDialog, new
                BackSelectDialog.LeaveMyDialogListener2() {
                    @Override
                    public void onClick(View view) {
                        switch (view.getId()) {
                            case R.id.yes_back_dialog:
                                backSelectDialog.dismiss();
                                finish();
                                break;
                            case R.id.no_back_dialog:
                                backSelectDialog.dismiss();
                                break;
                        }
                    }
                });
        backSelectDialog.setCanceledOnTouchOutside(false);
        backSelectDialog.show();
    }

    public void tipDialogshow(String r){
        tipDialog = new TipDialog(this,R.style.MyDialog,r,new
        TipDialog.LeaveMyDialogListener2(){
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.Ok:
                        tipDialog.dismiss();
                        index++;
                        contentMyRole.setText(relation_title.get(index));
                        describeMyRole.setText(des_cribe.get(index));
                        break;
                    case R.id.No:
                        contentMyRole.setText(relation_title.get(index));
                        describeMyRole.setText(des_cribe.get(index));
                        tipDialog.dismiss();
                        break;
                }
            }
        });
        tipDialog.setCanceledOnTouchOutside(false);
        tipDialog.show();
    }

}
