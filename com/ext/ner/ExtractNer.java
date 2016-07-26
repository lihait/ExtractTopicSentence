package com.ext.ner;

import java.io.IOException;
import java.util.ArrayList;

import org.wltea.analyzer.lucene.IKAnalyzer;

import com.ext.seg.word.SegWord;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;

/*

 加载NER模块

 */

public class ExtractNer

{

	private static AbstractSequenceClassifier<CoreLabel> ner;

	public ExtractNer()

	{

		InitNer();

	}

	public void InitNer()

	{

		String serializedClassifier = "ner/chinese.misc.distsim.crf.ser.gz";// chinese.misc.distsim.crf.ser.gz

		if (ner == null)

		{

			ner = CRFClassifier.getClassifierNoExceptions(serializedClassifier);

		}

	}

	public String doNer(String sent)

	{

		return ner.classifyWithInlineXML(sent);

	}

	public ArrayList<String> cutWord(String sentence) throws Exception {

		ArrayList<String> words = new ArrayList<String>();

		IKAnalyzer analyzer = new IKAnalyzer(true);

		words = analyzer.split(sentence);

		return words;
	}
	
	public static ArrayList<String> getNerResultByJieba(String sentence) {

		ArrayList<String> nerResult = new ArrayList<String>();

		String[] word = SegWord.segmentByJieba(sentence);

		ExtractNer extractNer = new ExtractNer();

		for (int i = 0; i < word.length; i++) {

			nerResult.add(extractNer.doNer(word[i]));

		}

		return nerResult;
	}
	
	public ArrayList<String> getNerResultByIk(String sentence) throws IOException {

		ArrayList<String> nerResult = new ArrayList<String>();

		ArrayList<String> words = new ArrayList<String>();
		
		IKAnalyzer analyzer = new IKAnalyzer(true);
		
		words = analyzer.split(sentence);

		ExtractNer extractNer = new ExtractNer();

		for (int i = 0; i < words.size(); i++) {

			nerResult.add(extractNer.doNer(words.get(i)));

		}

		return nerResult;
	}

	public static void main(String args[])

	{

		String str = "去年开始，打开百度李毅吧，满屏的帖子大多含有“屌丝”二字，一般网友不仅不懂这词什么意思，更难理解这个词为什么会这么火。";

		ArrayList<String> words = new ArrayList<String>();

		String[] word = SegWord.segmentByJieba(str);

		ExtractNer extractNer = new ExtractNer();

		for (int i = 0; i < word.length; i++) {

			String strs = extractNer.doNer(word[i]);
			
			int begin = strs.indexOf("<");
			
			int end = strs.indexOf(">");
			
			System.out.println(extractNer.doNer(word[i]));

		}

		System.out.println("Complete!");

	}

}
