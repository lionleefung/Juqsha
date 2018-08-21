package lightcone.com.pack.util.res;

import android.content.Context;
import android.widget.Toast;

import lightcone.com.pack.MyApplication;

/**
 * toast可以在非主线弹.
 * 
 * @author 然而toast里面handler初始化一定要有Looper.prepare(),Looper.loop();而loop()会导致线程无法退出.故建议toast在主线程弹出最好
 * @author pimeng
 * 
 */
public class ToastUtil {

	private static final Context context = MyApplication.appContext;

	public static final ToastUtil instance = new ToastUtil();

	private ToastUtil() {// toast显示

	}

	private Toast shortToast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
	private Toast longToast = Toast.makeText(context, "", Toast.LENGTH_LONG);

	public void init() {
	}

	public void show(CharSequence message) {
		shortToast.setText(message);
		shortToast.show();
	}

	public void show(int message) {
		shortToast.setText(message);
		shortToast.show();
	}

	public void showLong(CharSequence message) {
		longToast.setText(message);
		longToast.show();
	}

	public void showLong(int message) {
		longToast.setText(message);
		longToast.show();
	}

	public Toast getNewToast() {
		return new Toast(context);
	}

}
