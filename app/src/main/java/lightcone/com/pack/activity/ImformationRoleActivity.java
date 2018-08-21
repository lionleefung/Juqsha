package lightcone.com.pack.activity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lightcone.ad.admob.banner.AdmobBannarActivity;

import java.util.ArrayList;
import java.util.List;

import lightcone.com.pack.R;
import lightcone.com.pack.bean.Play;
import lightcone.com.pack.bean.Plot;
import lightcone.com.pack.dialog.ShowImageDialog;
import lightcone.com.pack.dialog.TipDialog;
import lightcone.com.pack.view.ReadView;

public class ImformationRoleActivity extends AdmobBannarActivity{
    private static final String TAG = "ImformationActivity";
    private ImageView imformation_back;
    private TextView title;
    private ReadView imformation;
    private Button uppage;
    private Button nextpage;
    private Play play;
    private ImageView imagename;

    private List<String> contentandimage = new ArrayList<>();
    private List<String> contenttitle = new ArrayList<>();
    int index = 0;
    private List<List<Integer>> positions = new ArrayList<>();//缓存前几页开始位置
    private int maxCharLength;
    private int curPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_imformation);

        play =(Play) getIntent().getSerializableExtra("plot_plays");
        imformation_back = findViewById(R.id.imformation_back);
        for(final Plot plot :play.plots){
            if(plot.contents != null){
                contentandimage.add(plot.contents);
                contenttitle.add(plot.title);
                positions.add(new ArrayList<Integer>());
            }
            if(plot.imgName != null){
                contentandimage.add(plot.getInsideImage());
                contenttitle.add(plot.title);
                positions.add(new ArrayList<Integer>());
            }
        }
        title = findViewById(R.id.imformation_title);
        title.setText(play.plots.get(0).title);
        imformation = findViewById(R.id.imformation);
        int r = getIntent().getIntExtra("r",16);
        imagename = findViewById(R.id.imagename);
        imagename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                dialog.show();
                new ShowImageDialog(ImformationRoleActivity.this, contentandimage.get(index)).show();
            }
        });
        imformation.setTextSize(TypedValue.COMPLEX_UNIT_DIP,r);
//        if(play.plots.get(0).contents == null && play.plots.get(0).imgName != null){
//            imformation.setVisibility(View.GONE);
//            imagename.setVisibility(View.VISIBLE);
//            Glide.with(getApplicationContext())
//                    .load(play.plots.get(0).getInsideImage())
//                    .into(imagename);
//        }else {
//            imagename.setVisibility(View.GONE);
//            imformation.setVisibility(View.VISIBLE);
//            imformation.setText(play.plots.get(0).contents);
//        }
//        imformation.setMovementMethod(ScrollingMovementMethod.getInstance());
        loadPage(0);
        maxCharLength = contentandimage.get(0).length();   //初始长度
        uppage = findViewById(R.id.up_page_dialog);
        nextpage = findViewById(R.id.next_page_dialog);
//        if(contentandimage.size() <= 1  && maxCharLength <= 200){
//            uppage.setVisibility(View.INVISIBLE);
//            nextpage.setVisibility(View.INVISIBLE);
//    }
        nextpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextPage();
            }
        });
        uppage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                previewPage();
            }
        });
        imformation_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        imformation.post(new Runnable() {
            @Override
            public void run() {
                int charNum;
                if(imformation.getVisibility() == View.VISIBLE) {
                    charNum = imformation.getText().toString().trim().length();
                }else{
                    charNum = maxCharLength;
                }
                if(contentandimage.size() <= 1 && charNum >= maxCharLength){    //如果只有一页，则不显示按钮
                    uppage.setVisibility(View.INVISIBLE);
                    nextpage.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void loadPage(int position) {
        String text = contentandimage.get(index).substring(contentandimage.get(index).length() - 2, contentandimage.get(index).length());
        if (text.equals("pg") || text.equals("ng")) {
            imformation.setVisibility(View.GONE);
            imagename.setVisibility(View.VISIBLE);
            Glide.with(getApplicationContext())
                    .load(contentandimage.get(index))
                    .into(imagename);
        } else {
            title.setText(contenttitle.get(index));
            imagename.setVisibility(View.GONE);
            imformation.setVisibility(View.VISIBLE);
            imformation.setText(contentandimage.get(index).substring(position));
        }
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
        title.setText(contenttitle.get(index));
        maxCharLength = contentandimage.get(index).length();
        curPosition = positions.get(index).get(lastIndex);
        positions.get(index).remove(lastIndex);
        loadPage(curPosition);
        //imformation.resize();
    }

    /**
     * 下一页按钮
     */
    public void nextPage() {
        int charNum;
        if(imformation.getVisibility() == View.VISIBLE) {
            charNum = imformation.getCharNum();
        }else{
            charNum = maxCharLength;
        }
        if (curPosition + charNum >= maxCharLength) {
            if (index >= contenttitle.size() - 1) {
                //最后一章，返回
                return;
            } else {
                positions.get(index).add(curPosition);  //把每一章最后一页加入
                index++;   //进入下一章
                curPosition = 0; //重置起始位置
                maxCharLength = contentandimage.get(index).length();//重置最大长度
                title.setText(contenttitle.get(index));//设置当前title
                loadPage(curPosition); //加载一页
                charNum = 0;
            }
        }
        positions.get(index).add(curPosition);
        curPosition += charNum;
        loadPage(curPosition);
        //imformation.resize();
    }

}
