package com.ext.conf;

import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

/**
 * 获取配置文件目录
 * 
 * @author srcb04161
 * 
 */
public class Config {

	public static String APP_PATH = "";
	public static boolean isWebApp = false;
	public static final boolean PRINT = false;

	public static final boolean ENCRYPT = false;

	static {

		APP_PATH = getAppPath() + "/";
		APP_PATH = postAppPath(APP_PATH);

	}

	public static String CONF_PATH = APP_PATH + "conf/";
	public static String SEG_PATH = APP_PATH + "conf/seg/";
	public static String DICT_PATH = APP_PATH + "conf/dict/";

	static {
		SetAppPath(APP_PATH);
	}

	public static void SetAppPath(String path) {
		SEG_PATH = getFullPath(SEG_PATH);
		DICT_PATH = getFullPath(DICT_PATH);
	}

	public static String getFullPath(String path) {
		if (path == null || path.equals(""))
			return null;
		path = path.replace('\\', '/');

		if (path.charAt(0) == '/')
			return path;
		if (path.indexOf(":") != -1)
			return path;

		String curPath = APP_PATH;
		if (path.length() >= 2 && path.substring(0, 2).equals("..")) {
			int index = curPath.lastIndexOf("/");
			if (index != -1)
				curPath = curPath.substring(0, index);
			path = path.substring(1, path.length());
		}

		if (path.charAt(0) == '.')
			path = path.substring(1, path.length());

		return curPath + path;
	}

	private static String getAppPath() {
		String appPath = null;
		String clsName = Config.class.getName();
		clsName = clsName.replace('.', '/');
		clsName += ".class";

		java.net.URL url = Config.class.getClassLoader().getResource(clsName);
		try {
			appPath = java.net.URLDecoder.decode(url.toString(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			System.exit(0);
		}
		int pos = appPath.indexOf("file:");
		if (pos != -1)
			appPath = appPath.substring(pos + 5);

		pos = appPath.indexOf(clsName);
		if (pos != -1)
			appPath = appPath.substring(0, pos - 1);

		if (appPath.endsWith("!"))
			appPath = appPath.substring(0, appPath.lastIndexOf("/"));

		if (appPath.indexOf(":") != -1 && appPath.charAt(0) == '/')
			appPath = appPath.substring(1, appPath.length());

		return appPath;
	}

	private static String postAppPath(String path) {
		if (!path.isEmpty()) {
			int pos = path.lastIndexOf("WEB-INF/");
			if (pos != -1)
				isWebApp = true;
			else
				pos = path.lastIndexOf("bin/");
			if (pos != -1)
				path = path.substring(0, pos);
		}
		return path;
	}
}
