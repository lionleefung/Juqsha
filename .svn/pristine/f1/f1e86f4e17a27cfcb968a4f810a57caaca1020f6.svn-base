package lightcone.com.pack.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.NativeAppInstallAd;
import com.google.android.gms.ads.formats.NativeAppInstallAdView;
import com.lightcone.AdLib;
import com.lightcone.googleanalysis.GaManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import lightcone.com.pack.R;
import lightcone.com.pack.activity.JubenActivity;
import lightcone.com.pack.ad.FBNativeAdManager;
import lightcone.com.pack.ad.GMSNativeAdvanceManager;
import lightcone.com.pack.bean.JubenBean;
import lightcone.com.pack.bean.PlotBean;
import lightcone.com.pack.model.PlotDescribeModel;

public class JubenAdapter extends RecyclerView.Adapter {
    private static final String TAG = "JubenAdapter";

    private Context context;
    private List<PlotBean> data = new ArrayList<>();
    private int textsizejuben;   //字号

    public JubenAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<PlotBean> data,int textsize) {     //设置数据，如果position是5的倍数则设为一个null
        textsizejuben = textsize;
        if (data == null || data.size() <= 0) return;
        for (int i = 0; i < data.size(); i++) {
            this.data.add(data.get(i));
            if ((i + 1) % 5 == 0) this.data.add(null);
        }
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {             //设置viewholder，不同类设置不同
        if (viewType == 1) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.native_small_cell, parent, false);
            return new AdHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_juben, parent, false);
            final ViewHolder holder = new ViewHolder(view);
            return holder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == 1) {
            AdHolder adHolder = (AdHolder) holder;
            adHolder.loadAd(position);
            return;
        } else {
            final ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.bindData(position);
        }

    }

    @Override
    public int getItemViewType(int position) {
        return data.get(position) == null ? 1 : 0;
    }    //根据数据判断类型

    @Override
    public int getItemCount() {
        return data.size();
    }

    class AdHolder extends RecyclerView.ViewHolder {
        ViewGroup ga_ad_view;

        AdHolder(View itemView) {
            super(itemView);
            ga_ad_view = itemView.findViewById(R.id.ga_ad_view);
            ga_ad_view.setVisibility(View.GONE);
            ga_ad_view.findViewById(R.id.ga_ad_bg).setBackgroundResource(R.drawable.list_bg2);
        }

        void loadAd(final int position) {
            Log.e("TAG", "Load Ad In Item" + position);
            AdLoader.Builder builder = new AdLoader.Builder(context, AdLib.admobNative);
            builder.forAppInstallAd(new NativeAppInstallAd.OnAppInstallAdLoadedListener() {
                @Override
                public void onAppInstallAdLoaded(NativeAppInstallAd ad) {
                    ga_ad_view.setVisibility(View.VISIBLE);
                    NativeAppInstallAdView adView = (NativeAppInstallAdView) ga_ad_view;
                    GMSNativeAdvanceManager.populateAppInstallAdView(ad, adView);
                }
            });
            VideoOptions videoOptions = new VideoOptions.Builder()
                    .setStartMuted(false)
                    .build();
            NativeAdOptions adOptions = new NativeAdOptions.Builder()
                    .setVideoOptions(videoOptions)
                    .build();
            builder.withNativeAdOptions(adOptions);
            AdLoader adLoader = builder.withAdListener(new AdListener() {
                @Override
                public void onAdFailedToLoad(int errorCode) {
                    Log.e(TAG, "Failed to load native ad: " + errorCode);
                }
            }).build();
            adLoader.loadAd(new AdRequest.Builder().build());
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.juben_title)
        TextView title;
        @BindView(R.id.juben_picture)
        ImageView picture;
        @BindView(R.id.juben_describe)
        TextView describe;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bindData(final int position) {
            final PlotBean jubenBean = data.get(position);
            if (jubenBean == null) return;
            if( jubenBean.plot_id <= 15) {
                title.setText((jubenBean.plot_name + " (" + jubenBean.plot_pernum + "人" + (jubenBean.plot_type == 0 ? context.getString(R.string.type) : "开放") + ")"));
            }else{
                title.setText((jubenBean.plot_name + " (" + jubenBean.plot_pernum + " characters" + ")"));
            }
            describe.setText(jubenBean.plot_describe);
            Glide.with(context)
                    .load(jubenBean.getImage())
                    .into(picture);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Log.e("TAG","你点击了"+String.valueOf(position));
                    GaManager.sendEvent("剧本页","进入某一剧本","script_" + jubenBean.plot_name +"enter");
                    Intent intent = new Intent(context, JubenActivity.class);
                    intent.putExtra("plotBean", jubenBean);
                    intent.putExtra("textsizejuben",textsizejuben);
                    context.startActivity(intent);
                }
            });
        }
    }
}
