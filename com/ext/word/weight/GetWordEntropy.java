package com.ext.word.weight;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;

import com.ext.seg.word.SegWord;
import com.ext.tfidf.WordWeight;

/**
 * 计算句子中每个词的信息熵(还可通过加命名实体等增加权重计算步骤)
 * 
 * @author Administrator
 *
 */
public class GetWordEntropy {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		String sentence = "拿到手机用了一下，首先说说优点：原生的android系统跟国内定制过的系统可以说是天壤之别，十分流畅，毫不弱于ios，其次moto的手机手感也不错；当然这款手机也有一些缺点，比如使用的习惯跟其他国产手机有些区别。";
		
		String file = "E:\\testfile\\test.txt";
		
		//String[] words = SegWord.segmentByJieba(sentence);
		
		//ArrayList<String> listWords = SegWord.segmentByIk(sentence);
		
		ArrayList<String> listWords2 = WordWeight.cutWords(file);
		
		String[] words2 = new String[listWords2.size()];
		
		for(int i = 0; i < listWords2.size(); i++){
			
			words2[i] = listWords2.get(i);
			
		}
		
		CalculateWordEntropy(words2);

	}

	public static String[] CalculateWordEntropy(String[] words) {

		int length = words.length;

		ArrayList<String[]> wordList = new ArrayList<String[]>();
		// 将分好的词每3个一组存到数组中
		for (int i = 0; i < length; i++) {

			String[] wordSeg = new String[3];
			if (i == 0) {
				wordSeg[0] = "null";
				wordSeg[1] = words[i];
				wordSeg[2] = words[i + 1];
			} else if (i == length - 1) {
				wordSeg[0] = words[i - 1];
				wordSeg[1] = words[i];
				wordSeg[2] = "null";
			} else {
				wordSeg[0] = words[i - 1];
				wordSeg[1] = words[i];
				wordSeg[2] = words[i + 1];
			}

			wordList.add(wordSeg);

		}
		// 去除重复的词
		List<String> lists = new ArrayList<String>();
		for (int l = 0; l < length; l++) {
			lists.add(words[l]);
		}
		List<String> tempList = new ArrayList<String>();
		for (String str : lists) {
			if (!(tempList.contains(str))) {
				tempList.add(str);
			}
		}
		String[] wordClean = new String[tempList.size()];
		for (int m = 0; m < tempList.size(); m++) {
			wordClean[m] = tempList.get(m);
		}
		// 统计每个词的词频
		int[] frequent = new int[wordClean.length];
		for (int j = 0; j < wordClean.length; j++) {
			int count = 0;
			for (int k = 0; k < words.length; k++) {
				if (wordClean[j].equals(words[k])) {
					count++;
				}
			}
			frequent[j] = count;
		}
		// 将三元组中中间的那个词相同的存到一个list中，然后计算该词的信息熵
		double[] allEntropy = new double[wordClean.length];
		for (int n = 0; n < wordClean.length; n++) {
			ArrayList<String[]> wordSegList = new ArrayList<String[]>();
			int count = 0;
			for (int p = 0; p < wordList.size(); p++) {
				String[] wordSegStr = wordList.get(p);
				if (wordSegStr[1].equals(wordClean[n])) {
					count++;
					wordSegList.add(wordSegStr);
				}
			}
			String[] leftword = new String[wordSegList.size()];
			String[] rightword = new String[wordSegList.size()];
			// 计算左信息熵
			for (int i = 0; i < wordSegList.size(); i++) {
				String[] left = wordSegList.get(i);
				leftword[i] = left[0];
			}
			// 去除左边重复的词
			List<String> listsLeft = new ArrayList<String>();
			for (int l = 0; l < leftword.length; l++) {
				listsLeft.add(leftword[l]);
			}
			List<String> tempListLeft = new ArrayList<String>();
			for (String str : listsLeft) {
				if (!(tempListLeft.contains(str))) {
					tempListLeft.add(str);
				}
			}
			String[] leftWordClean = new String[tempListLeft.size()];
			for (int m = 0; m < tempListLeft.size(); m++) {
				leftWordClean[m] = tempListLeft.get(m);
			}
			// 统计左边每个词的词频
			int[] leftFrequent = new int[leftWordClean.length];
			for (int j = 0; j < leftWordClean.length; j++) {
				int leftcount = 0;
				for (int k = 0; k < leftword.length; k++) {
					if (leftWordClean[j].equals(leftword[k])) {
						leftcount++;
					}
				}
				leftFrequent[j] = leftcount;
			}
			// 计算左熵值
			double leftEntropy = 0;
			for (int i = 0; i < leftFrequent.length; i++) {
				double a = (double) leftFrequent[i] / count;
				double b = Math.log((double) leftFrequent[i] / count);
				leftEntropy += -a * b;
				// leftEntropy +=
				// (-(double)(leftFrequent[i]/count))*Math.log((double)(leftFrequent[i]/count));
			}
			// 计算右信息熵
			for (int i = 0; i < wordSegList.size(); i++) {
				String[] right = wordSegList.get(i);
				rightword[i] = right[2];
			}
			// 去除右边重复的词
			List<String> listsRight = new ArrayList<String>();
			for (int l = 0; l < rightword.length; l++) {
				listsRight.add(rightword[l]);
			}
			List<String> tempListRight = new ArrayList<String>();
			for (String str : listsRight) {
				if (!(tempListRight.contains(str))) {
					tempListRight.add(str);
				}
			}
			String[] rightWordClean = new String[tempListRight.size()];
			for (int m = 0; m < tempListRight.size(); m++) {
				rightWordClean[m] = tempListRight.get(m);
			}
			// 统计右边每个词的词频
			int[] rightFrequent = new int[rightWordClean.length];
			for (int j = 0; j < rightWordClean.length; j++) {
				int rightcount = 0;
				for (int k = 0; k < rightword.length; k++) {
					if (rightWordClean[j].equals(rightword[k])) {
						rightcount++;
					}
				}
				rightFrequent[j] = rightcount;
			}
			// 计算右熵值
			double rightEntropy = 0.0;
			for (int i = 0; i < rightFrequent.length; i++) {
				double a = (double) rightFrequent[i] / count;
				double b = Math.log((double) rightFrequent[i] / count);
				rightEntropy += -a * b;
				// rightEntropy +=
				// (-(double)(rightFrequent[i]/count))*Math.log((double)(rightFrequent[i]/count));
			}
			// 计算词的总信息熵
			double wordEntropy = leftEntropy + rightEntropy;
			allEntropy[n] = wordEntropy;

		}

		String[] EntropyResult = new String[allEntropy.length];
		
		//HashMap<String,Double> entropySet = new HashMap<String,Double>();
		
		for (int i = 0; i < allEntropy.length; i++) {
			EntropyResult[i] = wordClean[i] + ":" + allEntropy[i];
			//entropySet.put(wordClean[i],allEntropy[i]);
			//System.out.println(EntropyResult[i]);
		}
		
		return EntropyResult;
	}
	
	public static double getAllEntropy(String[] word){
		
		double entropy = 0.0;
		
		String[] EntropyResult = CalculateWordEntropy(word);
		
		if(EntropyResult != null){
			
			for(int i = 0; i < EntropyResult.length;i++){
				
				String[] result = EntropyResult[i].split(":");
				
				if(result[1].toString().trim() != null && result[1].toString().trim().length() > 0){
					
					entropy = entropy + Double.valueOf(result[1].toString().trim());
					
				}
			}
		}

		return entropy;
		
	}

}
