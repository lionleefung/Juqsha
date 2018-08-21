package com.lightcone.cdn;

import android.support.annotation.Nullable;

/**
 * Created by yueweiwei on 2018/4/19.
 */

public interface OnCdnVersionConfigLoadCompleteListener {

    void onComplete(boolean success, @Nullable VersionConfig versionConfig);

}
