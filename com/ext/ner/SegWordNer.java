package com.ext.ner;

import java.io.File;

import java.io.IOException;

import java.util.Properties;

import org.apache.commons.io.FileUtils;

import edu.stanford.nlp.ie.crf.CRFClassifier;

import edu.stanford.nlp.ling.CoreLabel;

/*

 * Description 使用StanfordCoreNLP进行中文实体识别

 */

public class SegWordNer {

	public static CRFClassifier<CoreLabel> segmenter;

	static {

		// 设置一些初始化参数

		Properties props = new Properties();

		props.setProperty("sighanCorporaDict", "data");

		props.setProperty("serDictionary", "data/dict-chris6.ser.gz");

		props.setProperty("inputEncoding", "UTF-8");

		props.setProperty("sighanPostProcessing", "true");

		segmenter = new CRFClassifier < CoreLabel > (props);

		segmenter.loadClassifierNoExceptions("data/ctb.gz", props);

		segmenter.flags.setProperties(props);

	}

	public static String doSegment(String sent) {

		String[] strs = (String[]) segmenter.segmentString(sent).toArray();

		StringBuffer buf = new StringBuffer();

		for (String s : strs) {

			buf.append(s + " ");

		}

		System.out.println("segmentedres: " + buf.toString());

		return buf.toString();

	}

	public static void main(String[] args) {

		try {

			String readFileToString = FileUtils
					.readFileToString(new File("file/test.txt"));

			String doSegment = doSegment(readFileToString);

			System.out.println(doSegment);

			ExtractNer extractNer = new ExtractNer();

			System.out.println(extractNer.doNer(doSegment));

			System.out.println("Complete!");

		} catch (IOException e) {

			e.printStackTrace();

		}

	}

}
