package lightcone.com.pack.activity;

import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.lightcone.ad.admob.banner.AdmobBannarActivity;
import com.lightcone.googleanalysis.GaManager;
import com.lightcone.share.ShareBuilder;


import java.io.File;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lightcone.com.pack.R;
import lightcone.com.pack.adapter.JubenAdapter;
import lightcone.com.pack.model.PlotDescribeModel;
import lightcone.com.pack.util.res.AssetUtil;


public class MainActivity extends AdmobBannarActivity {
    private static final String TAG = "MainActivity";
    @BindView(R.id.setting)
    ImageView setting;
    @BindView(R.id.share)
    ImageView share;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.layout_admob_banner_ad)
    RelativeLayout layoutAdmobBannerAd;

    private JubenAdapter jubenAdapter;                  //recyclerview的适配器
    private int textsize;                              //字体大小，用于在各个activity之间传递并设置
    private String language;                          //语言

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        textsize = getIntent().getIntExtra("textsize",16);             //获取从setting里面传来的字体大小，空则默认为16

        Locale locale = Locale.getDefault();                  //获取语言设置
        language = locale.getLanguage();
        initData();                                            //初始化数据
        initView();                                           //初始化布局
        //Log.d(TAG, "onCreate: "+language);

    }

    @OnClick({R.id.setting, R.id.share})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.setting:
                GaManager.sendEvent("主页面","点击设置","hp_settings");             //Gamanager统计
                Intent intent = new Intent(MainActivity.this,SettingActivity.class);       //打开设置页面
                intent.putExtra("textsizeback",textsize);                                           //返回字号大小
                startActivity(intent);
                finish();
                break;
            case R.id.share:
                GaManager.sendEvent("主页面","点击分享","hp_share");
               // String r = getApplicationContext().getResources().getString(R.string.share_shouye);
                //String r = getApplicationContext().getResources().getString(R.string.share_shouye);
//                Bitmap shareBitmap = BitmapUtil.decodeBitmapFromAssets("plot/bg_pic/1.jpg", 300, 300);
//                ShareBuilder shareBuilder = new ShareBuilder(this);
//                shareBuilder.buildShareText(R.string.share_shouye);
//                shareBuilder.shareImage(shareBitmap);
                ShareBuilder shareBuilder = new ShareBuilder(this,"");                        //分享
                String sharePath = getExternalFilesDir("share") + "/share.png";
                if(language.equals("en")) {           //根据语言不一样分享不同图片
                    AssetUtil.instance.copyFile("plot/bg_pic/英文宣传图.jpg", sharePath,true);
                }else{
                    AssetUtil.instance.copyFile("plot/bg_pic/share_banner.jpg", sharePath,true);
                }
                shareBuilder.buildShareUri(Uri.fromFile(new File(sharePath)));
                shareBuilder.buildShareText(R.string.share_shouye);
                shareBuilder.share();
                break;
        }
    }
    private void initData(){  //初始化数据
    }

    private void initView(){
        jubenAdapter = new JubenAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);        //不改变宽高
        recyclerView.setAdapter(jubenAdapter);
        if(language.equals("en")) {   //根据语言不一样传入不同的数据
            jubenAdapter.setData(PlotDescribeModel.getInstance().getPlotBeanList().subList(15,19), textsize);//15-19，不包含19
        }else{
            jubenAdapter.setData(PlotDescribeModel.getInstance().getPlotBeanList().subList(0, 15), textsize);
        }
    }

}
