package com.lightcone.ad.popad;

import com.lightcone.ad.helper.InstallHelper;
import com.lightcone.ad.model.AdModel;

import com.lightcone.common.R;
import com.lightcone.googleanalysis.GaManager;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

class PopAdWindow {
	
	private static final float SIZE_RATE = 0.7F;
	private static final float IMAGE_RATE = 960f/590f;
	
	private Context context;
	private View parent;
	private View view;
	private ImageView adImage;
	private ImageView close;
	private AdModel adModel;
	private PopupWindow adWindow;
	
	public PopAdWindow(Context context , View parent , AdModel model){
		this.context = context;
		this.parent = parent;
		this.adModel = model;
		initAdPopupWindow();
	}

	private void initAdPopupWindow(){
		view = LayoutInflater.from(context).inflate(R.layout.ad_popup_view,null);
		this.adImage = (ImageView)view.findViewById(R.id.ad_backgroud);
		adImage.setImageDrawable(adModel.getDrawable());
		this.close = (ImageView)view.findViewById(R.id.close);
		
		int screenWidth  = getWindowManager().getDefaultDisplay().getWidth();      
		int screenHeight = getWindowManager().getDefaultDisplay().getHeight(); 
		
		RelativeLayout layout = (RelativeLayout)view.findViewById(R.id.ad_layout);
		layout.getLayoutParams().width = (int)(screenWidth * SIZE_RATE);
		layout.getLayoutParams().height = (int)(screenWidth * SIZE_RATE * IMAGE_RATE);
		
	//	adWindow = new PopupWindow(view, (int)(screenWidth * SIZE_RATE), (int)(screenHeight * SIZE_RATE) , true);
		adWindow = new PopupWindow(view, screenWidth,screenHeight , true);
		adWindow.setFocusable(true);
		//必须设置背景  
		ColorDrawable dw = new ColorDrawable(Color.BLACK);
		adWindow.setBackgroundDrawable(dw);
		   //设置点击其他地方 就消失  
		  adWindow.setOutsideTouchable(true);
		
		adImage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dismiss();
				InstallHelper.openAppInPlayStore(adModel.getClickUrl());
				
				if (onAdClickListener != null) {
					onAdClickListener.onPopAdWindowClick();
				}
				
				GaManager.sendStatValueByCustomMetric(2, 1);
			}
		});
		
		close.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
				if (onCloseListener != null) {
					onCloseListener.onPopAdWindowClose();
				}
			}
		});
	}
	
	public void show(){
		adWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
		
		GaManager.sendStatValueByCustomMetric(1, 1);

	}
	
	public void dismiss(){
		if(adWindow != null){
			adWindow.dismiss();
		}
	}
	
	
	
	private WindowManager getWindowManager(){
		return (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
	}


	public static class PopAdWindowParams{
		public int index;
		public Drawable bgDrawable;
	}
	
	private OnPopAdWindowCloseListener onCloseListener;
	
	public void setOnPopAdWindowCloseListener(OnPopAdWindowCloseListener onCloseListener) {
		this.onCloseListener = onCloseListener;
	}
	
	private OnPopAdWindowAdClickListener onAdClickListener;
	
	public void setOnPopAdWindowAdClickListener(OnPopAdWindowAdClickListener onClickListener) {
		this.onAdClickListener = onClickListener;
	}

	
}
