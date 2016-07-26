package com.ext.seg.word;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import org.wltea.analyzer.lucene.IKAnalyzer;

import com.ext.conf.Config;
import com.ext.tfidf.WordWeight;
import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.JiebaSegmenter.SegMode;
import com.huaban.analysis.jieba.SegToken;
import com.huaban.analysis.jieba.WordDictionary;

public class SegWord {

	private static JiebaSegmenter segmenter = null;

	public static void init() {
		if (segmenter == null) {
			WordDictionary.getInstance().init(new File(Config.SEG_PATH));
			segmenter = new JiebaSegmenter();

		}
	}

//	public static ArrayList<String> segment(String sentence) {
//		init();
//		List<SegToken> words = segmenter.process(sentence, SegMode.SEARCH);
//		int iSize = words.size();
//		ArrayList<String> word = new ArrayList<String>();
//		for (int i = 0; i < iSize; i++) {
//			SegToken seg = words.get(i);
//			word.add(sentence.substring(seg.startOffset, seg.endOffset));
//		}
//		return word;
//	}

	public static String[] segmentByJieba(String sentence) {
		init();
		String senStr = sentence.trim();
		List<SegToken> words = segmenter.process(senStr, SegMode.SEARCH);
		int iSize = words.size();
		String[] word = new String[iSize];
		for (int i = 0; i < iSize; i++) {
			SegToken seg = words.get(i);
			word[i] = sentence.substring(seg.startOffset, seg.endOffset);
		}

		return word;
	}
	
	public static ArrayList<String> segmentByIk(String sentence) throws Exception {
		ArrayList<String> words = new ArrayList<String>();
		IKAnalyzer analyzer = new IKAnalyzer(true);
		words = analyzer.split(sentence);
		return words;
	}
	
	private static String clearStr(String word) {
		String key = word;
		if (null != word && !"".equals(word.trim())) {
			key = word.trim().toLowerCase();
		}
		return key;
	}

	public static void main(String args[]) throws Exception{
		
		String str = "屌丝，一个字头的诞生，屌丝";
		String[] word = segmentByJieba(str);
		ArrayList<String> list = segmentByIk(str);
		for(int i=0;i<list.size();i++){
			System.out.print(list.get(i) + "	");
		}
		System.out.println();
		for(int i=0;i<word.length;i++){
			System.out.print(word[i] + "	");
		}
	}
	
}
