package com.lightcone.ad.billing;//package org.gzy.adproject.billing;
//
//import org.gzy.adproject.AdManager;
//
//import com.lightcone.billing.Billing;
//import com.lightcone.billing.BillingManager;
//import com.lightcone.billing.OnBillingOverListener;
//import com.lightcone.billing.ProductConfig;
//
//public class BillingHandler {
//
//	private static BillingHandler instance;
//	
//	public static BillingHandler getInstance() {
//		if (instance == null) {
//			instance = new BillingHandler();
//			instance.registAdProduct();
//		}
//		return instance;
//	}
//	
//	public static final String SKU_NO_AD = AdManager.getInstance().getContext().getPackageName() + ".noad";
//	
//	/**
//	 * 付费去广告
//	 */
//	public void removeAdByConsume(Billing billing, final OnRemoveAdListener onRemoveAdListener) {
//		BillingManager.getInstance().buyProduct(billing, SKU_NO_AD, "", new OnBillingOverListener() {
//			@Override
//			public void onBillingOver(boolean res) {
//				onRemoveAdListener.onRemoveAd(res);
//			}
//		});
//	}
//	
//	public boolean hasConsumeAd() {
//		return BillingManager.getInstance().hasConsumeRecord(AdManager.getInstance().getContext(), SKU_NO_AD);
//	}
//
//	private void registAdProduct() {
//		BillingManager.getInstance().registProduct(SKU_NO_AD, ProductConfig.TYPE_NON_CONSUMABLE);
//	}
//}
