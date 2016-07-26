package com.ext.entrance;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.ext.conf.Sentence;
import com.ext.feature.GetSimilarityRatio;
import com.ext.feature.SenAndTitleSimilarityRatio;
import com.ext.feature.SentenceAndTopicWord;
import com.ext.feature.SentencePosition;
import com.ext.feature.TitleClassification;
import com.ext.tfidf.WordWeight;

public class Main {

	public static ArrayList<String> getTopicSentence(String content, String title, ArrayList<String> lableList) throws Exception {
		// TODO Auto-generated method stub

		//String title = "阿里荣威联合发布首款互联网汽车 下月开卖";
		//String title = "谭维克将赴市社科院担任领导职务";
//		String reviewContent = null;
//		String filepath = "D:\\ExtTopic\\testfile\\test4.txt";
//		try {
//			reviewContent = WordWeight.readFile(filepath);
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		//System.out.println(s);
//		ArrayList<String> lableList = new ArrayList<String>();
//		
//		lableList.add("阿里");
//		lableList.add("无人驾驶");
//		lableList.add("互联网");
		//lableList.add("Skin");

		//判断标题是否有提示性,如果标题具有提示性可将标题作为主题句
		int titleCategory = TitleClassification.titleClassification(title, content, lableList);
		
		ArrayList<ArrayList<String>> featureResult = SentencePosition.sentencePosition(lableList, content, title);
		
		Map<String,Double> senWeightMap = new HashMap<String,Double>();
		
		int length = featureResult.size();
		
		String[] sentence = new String[length];
		
		double[] fvalue = new double[length];
		
		int idx = 0;
		
		//保存句子内容和句子对应权重
		List<Sentence> result = new ArrayList<Sentence>();
		
		Sentence item;
		
		for(ArrayList<String> sentenceList : featureResult){
			
			double f1 = 0.05 * Double.valueOf(sentenceList.get(1));
			
			double f2 = 0.05 * Double.valueOf(sentenceList.get(2));
			
			double f3 = 0.05 * Double.valueOf(sentenceList.get(3));
			
			double f4 = titleCategory * 0.2 * Double.valueOf(sentenceList.get(4));
			
			double f5 = titleCategory * 0.2 * Double.valueOf(sentenceList.get(5));
			
			double f6 = 0.15 * Double.valueOf(sentenceList.get(6));
			
			double f7 = 0.3 * Double.valueOf(sentenceList.get(7));

			double sentenceWeight = f1 + f2 + f3 + f4 + f5 + f6 + f7;
			
			sentence[idx] = sentenceList.get(0);
			
			fvalue[idx] = sentenceWeight;
			
			idx++;
			
			//System.out.println(sentenceList.get(0) + ":" + sentenceWeight);
			
			senWeightMap.put(sentenceList.get(0), sentenceWeight);
			
            item = new Sentence();
			
			item.setContent(sentenceList.get(0));
			
			item.setWeight(sentenceWeight);
			
			result.add(item);
			
		}
		
		//计算两个句子的相似度，若两个句子的相似度大于0.6则输出权重大的句子，若权重相同则两个句子都输出
//		ArrayList<String> sentenceArray = new ArrayList<String>();
//		ArrayList<Double> ffvalue = new ArrayList<Double>();
//		double[][] ratio = new double[sentence.length][sentence.length];
//		
//		for(int m = 0;m < sentence.length;m++){
//			for(int n = m;n < sentence.length;n++){
//				
//				double similarityRatio = GetSimilarityRatio.getSimilarityRatio(sentence[m], sentence[n]);
//				ratio[m][n] = similarityRatio;
//				
//				if(similarityRatio > 0.6){
//					if(fvalue[m] < fvalue[n]){
//						sentence[m] = sentence[n];
//						fvalue[m] = fvalue[n];
//					}
//				}
//				sentenceArray.add(sentence[m]);
//				ffvalue.add(fvalue[m]);
//				
//			}
//		}
		
		
		//使用冒泡排序实现句子与权重同时排序
		for(int i = 0;i < sentence.length;i++){
			for(int j = i;j < sentence.length;j++){
				if(fvalue[i] < fvalue[j]){
					double value = fvalue[j];
					fvalue[j] = fvalue[i];
					fvalue[i] = value;
					
					String sentenceStr = sentence[j];
					sentence[j] = sentence[i];
					sentence[i] = sentenceStr;
					
				}
			}
		}
		
		//保存最终结果
		ArrayList<String> sentenceResult = new ArrayList<String>();
		
		//如果标题具有提示性则将标题作为主题句
		if(titleCategory == 1){
			sentenceResult.add(title + "\t" + 10.0);
			//System.out.println(title + ":" + 10.0);
		}
		//文章总句数若少于10句则全部为主题句，若多于10句则取1/3为主题句
//		int sentenceNum = sentence.length;
//		
//		if(sentenceNum > 10){
//			
//			sentenceNum = sentenceNum/3;
//			
//		}
		
		for(int k = 0;k < sentence.length;k++){
			sentenceResult.add(sentence[k] + "\t" + fvalue[k]);
			//System.out.println(sentence[k] + ":" + fvalue[k]);
		}
		return sentenceResult;
		
//		List<String> keyList = new LinkedList<String>();
//	    keyList.addAll(senWeightMap.keySet());
//		List<Double> valueList = new LinkedList<Double>();
//		valueList.addAll(senWeightMap.values());
//		for(int i = 0;i < valueList.size();i++){
//			for(int j = i+1;j < valueList.size();j++){
//				if(valueList.get(j) > valueList.get(i)){
//					valueList.set(j, valueList.get(i));
//					valueList.set(i, valueList.get(j));
//					
//					keyList.set(j, keyList.get(i));
//					keyList.set(i, keyList.get(j));
//				}
//			}
//		}
//		
//		Map<String,Double> sortWeight = new HashMap<String,Double>();
//		
//		for(int k = 0;k < keyList.size();k++){
//			System.out.println(keyList.get(k)+ ":" +valueList.get(k));
//			sortWeight.put(keyList.get(k), valueList.get(k));
//		}
//		
//		Iterator iter = sortWeight.entrySet().iterator();
//		while(iter.hasNext()){
//			
//			Map.Entry<String, Double> entry = (Entry<String, Double>) iter.next();
//			String key = entry.getKey();
//			Double value = entry.getValue();
//			//System.out.println(key + ":" + value);
//			
//		}

	}

	private static boolean isNaN(String result) {
		// TODO Auto-generated method stub
		if(result.equals("NaN") || result.equals("Infinity")){
			return true;
		}
		
		return false;
	}

}
