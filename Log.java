package com.example.aaa;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

/**
 * Log工具，类似android.util.Log。 tag自动产生，格式:
 * customTagPrefix:className.methodName(L:lineNumber),
 */
public class LogUtils {

	public static String customTagPrefix = "";

	private LogUtil() {
	}

	private static boolean LOG = true;
	private static boolean LOG2FILE = true;
	private static File saveFile;
	private static SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());// 日志的输出格式
	private static int deleteDay = 1; // 每周1清除日志文件
	static {
		saveFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/padStudy01/log.txt");
		if (!saveFile.getParentFile().exists()) {
			saveFile.getParentFile().mkdirs();
		}
		if (saveFile.exists()) {
			Calendar calendar = Calendar.getInstance();
			int day = calendar.get(Calendar.DAY_OF_WEEK) - 1;
			if (day == deleteDay) {
				saveFile.delete();
			}
		}
	}

	private static String generateTag() {
		StackTraceElement caller = getCallerStackTraceElement();
		String tag = "%s.%s(L:%d)";
		String callerClazzName = caller.getClassName();
		callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1);
		tag = String.format(tag, callerClazzName, caller.getMethodName(), caller.getLineNumber());
		tag = TextUtils.isEmpty(customTagPrefix) ? tag : customTagPrefix + ":" + tag;
		return tag;
	}

	public static void i(String content) {
		String tag = generateTag();
		if (LOG) {
			Log.i(tag, content);
		}
		if (LOG2FILE) {
			Log2File(tag, content);
		}
	}

	public static void i(String content, Throwable tr) {
		String tag = generateTag();
		if (LOG) {
			Log.i(tag, content, tr);
		}
		if (LOG2FILE) {
			Log2File(tag, content);
		}
	}

	public static void wtf(String content) {
		String tag = generateTag();
		if (LOG) {
			Log.wtf(tag, content);
		}
		if (LOG2FILE) {
			Log2File(tag, content);
		}
	}

	/** LOG 写入文件 */
	private static void Log2File(String tag, String text) {
		String msg = timeFormat.format(System.currentTimeMillis()) + "    " + "    " + tag + "    " + text;
		FileWriter fw = null;
		BufferedWriter bw = null;
		try {
			fw = new FileWriter(saveFile, true);
			bw = new BufferedWriter(fw);
			bw.write(msg);
			bw.write("\r\n");
			bw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bw != null) {
					bw.close();
				}
				if (fw != null) {
					fw.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/** 获取类信息，[3]是当前线程，[4]是调它的线程... */
	public static StackTraceElement getCallerStackTraceElement() {
		return Thread.currentThread().getStackTrace()[5];
	}
}
