package com.lightcone.ad.helper.timer;

import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.lightcone.ad.helper.Callback;

import android.os.Handler;

public class Timer {
	
	private static final long TIMER_LOOP_TIME = 10 * 1000;//10秒

	private static ExecutorService executors = Executors.newSingleThreadExecutor();
	private static Handler handler = new Handler();
	private static Vector<Runnable> runnableVector = new Vector<>();
	static {
		executors.execute(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(TIMER_LOOP_TIME);
						for (Runnable runnable : runnableVector) {
							runnable.run();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
	}
	
	
	private Callback<Integer> callback;
	private long period;
	
	private long lastCallTime;
	private int callCount;
	private boolean isRun;
	
	/**
	 * banner专用,保证period为10秒整数倍就好啦
	 * @param callback
	 * @param delay
	 * @param period
	 */
	public Timer(Callback<Integer> callback, long delay, long period) {
		this.callback = callback;
		this.period = period;
		this.lastCallTime = System.currentTimeMillis() - period + delay;
		callCount = 0;
		isRun = false;
	}
	
	public void onResume() {
		if (!isRun) {
			runnableVector.add(onRunnable);
			isRun = true;
		}
	}
	
	public void onPause() {
		if (isRun) {
			runnableVector.remove(onRunnable);
			isRun = false;
		}
	}
	
	public void onDestroy() {
		onPause();
		onRunnable = null;
		callback = null;
	}
	
	public boolean isRunning() {
		return isRun;
	}
	
	private Runnable onRunnable = new Runnable() {
		@Override
		public void run() {
			long now = System.currentTimeMillis();
			if (now - lastCallTime >= period) {
				handler.post(new Runnable() {
					@Override
					public void run() {
						if (callback != null) {
							callback.onCallback(++callCount);
						}
					}
				});
				lastCallTime = now;
			}
		}
	};
	
}
