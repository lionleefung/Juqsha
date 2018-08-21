package com.lightcone.reinforce;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.SecretKeySpec;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Debug;
import android.util.Base64;



public class RogueKiller {
	static {
		if (Debug.isDebuggerConnected()) {
			throw new RuntimeException();
		}
	}

    /**
     * 在MyApplication的onCreate()调用该函数即可.
     *
     * @param context
     * @param online             true表示上线,调用张丹的RogueKiller加密；false表示开发阶段,未调用RogueKiller
     */
    public static void init(Context context, boolean online) {
        if (online) {
            RogueKiller.touch(context, null);
            RogueKiller.untouch(context, null);
        }
    }

	public static String touch(Context context, String text) {
		String rs = null;
		check(context);

		if (null != text) {
			rs = SecurityHelper.getInstance().encrypt(text);
		}

		return rs;
	}

	public static String untouch(Context context, String text) {
		String rs = null;
		check(context);

		if (null != text) {
			rs = SecurityHelper.getInstance().decrypt(text);
		}

		return rs;
	}

	private static void check(Context context) {
		checkDebug(context);
		checkSignature(context);
	}

	private static void checkDebug(Context context) {
		if (null != context) {
			if (0 != (context.getApplicationContext().getApplicationInfo().flags &= ApplicationInfo.FLAG_DEBUGGABLE)) {
				throw new RuntimeException();
			}
		}
	}

	private static void checkSignature(Context context) {
		if (null != context) {
			if (!"eNKBXU2Mb8D89sxLHKEbThXrz/8=".equals(getSignature(context))) {
				throw new RuntimeException();
			}
		}
	}

	private static String getSignature(Context context) {
		String rs = null;

		if (null != context) {
			try {
				PackageInfo packageInfo = context.getApplicationContext().getPackageManager()
						.getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
				MessageDigest md = MessageDigest.getInstance("SHA");
				md.update(packageInfo.signatures[0].toByteArray());
				rs = Base64.encodeToString(md.digest(), Base64.DEFAULT).trim();
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		}

		return rs;
	}

	protected static class SecurityHelper {
		public final static String ENCODING = "UTF-8";

		private final static String ENCRYPTION_AES = "AES";
		private final static String KEY_AES = "Wh!tY#uCanDoI$Do";
		private final static String TRANSFORMATION_AES = "AES/ECB/PKCS5Padding";
		private final static String ALGORITHM_AES = "AES";
		private final static String SEPARATOR_CIPHER_TEXT = "-";

		private SecretKeySpec key;
		private Cipher cipher;
		private String encoding;

		private static SecurityHelper instance;

		public static SecurityHelper getInstance() {
			if (null == instance) {
				instance = new SecurityHelper();
				instance.init(ENCRYPTION_AES, KEY_AES, ENCODING);
			}

			return instance;
		}

		private SecurityHelper() {
			this.encoding = ENCODING;
		}

		public void setEncoding(String encoding) {
			if (null == encoding) {
				encoding = ENCODING;
			}

			this.encoding = encoding;
		}

		public String getEncoding() {
			return this.encoding;
		}

		public String encrypt(String plainText) {
			String rs = plainText;

			if (null != plainText) {
				try {
					byte[] plainByteArray = plainText.getBytes(this.encoding);
					this.cipher.init(Cipher.ENCRYPT_MODE, this.key);
					byte[] cipherByteArray = cipher.doFinal(plainByteArray);
					String cipherText = this.parseByteToHexStr(cipherByteArray);
					rs = this.formatHexStr(cipherText);
				} catch (UnsupportedEncodingException e) {
				} catch (InvalidKeyException e) {
				} catch (IllegalBlockSizeException e) {
				} catch (BadPaddingException e) {
				} catch (Exception e) {
				}
			}

			return rs;
		}

		public String decrypt(String cipherText) {
			String rs = cipherText;

			if (null != cipherText) {
				try {
					byte[] cipherByteArray = this.parseHexStrToByte(this.parseHexStr(cipherText));
					this.cipher.init(Cipher.DECRYPT_MODE, this.key);
					byte[] plainByteArray = cipher.doFinal(cipherByteArray);
					String plainText = new String(plainByteArray, this.encoding);
					rs = plainText;
				} catch (UnsupportedEncodingException e) {
				} catch (InvalidKeyException e) {
				} catch (IllegalBlockSizeException e) {
				} catch (BadPaddingException e) {
				} catch (Exception e) {
				}
			}

			return rs;
		}

		private void init(String encryptionAlgorithm, String key, String encoding) {
			this.setEncryptionMes(encryptionAlgorithm, key, encoding);
		}

		private void setEncryptionMes(String encryptionAlgorithm, String key, String encoding) {
			try {
				if (ENCRYPTION_AES.equalsIgnoreCase(encryptionAlgorithm)) {
					this.key = new SecretKeySpec(key.getBytes(this.encoding), ALGORITHM_AES);
					this.cipher = Cipher.getInstance(TRANSFORMATION_AES);
				} else {
					this.key = new SecretKeySpec(key.getBytes(this.encoding), ALGORITHM_AES);
					this.cipher = Cipher.getInstance(TRANSFORMATION_AES);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		private String formatHexStr(String parsedHexStr) {
			String rs = null;

			if (null != parsedHexStr) {
				StringBuilder stringBuilder = new StringBuilder();

				for (int i = 0; i + 2 < parsedHexStr.length(); i += 2) {
					stringBuilder.append(parsedHexStr.substring(i, i + 2));
					stringBuilder.append(SEPARATOR_CIPHER_TEXT);
				}

				int length = parsedHexStr.length();
				stringBuilder.append(parsedHexStr.substring(length - 2, length));
				rs = stringBuilder.toString();
			}

			return rs;
		}

		private String parseHexStr(String formatedHexStr) {
			String rs = null;

			if (null != formatedHexStr) {
				if (-1 == formatedHexStr.indexOf(SEPARATOR_CIPHER_TEXT)) {
					rs = formatedHexStr;
				} else {
					StringBuilder stringBuilder = new StringBuilder();
					String[] temp = formatedHexStr.split(SEPARATOR_CIPHER_TEXT);

					for (int i = 0; i < temp.length; i++) {
						stringBuilder.append(temp[i]);
					}

					rs = stringBuilder.toString();
				}
			}

			return rs;
		}

		private String parseByteToHexStr(byte[] byteArray) {
			String rs = null;

			if (null != byteArray) {
				StringBuilder stringBuilder = new StringBuilder();

				for (int i = 0; i < byteArray.length; i++) {
					String hex = Integer.toHexString(byteArray[i] & 0xFF);

					if (1 == hex.length()) {
						hex = '0' + hex;
					}

					stringBuilder.append(hex.toUpperCase(Locale.ENGLISH));
				}

				rs = stringBuilder.toString();
			}

			return rs;
		}

		private byte[] parseHexStrToByte(String hexStr) {
			byte[] rs = null;

			if (0 != hexStr.length()) {
				rs = new byte[hexStr.length() / 2];

				for (int i = 0; i < hexStr.length() / 2; i++) {
					int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
					int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
					rs[i] = (byte) (high * 16 + low);
				}
			}

			return rs;
		}
	}
}
