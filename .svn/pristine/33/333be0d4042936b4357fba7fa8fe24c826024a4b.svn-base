package lightcone.com.pack.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lightcone.ad.admob.banner.AdmobBannarActivity;
import com.lightcone.googleanalysis.GaManager;
import com.lightcone.share.ShareBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lightcone.com.pack.R;
import lightcone.com.pack.adapter.CharacterAdapter;
import lightcone.com.pack.adapter.OnItemClickedListener;
import lightcone.com.pack.adapter.RulerAndRoleAdapter;
import lightcone.com.pack.bean.Play;
import lightcone.com.pack.bean.PlotBean;
import lightcone.com.pack.bean.Role;
import lightcone.com.pack.dialog.SelectDialog;
import lightcone.com.pack.util.res.AssetUtil;

public class RoleActivity extends AdmobBannarActivity {
    @BindView(R.id.back_role)
    ImageView backRole;
    @BindView(R.id.title_role)
    TextView titleRole;
    @BindView(R.id.recyclerview_role)
    RecyclerView recyclerviewRole;
    @BindView(R.id.invitefriend)
    Button invitefriend;
    @BindView(R.id.changetext)
    TextView changetext;
    private SelectDialog selectDialog;

    private List<String> name = new ArrayList<>();
    private List<String> tip = new ArrayList<>();
    private PlotBean plotBean;
    private int textsizerole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role);
        ButterKnife.bind(this);
        textsizerole = getIntent().getIntExtra("textsizerole",16);
        initData();
        initView();
    }
    private void initData(){
        plotBean = (PlotBean) getIntent().getSerializableExtra("plotBean1");
        for (Role role : plotBean.plot_roles) {
            if(role.rolelabel != null) {
                name.add(role.name);
                tip.add("(" + role.rolelabel + ")");
            }else{
                name.add(role.name );
                tip.add("");
            }
        }
    }
    private void initView() {
        if( plotBean.plot_id <= 15) {
            titleRole.setText((plotBean.plot_name + " (" + plotBean.plot_pernum + "人" + (plotBean.plot_type == 0 ? "封闭": "开放") + ")"));
        }else{
            titleRole.setText((plotBean.plot_name + " (" + plotBean.plot_pernum + " characters" + ")"));
        }
        CharacterAdapter characterAdapter = new CharacterAdapter(name,tip);
        final GridLayoutManager gridLayoutManager=new GridLayoutManager(RoleActivity.this, 2);
        recyclerviewRole.setLayoutManager(gridLayoutManager);
        recyclerviewRole.setAdapter(characterAdapter);

        characterAdapter.setOnItemClickListener(new OnItemClickedListener() {
            @Override
            public void onItemClick(View view, int position) {
                selectDialogshow(position);
            }
        });

    }

    @OnClick({R.id.back_role, R.id.title_role, R.id.recyclerview_role, R.id.invitefriend})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_role:
                finish();
                break;
            case R.id.title_role:
                break;
            case R.id.recyclerview_role:
                break;
            case R.id.invitefriend:
                GaManager.sendEvent("角色页","点击分享好友","character_" +plotBean.plot_name +" _share");
                GaManager.sendEvent("角色页","点击分享","character_share");
                int i = plotBean.plot_id;
                ShareBuilder shareBuilder = new ShareBuilder(this,"【" +plotBean.plot_name + "】");
                String sharePath = getExternalFilesDir("share") + "/share.png";
                AssetUtil.instance.copyFile("plot/bg_pic/" + plotBean.plot_pic,sharePath,true);
                shareBuilder.buildShareUri(Uri.fromFile(new File(sharePath)));
                if(i == 16 ){
                    shareBuilder.buildShareText(R.string.share_1);
                }else if(i == 17){
                    shareBuilder.buildShareText(R.string.share_2);
                }else if(i == 18) {
                    shareBuilder.buildShareText(R.string.share_3);
                } else if(i == 19) {
                    shareBuilder.buildShareText(R.string.share_4);
                }else{
                    shareBuilder.buildShareText(R.string.share_shouye);
                }
                shareBuilder.share();
                break;
        }
    }

    public void selectDialogshow(final int position) {
        selectDialog = new SelectDialog(this, R.style.MyDialog, new
                SelectDialog.LeaveMyDialogListener() {
                    @Override
                    public void onClick(View view) {
                        switch (view.getId()){
                            case R.id.yes_select_dialog:
                                GaManager.sendEvent("角色页","点击剧本角色","character_" +plotBean.plot_name +" _total");
                                GaManager.sendEvent("角色页","点击所有角色","character_total");
                                selectDialog.dismiss();
                                Intent intent = new Intent(RoleActivity.this,MyRole2Activity.class);
                                intent.putExtra("role",plotBean.plot_roles.get(position));
                                intent.putExtra("plotBean3",plotBean);
                                intent.putExtra("textsizerole",textsizerole);
                                startActivity(intent);
                                break;
                        }
                    }
                });
        selectDialog .setCanceledOnTouchOutside(false);
        selectDialog.show();
    }
}
