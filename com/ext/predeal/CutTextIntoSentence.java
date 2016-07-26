package com.ext.predeal;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.StringTokenizer;

import com.ext.tfidf.WordWeight;

public class CutTextIntoSentence {

	public static void main(String[] args) {

		String s = "拿到手机用了一下，首先说说优点：原生的android系统跟国内定制过的系统可以说是天壤之别！十分流畅，毫不弱于ios，其次moto的手机手感也不错!\n当然这款手机也有一些缺点，比如使用的习惯跟其他国产手机有些区别。";
		// System.out.println(s);
		String filepath = "D:\\ExtTopic\\file\\test.txt";
		String reviewContent = null;
		try {
			reviewContent = WordWeight.readFile(filepath);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ArrayList<String> test = cutTextIntoSentences(reviewContent, 0);
		for (String c : test) {
			System.out.println(c);
		}

	}

	public static ArrayList<String> cutTextIntoSentences(String reviewContent,
			int tag) {
		ArrayList<String> clauses = new ArrayList<String>();
		// tag=0，只分句;tag=1,记录句子在文章中是第几句;tag=2,记录段落位置等信息
		if (tag == 0) {
			// 去掉回车符
			reviewContent = reviewContent.replaceAll("\r\n", "");
			reviewContent = reviewContent.replaceAll("\n", "");

			ArrayList<String> SubSentences = new ArrayList<String>();
			BreakIterator boundary = BreakIterator
					.getSentenceInstance(Locale.CHINESE);
			boundary.setText(reviewContent);
			int start = boundary.first();
			for (int end = boundary.next(); end != BreakIterator.DONE; start = end, end = boundary
					.next()) {
				SubSentences.add(reviewContent.substring(start, end));
			}
			for (String sent : SubSentences) {
				clauses.add(sent.trim());
			}
		} else if (tag == 1) {
			// 去掉回车符
			reviewContent = reviewContent.replaceAll(":", "");
			reviewContent = reviewContent.replaceAll("\r\n", "");
			reviewContent = reviewContent.replaceAll("\n", "");
			ArrayList<String> SubSentences = new ArrayList<String>();
			BreakIterator boundary = BreakIterator
					.getSentenceInstance(Locale.CHINESE);
			boundary.setText(reviewContent);
			int start = boundary.first();
			// 记录句子在文章中是第几句
			int idx = 1;
			for (int end = boundary.next(); end != BreakIterator.DONE; start = end, end = boundary
					.next()) {
				SubSentences.add(reviewContent.substring(start, end) + ":"
						+ idx);
				idx++;
			}
			for (String sent : SubSentences) {
				clauses.add(sent.trim());
			}
		} else if (tag == 2) {
			String[] text = reviewContent.split("\t");

			ArrayList<String> textList = new ArrayList<String>();
			for (int i = 0; i < text.length; i++) {
				if (text[i].equals("")) {
					continue;
				}
				textList.add(text[i]);
			}

			// 记录句子在整篇文章中是第几句
			int j = 1;
			for (int i = 0; i < textList.size(); i++) {
				// 记录句子在该段落中是第几句
				int k = 1;
				ArrayList<String> SubSentencesIdx = new ArrayList<String>();
				BreakIterator boundary = BreakIterator
						.getSentenceInstance(Locale.CHINESE);
				boundary.setText(textList.get(i));
				int start = boundary.first();
				for (int end = boundary.next(); end != BreakIterator.DONE; start = end, end = boundary
						.next()) {
					SubSentencesIdx.add(textList.get(i).substring(start, end)
							+ ":" + i + ":" + j + ":" + k);
					k++;
					j++;
				}
				for (String sent : SubSentencesIdx) {
					clauses.add(sent.trim());
				}
			}
		}

		return clauses;
	}
}
