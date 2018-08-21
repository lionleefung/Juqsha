package lightcone.com.pack.ad;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.ads.AdChoicesView;
import com.facebook.ads.AdIconView;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;

import java.util.ArrayList;
import java.util.List;

import lightcone.com.pack.R;

/**
 * Created by panjianye on 2018/02/24.
 */
public class FBNativeAdManager {

    public static void inflateAd(NativeAd nativeAd, View adView) {
        if (nativeAd == null) {
            return;
        }

        nativeAd.unregisterView();

        // Add the AdChoices icon
        LinearLayout adChoicesContainer = (LinearLayout) adView.findViewById(R.id.ad_choices_container);
        if (adChoicesContainer != null) {
            AdChoicesView adChoicesView = new AdChoicesView(adView.getContext(), nativeAd, true);
            adChoicesContainer.addView(adChoicesView, 0);
        }

        // Create native UI using the ad metadata.
        AdIconView nativeAdIcon = (AdIconView) adView.findViewById(R.id.native_ad_icon);
        TextView nativeAdTitle = (TextView) adView.findViewById(R.id.native_ad_title);
        MediaView nativeAdMedia = (MediaView) adView.findViewById(R.id.native_ad_media);
        TextView nativeAdSocialContext = (TextView) adView.findViewById(R.id.native_ad_social_context);
        TextView nativeAdBody = (TextView) adView.findViewById(R.id.native_ad_body);
        TextView sponsoredLabel = (TextView) adView.findViewById(R.id.native_ad_sponsored_label);
        Button nativeAdCallToAction = (Button) adView.findViewById(R.id.native_ad_call_to_action);

        // Set the Text.
        if (nativeAdTitle != null) {
            nativeAdTitle.setText(nativeAd.getAdvertiserName());
        }
        if (nativeAdBody != null) {
            nativeAdBody.setText(nativeAd.getAdBodyText());
        }
        if (nativeAdSocialContext != null) {
            nativeAdSocialContext.setText(nativeAd.getAdSocialContext());
        }
        if (nativeAdCallToAction != null) {
            nativeAdCallToAction.setVisibility(nativeAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
            nativeAdCallToAction.setText(nativeAd.getAdCallToAction());
        }
        if (sponsoredLabel != null) {
            sponsoredLabel.setText(nativeAd.getSponsoredTranslation());
        }

        // Create a list of clickable views
        List<View> clickableViews = new ArrayList<>();
        clickableViews.add(nativeAdTitle);
        clickableViews.add(nativeAdCallToAction);

        // Register the Title and CTA button to listen for clicks.
        nativeAd.registerViewForInteraction(adView, nativeAdMedia, nativeAdIcon, clickableViews);
    }
}
