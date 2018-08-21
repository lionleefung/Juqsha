package com.lightcone.reinforce;

import com.lightcone.utils.FileUtil;

import java.io.InputStream;


public class CryptUtil {

	public static String jiemi(InputStream fis) {
		byte A = (byte) 69;
		byte B = (byte) 38;
		byte M = (byte) 105;
		byte key = (byte) 58;
		byte[] data = null;
		data = FileUtil.readFileBytes(fis);
		long length = data.length;
		for (int i = 0; i < length; i++) {
			key = (byte) ((A * key + B + M) % M);
			data[i] ^= key;
		}
		return new String(data);
	}

}
