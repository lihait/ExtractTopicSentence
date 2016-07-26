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

public class CutTextIntoSentences {

	public static void main(String[] args) {

		String s = "拿到手机用了一下，首先说说优点：原生的android系统跟国内定制过的系统可以说是天壤之别！十分流畅，毫不弱于ios，其次moto的手机手感也不错!\n当然这款手机也有一些缺点，比如使用的习惯跟其他国产手机有些区别。";
		//System.out.println(s);
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
		ArrayList<String> test = cutTextIntoSentences(reviewContent);
		for (String c : test) {
			System.out.println(c);
		}

	}

	public static ArrayList<String> cutTextIntoSentences(String reviewContent) {
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

		ArrayList<String> clauses = new ArrayList<String>();
		
		// 如果句子太长，进一步分句；将来可考虑全都以逗号分句
		for (String sent : SubSentences) {
			if (sent.length() >= 300) {
				StringTokenizer token_comma = new StringTokenizer(sent, "，,；",
						false);
				if (token_comma.countTokens() > 1) {
					while (token_comma.hasMoreTokens()) {
						String clause = token_comma.nextToken().trim();
						if (clause.length() > 0) {
							clauses.add(clause);
						}
					}
				}

			} else {
				clauses.add(sent.trim());
			}
		}

		return clauses;
	}

	//获取句子在文章中的位置
	public static ArrayList<String> cutTextIntoSentencesGetIdx(String reviewContent) {
		// 去掉回车符
		reviewContent = reviewContent.replaceAll(":", "");
		reviewContent = reviewContent.replaceAll("\r\n", "");
		reviewContent = reviewContent.replaceAll("\n", "");

		ArrayList<String> SubSentences = new ArrayList<String>();
		BreakIterator boundary = BreakIterator
				.getSentenceInstance(Locale.CHINESE);
		boundary.setText(reviewContent);
		int start = boundary.first();
		//记录句子在文章中是第几句
		int idx = 1;
		for (int end = boundary.next(); end != BreakIterator.DONE; start = end, end = boundary
				.next()) {
			SubSentences.add(reviewContent.substring(start, end) + ":" + idx);
			idx++;
		}

		ArrayList<String> clauses = new ArrayList<String>();

		// 如果句子太长，进一步分句；
		for (String sent : SubSentences) {
			String[] senArray = sent.split(":");
			String sentContent = senArray[0];
			String senIdx = senArray[1];
			int curIdx = Integer.parseInt(senIdx);
			int senidx = Integer.parseInt(senIdx);
			if (sentContent.length() >= 300) {
				StringTokenizer token_comma = new StringTokenizer(sentContent, "，,；",
						false);
				if (token_comma.countTokens() > 1) {
					while (token_comma.hasMoreTokens()) {
						String clause = token_comma.nextToken().trim();
						if (clause.length() > 0) {
							clauses.add(clause + ":" + senidx);
							senidx++;
						}
					}
				}

			} else {
				clauses.add(sent.trim());
			}
		}

		return clauses;
	}
	
	/*
	 * 把句子按行分段，标记句子在篇章中的位置
	 */
	public static ArrayList<String> cutTextIntoSentencesIdx(String reviewContent) {
		// 去掉特殊符号
		
		//reviewContent = reviewContent.replaceAll("", "");
		//reviewContent = reviewContent.replaceAll("\r", "");
		reviewContent = reviewContent.replaceAll(":", "");
		reviewContent = reviewContent.replaceAll("\t", "");
		reviewContent = reviewContent.replaceAll("\r\n", "\t");
		reviewContent = reviewContent.replaceAll("\n", "\t");

		String[] text = reviewContent.split("\t");
        
		ArrayList<String> textList = new ArrayList<String>();
		for(int i = 0 ;i < text.length;i++){
			if(text[i].equals("")){
				continue;
			}
			textList.add(text[i]);
		}

		// 保存句子在段落中的位置信息
		ArrayList<String> clausesIdx = new ArrayList<String>();
		//记录句子在整篇文章中是第几句
		int j = 1;
		for (int i = 0; i < textList.size(); i++) {
			//记录句子在该段落中是第几句
			int k = 1;
			ArrayList<String> SubSentencesIdx = new ArrayList<String>();
			BreakIterator boundary = BreakIterator
					.getSentenceInstance(Locale.CHINESE);
			boundary.setText(textList.get(i));
			int start = boundary.first();
			for (int end = boundary.next(); end != BreakIterator.DONE; start = end, end = boundary
					.next()) {
				SubSentencesIdx.add(textList.get(i).substring(start, end) + ":" + i
						+ ":" + j + ":" + k);
				k++;
				j++;
			}

			// 长句分句后该句内的每个句子的位置发生变化，但长句后的句子在段落中的位置不发生变化
			for (String sent : SubSentencesIdx) {
				String[] sentenceArray = sent.split(":");
				String sentenceText = sentenceArray[0];
				String paragraphIdx = sentenceArray[1];
				String idx = sentenceArray[2];
				String idx2 = sentenceArray[3];
				if (sentenceText.length() >= 300) {
					StringTokenizer token_comma = new StringTokenizer(
							sentenceText, "，,；", false);
					int Idx = Integer.parseInt(idx);
					int Idx2 = Integer.parseInt(idx2);
					if (token_comma.countTokens() > 1) {
						while (token_comma.hasMoreTokens()) {
							String clause = token_comma.nextToken().trim()
									+ ":" + paragraphIdx + ":" + Idx + ":" + Idx2;
							if (clause.length() > 0) {
								clausesIdx.add(clause);
								Idx++;
								Idx2++;
							}
						}
					}

				} else {
					clausesIdx.add(sent.trim());
				}
			}

		}

		for (String str : clausesIdx) {
			System.out.println(str);
		}

		return clausesIdx;

	}

}
