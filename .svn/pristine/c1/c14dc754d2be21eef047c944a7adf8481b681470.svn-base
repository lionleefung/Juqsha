package lightcone.com.pack.util.sp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;
import java.util.Set;

public class SpWrapper {

	private Context context;
	private String fileName;

	SpWrapper(Context context, String fileName) {
		this.context = context;
		this.fileName = fileName;
	}

	private SharedPreferences getSharedPreferences() {
		return context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
	}

	/*************** put ***************/
	public void putInt(String key, Integer value) {
		getSharedPreferences().edit().putInt(key, value).apply();
	}

	public void putString(String key, String value) {
		getSharedPreferences().edit().putString(key, value).apply();
	}

	@SuppressLint("NewApi")
	public void putStringSet(String key, Set<String> values) {
		getSharedPreferences().edit().putStringSet(key, values).apply();
	}

	public void putBoolean(String key, boolean value) {
		getSharedPreferences().edit().putBoolean(key, value).apply();
	}

	public void putLong(String key, long value) {
		getSharedPreferences().edit().putLong(key, value).apply();
	}

	public void putFloat(String key, float value) {
		getSharedPreferences().edit().putFloat(key, value).apply();
	}

	/*************** get ***************/
	public Map<String, ?> getAll() {
		return getSharedPreferences().getAll();
	}

	public int getInt(String key, int defValue) {
		return getSharedPreferences().getInt(key, defValue);
	}

	public String getString(String key, String defValue) {
		return getSharedPreferences().getString(key, defValue);
	}

	public boolean getBoolean(String key, boolean defValue) {
		return getSharedPreferences().getBoolean(key, defValue);
	}

	public long getLong(String key, long defValue) {
		return getSharedPreferences().getLong(key, defValue);
	}

	public float getFloat(String key, float defValue) {
		return getSharedPreferences().getFloat(key, defValue);
	}

	@SuppressLint("NewApi")
	public Set<String> getStringSet(String key, Set<String> defValues) {
		return getSharedPreferences().getStringSet(key, defValues);
	}

	/*************** clear remove contains ***************/
	public void remove(String key) {
		getSharedPreferences().edit().remove(key).apply();
	}

	public void clear() {
		getSharedPreferences().edit().clear().apply();
	}

	public boolean contains(String key) {
		return getSharedPreferences().contains(key);
	}

}
