package lightcone.com.pack.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lightcone.ad.admob.banner.AdmobBannarActivity;
import com.lightcone.ad.helper.Callback;
import com.lightcone.googleanalysis.GaManager;
import com.lightcone.share.ShareBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lightcone.com.pack.R;
import lightcone.com.pack.ad.AdHelper;
import lightcone.com.pack.adapter.OnItemClickedListener;
import lightcone.com.pack.adapter.RulerAndRoleAdapter;
import lightcone.com.pack.bean.Play;
import lightcone.com.pack.bean.PlotBean;
import lightcone.com.pack.dialog.TipDialog;
import lightcone.com.pack.util.res.AssetUtil;
import lightcone.com.pack.util.res.BitmapUtil;

public class JubenActivity extends AdmobBannarActivity {

    @BindView(R.id.back_juben)
    ImageView backJuben;
    @BindView(R.id.title_juben)
    TextView titleJuben;
    @BindView(R.id.share_juben)
    ImageView shareJuben;
    @BindView(R.id.banner_juben)
    ImageView bannerJuben;
    @BindView(R.id.describe_juben)
    TextView describeJuben;
    @BindView(R.id.fragment_juben)
    RecyclerView fragmentJuben;
    @BindView(R.id.select_juben)
    Button selectJuben;
    @BindView(R.id.layout_admob_banner_ad)
    RelativeLayout layoutAdmobBannerAd;

    private PlotBean plotBean;
    private List<String> name = new ArrayList<>();
    private int textsizejuben;
    private TipDialog tipDialog;
    private int position1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juben);
        GaManager.sendEvent("剧本页","进入","script_enter");
        ButterKnife.bind(this);
        textsizejuben = getIntent().getIntExtra("textsizejuben",16);          //获取字体大小
        initData();
        initView();
    }

    private void initData() {
        plotBean = (PlotBean) getIntent().getSerializableExtra("plotBean");                 //获取传入的剧本数据
        for (Play play : plotBean.plot_plays) {
            name.add(play.label);
        }
    }

    private void initView() {
        if( plotBean.plot_id <= 15) {                           //根据编号不一样设置不同的文本
            titleJuben.setText((plotBean.plot_name + " (" + plotBean.plot_pernum + "人" + (plotBean.plot_type == 0 ? "封闭": "开放") + ")"));
    }else{
        titleJuben.setText((plotBean.plot_name + " (" + plotBean.plot_pernum + " characters" + ")"));
    }

        Glide.with(this.getApplicationContext())                //用Glide加载图片
                .load(plotBean.getImage())
                .into(bannerJuben);
        describeJuben.setText(plotBean.plot_describe);          //设置描述

        final RulerAndRoleAdapter rulerAndRoleAdapter = new RulerAndRoleAdapter(name);            //recyclerview的设配器
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(JubenActivity.this, 2);
        fragmentJuben.setLayoutManager(gridLayoutManager);
        fragmentJuben.setAdapter(rulerAndRoleAdapter);

        rulerAndRoleAdapter.setOnItemClickListener(new OnItemClickedListener() {         //设置点击事件
            @Override
            public void onItemClick(View view, int position) {
                position1 = position;
                if(plotBean.plot_plays.get(position).plots.get(0).titletip != null){              //根据提示的有无判断是否需要提示
                    tipDialogshow(plotBean.plot_plays.get(position).plots.get(0).titletip);
                }else {
                    //Toast.makeText(getApplicationContext(), "你点击了" + position, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(JubenActivity.this, ImformationActivity.class); //点击进入详细页
                    intent.putExtra("plot_plays", plotBean.plot_plays.get(position));
                    intent.putExtra("textsizeimformation", textsizejuben);                 //传入字号
                    startActivity(intent);
                }
            }
        });
    }

    @OnClick({R.id.back_juben, R.id.share_juben, R.id.layout_admob_banner_ad, R.id.select_juben})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_juben:  //返回弹出插屏广告
                AdHelper.popAd(titleJuben, new Callback<Boolean>() {
                    @Override
                    public void onCallback(Boolean response) {
                        finish();
                    }
                });
                GaManager.sendEvent("剧本页","点击返回的次数","script_back");
                break;
            case R.id.share_juben:   //分享
                GaManager.sendEvent("剧本页","点击分享某一剧本","script_" +plotBean.plot_name +" _share");
                GaManager.sendEvent("剧本页","点击分享","script_share");
//                String r = getApplicationContext().getResources().getString(R.string.share_shouye);
//                Bitmap shareBitmap = BitmapUtil.decodeBitmapFromAssets("plot/bg_pic/1.jpg", 300, 300);
//                ShareBuilder shareBuilder = new ShareBuilder(this);
//                shareBuilder.buildShareText(R.string.share_shouye);
//                shareBuilder.shareImage(shareBitmap);
                int i = plotBean.plot_id;
                ShareBuilder shareBuilder = new ShareBuilder(this,"【" +plotBean.plot_name + "】");
                String sharePath = getExternalFilesDir("share") + "/share.png";
                AssetUtil.instance.copyFile("plot/bg_pic/" + plotBean.plot_pic,sharePath,true);
                shareBuilder.buildShareUri(Uri.fromFile(new File(sharePath)));
                if(i == 16 ){       //根据id不同分享不同的文本
                    shareBuilder.buildShareText(R.string.share_1);
                }else if(i == 17){
                    shareBuilder.buildShareText(R.string.share_2);
                }else if(i == 18) {
                    shareBuilder.buildShareText(R.string.share_3);
                }else if(i == 19) {
                    shareBuilder.buildShareText(R.string.share_4);
                }else{
                    shareBuilder.buildShareText(R.string.share_shouye);
                }
                shareBuilder.share();
                break;
            case R.id.layout_admob_banner_ad:   //广告
                break;
            case R.id.select_juben:
                GaManager.sendEvent("剧本页","点击选择角色","script__character");
                GaManager.sendEvent("剧本页","点击选择角色","script_" +plotBean.plot_name +" _character");
                Intent intent = new Intent(this, RoleActivity.class);
                intent.putExtra("plotBean1", plotBean);
                intent.putExtra("textsizerole",textsizejuben);                             //传入字号
                startActivity(intent);
                break;
        }
    }
    public void tipDialogshow(String r) {                                                         //提示框
        tipDialog = new TipDialog(this, R.style.MyDialog, r, new
                TipDialog.LeaveMyDialogListener2() {
                    @Override
                    public void onClick(View view) {
                        switch (view.getId()) {
                            case R.id.Ok:
                                tipDialog.dismiss();
                                Intent intent = new Intent(JubenActivity.this, ImformationActivity.class);
                                intent.putExtra("plot_plays", plotBean.plot_plays.get(position1));
                                intent.putExtra("textsizeimformation", textsizejuben);
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
