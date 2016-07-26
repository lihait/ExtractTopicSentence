package com.ext.dict;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;

import com.ext.conf.Config;


/**
 * 获取外部资源，如特征词库等
 * 
 * @author srcb04161
 * 
 */
public class WordDict {

//	static String featureDictPath = Config.DICT_PATH + "aspect_feature.jd";
//
//	static String negDictPath = Config.DICT_PATH + "aspect_neg.jd";
//
//	static String posDictPath = Config.DICT_PATH + "aspect_pos.jd";

	static String stopDictPath = Config.DICT_PATH + "stop.words";
	
	static String tagDictPath = Config.DICT_PATH + "tags.txt";

//	static String negationDictPath = Config.DICT_PATH + "aspect_negation.bk";

//	public static HashSet<?> featureDict = readTxtFile(featureDictPath);
//
//	public static HashSet<?> negDict = readTxtFile(negDictPath);
//
//	public static HashSet<?> posDict = readTxtFile(posDictPath);

	public static HashSet<?> stopDict = readTxtFile(stopDictPath);
	
	public static HashSet<?> tagDict = readTxtFile(tagDictPath);

//	public static HashSet<?> negationDict = readTxtFile(negationDictPath);

	/*
	 * 读取文件内容
	 */
	public static HashSet<String> readTxtFile(String filePath) {

		HashSet<String> DictSet = new HashSet<String>();

		try {

			String encoding = "utf8";

			File file = new File(filePath);

			if (file.isFile() && file.exists()) { // 判断文件是否存在

				InputStreamReader read = new InputStreamReader(
						new FileInputStream(file), encoding);// 设定文件格式

				BufferedReader bufferedReader = new BufferedReader(read);

				String lineTxt = null;

				while ((lineTxt = bufferedReader.readLine()) != null) {

					DictSet.add(lineTxt.trim());

				}

				read.close();

			} else {

				System.out.println("文件不存在！");

			}
		} catch (Exception e) {

			System.out.println("读取文件内容出错！");

			e.printStackTrace();

		}
		return DictSet;

	}

}
