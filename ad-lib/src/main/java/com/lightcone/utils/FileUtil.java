package com.lightcone.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import android.graphics.Bitmap;
import android.util.Log;

public class FileUtil {

	/********************* dir *************************/

	public static String mkdir(String dir) {
		File file = new File(dir);
		if (!file.exists()) {
			file.mkdirs();
		}
		return dir;
	}

	public static File createFile(String filePath) throws IOException {
		File file = new File(filePath);
		if (file.getParent() != null && !new File(file.getParent()).exists()) {
			new File(file.getParent()).mkdirs();
		}
		if (!file.exists()) {
			file.createNewFile();
		}
		return file;
	}

	public static void deleteFile(File file) {
		if (file.exists()) {
			if (file.isFile()) {
				file.delete();
			} else if (file.isDirectory()) {
				File files[] = file.listFiles();
				for (int i = 0; i < files.length; i++) {
					deleteFile(files[i]);
				}
			}
			file.delete();
		} else {
			Log.e("Files", "file is not exist！");
		}
	}

	public static List<File> listFiles(File file) {
		List<File> files = new ArrayList<>();
		if (file != null) {
			if (file.isDirectory()) {
				File[] fileArray = file.listFiles();
				if (fileArray != null) {
					for (int i = 0; i < fileArray.length; i++) {
						files.addAll(listFiles(fileArray[i]));
					}
				}
			} else {
				files.add(file);
			}
		}
		return files;
	}

	/********************** write *************************/

	public static void writeStringToFile(String content, String outFile) {
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(outFile));
			writer.write(content);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void writeBytesToFile(byte[] content, String outFile) {
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(outFile);
			out.write(content);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void append(String content, String outFile) {
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(outFile, true));
			bw.write(content);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * @param src
	 *            网络请求传过来的流可能不完整
	 * @param outFile
	 * @param replace
	 * @return 文件完整创建.此处返回值对于网络流具有较大意义.
	 */
	public static boolean writeStreamToFile(InputStream src, String outFile, boolean replace) {
		File file = new File(outFile);
		if (file.exists()) {
			if (replace) {
				file.delete();
			} else {
				return true;
			}
		}
		BufferedInputStream bis = null;
		RandomAccessFile raf = null;
		try {
			file.createNewFile();
			bis = new BufferedInputStream(src);
			raf = new RandomAccessFile(file, "rw");
			raf.seek(0L);
			byte[] b = new byte[1024];
			while (true) {
				int len = bis.read(b, 0, b.length);
				if (len == -1) {
					break;
				}
				raf.write(b, 0, len);
			}
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (raf != null) {
				try {
					raf.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return false;
	}

	public static void writeBitmapToFile(Bitmap image, String outFile) {
		FileOutputStream fos = null;
		try {
			createFile(outFile);
			fos = new FileOutputStream(outFile);
			image.compress(Bitmap.CompressFormat.PNG, 90, fos);
			fos.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/*************************** read **********************************/

	public static String readFile(String filePath) {
		File file = new File(filePath);
		BufferedReader reader = null;
		StringBuilder sb = new StringBuilder();
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {
				sb.append(tempString);
			}
			return sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	public static String readFile(InputStream in) {
		BufferedReader reader = null;
		StringBuilder sb = new StringBuilder();
		try {
			reader = new BufferedReader(new InputStreamReader(in));
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {
				sb.append(tempString);
			}
			return sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	public static byte[] readFileBytes(InputStream in) {
		byte[] bytes = new byte[1024];
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			int ret = -1;
			while ((ret = in.read(bytes)) != -1) {
				bos.write(bytes, 0, ret);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				bos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return bos.toByteArray();
	}

	/**************************** copy *******************************/

	public static void copyFile(String inFile, String outFile) {
		FileInputStream fis = null;
		FileOutputStream fos = null;
		try {
			int byteread = 0;
			fis = new FileInputStream(inFile); // 读入原文件
			fos = new FileOutputStream(outFile);
			byte[] buffer = new byte[1024];
			while ((byteread = fis.read(buffer)) != -1) {
				fos.write(buffer, 0, byteread);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	public static void copyFolder(String srcPath, String dstPath) {

		try {
			(new File(dstPath)).mkdirs(); // 如果文件夹不存在 则建立新文件夹
			File a = new File(srcPath);
			String[] file = a.list();
			File temp = null;
			for (int i = 0; i < file.length; i++) {
				if (srcPath.endsWith(File.separator)) {
					temp = new File(srcPath + file[i]);
				} else {
					temp = new File(srcPath + File.separator + file[i]);
				}

				if (temp.isFile()) {
					FileInputStream input = new FileInputStream(temp);
					FileOutputStream output = new FileOutputStream(dstPath + File.separator
							+ (temp.getName()).toString());
					byte[] b = new byte[1024 * 5];
					int len;
					while ((len = input.read(b)) != -1) {
						output.write(b, 0, len);
					}
					output.flush();
					output.close();
					input.close();
				}
				if (temp.isDirectory()) {// 如果是子文件夹
					copyFolder(srcPath + File.separator + file[i], dstPath + File.separator + file[i]);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*************************** zip *********************************/

	/**
	 * @param inputStream
	 * @param outdir
	 *            目录
	 * @return
	 */
	public static boolean unZip(InputStream inputStream, String outdir) {
		File file = new File(outdir);
		if (!file.exists()) {
			file.mkdirs();
		}
		ZipInputStream zipInputStream = null;
		boolean suc = true;
		try {
			zipInputStream = new ZipInputStream(inputStream);
			ZipEntry zipEntry = zipInputStream.getNextEntry();
			// 使用1Mbuffer
			byte[] buffer = new byte[1024 * 1024];
			int count = 0;
			while (zipEntry != null) {
				if (zipEntry.isDirectory()) {
					// String name = zipEntry.getName();
					// name = name.substring(0, name.length() - 1);
					file = new File(outdir + File.separator + zipEntry.getName());
					file.mkdir();
				} else {
					file = createFile(outdir + File.separator + zipEntry.getName());
					FileOutputStream fileOutputStream = new FileOutputStream(file);
					while ((count = zipInputStream.read(buffer)) > 0) {
						fileOutputStream.write(buffer, 0, count);
					}
					fileOutputStream.close();
				}
				zipEntry = zipInputStream.getNextEntry();
			}
			zipInputStream.close();
		} catch (IOException e) {
			suc = false;
			e.printStackTrace();
		} finally {
			if (zipInputStream != null) {
				try {
					zipInputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return suc;
	}

	/**
	 * zip格式,sourcePath[]为根目录
	 */
	public static boolean zip(String[] sourcePaths, String zipPath) {
		FileOutputStream fos = null;
		ZipOutputStream zos = null;
		try {
			fos = new FileOutputStream(zipPath);
			zos = new ZipOutputStream(fos);
			for (String path : sourcePaths) {
				writeZip(new File(path), "", zos);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (zos != null) {
					zos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	/**
	 * zip格式.sourcePath为根目录
	 */
	public static boolean zip(String sourcePath, String zipPath) {
		FileOutputStream fos = null;
		ZipOutputStream zos = null;
		try {
			fos = new FileOutputStream(zipPath);
			zos = new ZipOutputStream(fos);
			writeZip(new File(sourcePath), "", zos);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (zos != null) {
					zos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	private static void writeZip(File file, String parentPath, ZipOutputStream zos) {
		if (file.exists()) {
			if (file.isDirectory()) {// 处理文件夹
				parentPath += file.getName() + File.separator;// 跨平台注意分隔符"/","\"
				File[] files = file.listFiles();
				for (File f : files) {
					writeZip(f, parentPath, zos);
				}
			} else {
				FileInputStream fis = null;
				try {
					fis = new FileInputStream(file);
					ZipEntry ze = new ZipEntry(parentPath + file.getName());
					zos.putNextEntry(ze);
					byte[] content = new byte[1024];
					int len;
					while ((len = fis.read(content)) != -1) {
						zos.write(content, 0, len);
						zos.flush();
					}

				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						if (fis != null) {
							fis.close();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * Java文件操作 获取文件扩展名
	 */
	public static String getExtensionName(String filename) {
		if ((filename != null) && (filename.length() > 0)) {
			int dot = filename.lastIndexOf('.');
			if ((dot > -1) && (dot < (filename.length() - 1))) {
				return filename.substring(dot + 1);
			}
		}
		return filename;
	}

	/**
	 * Java文件操作 获取不带扩展名的文件名
	 */
	public static String getFileNameNoEx(String filename) {
		if ((filename != null) && (filename.length() > 0)) {
			int dot = filename.lastIndexOf('.');
			if ((dot > -1) && (dot < (filename.length()))) {
				return filename.substring(0, dot);
			}
		}
		return filename;
	}
}
