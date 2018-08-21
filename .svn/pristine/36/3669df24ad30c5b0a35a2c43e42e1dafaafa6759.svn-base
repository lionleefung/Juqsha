package lightcone.com.pack.activity;

import android.content.Intent;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lightcone.ad.admob.banner.AdmobBannarActivity;
import com.lightcone.feedback.FeedbackManager;
import com.lightcone.googleanalysis.GaManager;
import com.lightcone.rate.LikePopupWindow;
import com.lightcone.share.ShareBuilder;

import java.io.File;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lightcone.com.pack.R;
import lightcone.com.pack.util.res.AssetUtil;

public class SettingActivity extends AdmobBannarActivity {
    private static final String TAG = "SettingActivity";

    @BindView(R.id.back_setting)
    ImageView backSetting;
    @BindView(R.id.ping_xing)
    LinearLayout pingXing;
    @BindView(R.id.fen_xiang)
    LinearLayout fenXiang;
    @BindView(R.id.fan_kui)
    LinearLayout fanKui;
    @BindView(R.id.degrade)
    ImageView degrade;
    @BindView(R.id.text)
    TextView text;
    @BindView(R.id.upgrade)
    ImageView upgrade;

    private int textsize = 16;
    private int i = 2;
    private String language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        Locale locale = Locale.getDefault();
        language = locale.getLanguage();
        int r = getIntent().getIntExtra("textsizeback",16);
        textsize2i(r);
        text.setTextSize(TypedValue.COMPLEX_UNIT_DIP,r);
    }

    @OnClick({R.id.back_setting, R.id.ping_xing, R.id.fen_xiang, R.id.fan_kui, R.id.degrade, R.id.upgrade})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_setting:
                i2textsize();
                Intent intent = new Intent(SettingActivity.this,MainActivity.class);
                intent.putExtra("textsize",textsize);
                startActivity(intent);
                finish();
                break;
            case R.id.ping_xing:
                new LikePopupWindow(this).show(this.getWindow().getDecorView());
                break;
            case R.id.fen_xiang:
                ShareBuilder shareBuilder = new ShareBuilder(this,"");
                String sharePath = getExternalFilesDir("share") + "/share.png";
                if(language.equals("en")) {
                    AssetUtil.instance.copyFile("plot/bg_pic/英文宣传图.jpg", sharePath,true);
                }else{
                    AssetUtil.instance.copyFile("plot/bg_pic/share_banner.jpg", sharePath,true);
                }
                shareBuilder.buildShareUri(Uri.fromFile(new File(sharePath)));
                shareBuilder.buildShareText(R.string.share_shouye);
                shareBuilder.share();
                break;
            case R.id.fan_kui:
                FeedbackManager.getInstance().showFeedbackActivity(this);
                break;
            case R.id.degrade:
                GaManager.sendEvent("设置页","减小字号"," settings_sizeminus");
                if(i <= 1){
                    //Toast.makeText(getApplicationContext(),"已经最小了呢"+String.valueOf(textsize),Toast.LENGTH_SHORT).show();
                }else{
                    i --;
                }
                i2textsize();
                text.setTextSize(TypedValue.COMPLEX_UNIT_DIP,textsize);
                break;
            case R.id.upgrade:
                GaManager.sendEvent("设置页","增加字号","settings_sizeplus");
                if (i >= 5){
                    //Toast.makeText(getApplicationContext(),"已经最大了呢"+String.valueOf(textsize),Toast.LENGTH_SHORT).show();
                }else{
                    i ++ ;
                }
                i2textsize();
                text.setTextSize(TypedValue.COMPLEX_UNIT_DIP,textsize);
                break;
        }
    }

    public void i2textsize(){
        switch (i){
            case 1 :textsize = 14;
            break;
            case 2 :textsize = 16;
                break;
            case 3 :textsize = 18;
                break;
            case 4 :textsize = 20;
                break;
            case 5 :textsize = 22;
                break;
                default:
        }
    }
    public void textsize2i(int textsizeback){
        switch (textsizeback){
            case 14 :i = 1;
                break;
            case 16 :i = 2;
                break;
            case 18 :i = 3;
                break;
            case 20 :i = 4;
                break;
            case 22 :i = 5;
                break;
            default:
        }
    }
}
