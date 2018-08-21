package lightcone.com.pack.util.res;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class StringUtil {

	public static final String EMPTY = "";

	public static boolean isEmpty(String str) {
		if (str == null || str.equals("")) {
			return true;
		}
		return false;
	}

	public static boolean isNumeric(String str) {
		return str.matches("[0-9]*");
	}

	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}

	public static List<String> split(String data, String splitor) {
		List<String> rs = new ArrayList<>();
		if (!isEmpty(data)) {
			String[] sarr = data.split(splitor);
			for (String s : sarr) {
				rs.add(s);
			}
		}
		return rs;
	}
	
	public static List<Integer> splitInt(String data, String splitor) {
		List<Integer> rs = new ArrayList<>();
		if (!isEmpty(data)) {
			String[] sarr = data.split(splitor);
			for (String s : sarr) {
				rs.add(Integer.parseInt(s));
			}
		}
		return rs;
	}
	
	public static List<Long> splitLong(String data, String splitor) {
		List<Long> rs = new ArrayList<>();
		if (!isEmpty(data)) {
			String[] sarr = data.split(splitor);
			for (String s : sarr) {
				rs.add(Long.parseLong(s));
			}
		}
		return rs;
	}
	
	
	public static String join(Collection<?> collection , String splitor){
		if(collection == null || collection.size() == 0){
			return EMPTY;
		}
		StringBuilder sb = new StringBuilder();
		for(Iterator<?> it = collection.iterator(); it.hasNext() ; ){
			Object ob = it.next();
			if (it.hasNext()) {
				sb.append(ob.toString()).append(splitor);
			} else {
				sb.append(ob.toString());
			}
		}
		return sb.toString();
	}

	public static String join(Object[] array) {
		return join(array, null);
	}

	public static String join(Object[] array, char separator) {
		if (array == null) {
			return null;
		}

		return join(array, separator, 0, array.length);
	}

	public static String join(Object[] array, char separator, int startIndex,
                              int endIndex) {
		if (array == null) {
			return null;
		}
		int bufSize = (endIndex - startIndex);
		if (bufSize <= 0) {
			return EMPTY;
		}

		bufSize *= ((array[startIndex] == null ? 16 : array[startIndex]
				.toString().length()) + 1);
		StringBuilder buf = new StringBuilder(bufSize);

		for (int i = startIndex; i < endIndex; i++) {
			if (i > startIndex) {
				buf.append(separator);
			}
			if (array[i] != null) {
				buf.append(array[i]);
			}
		}
		return buf.toString();
	}
	
	 public static String join(Object[] array, String separator) {
	        if (array == null) {
	            return null;
	        }
	        return join(array, separator, 0, array.length);
	    }
	 
	 public static String join(Object[] array, String separator, int startIndex, int endIndex) {
	        if (array == null) {
	            return null;
	        }
	        if (separator == null) {
	            separator = EMPTY;
	        }

	        int bufSize = (endIndex - startIndex);
	        if (bufSize <= 0) {
	            return EMPTY;
	        }

	        bufSize *= ((array[startIndex] == null ? 16 : array[startIndex].toString().length())
	                        + separator.length());

	        StringBuilder buf = new StringBuilder(bufSize);

	        for (int i = startIndex; i < endIndex; i++) {
	            if (i > startIndex) {
	                buf.append(separator);
	            }
	            if (array[i] != null) {
	                buf.append(array[i]);
	            }
	        }
	        return buf.toString();
	    }
}
